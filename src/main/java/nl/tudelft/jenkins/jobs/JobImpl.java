package nl.tudelft.jenkins.jobs;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static nl.tudelft.commons.XmlUtils.findSingleElementInDocumentByXPath;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import nl.tudelft.commons.XmlUtils;
import nl.tudelft.jenkins.auth.User;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobImpl implements Job {

	/**
	 * If true, Jobs are created with limited permissions, specifically tailored
	 * for use with DevHub.
	 */
	private static final boolean DEVHUB = true;

	private static final Logger LOG = LoggerFactory.getLogger(JobImpl.class);

	private final String name;
	private final Document document;

	private final JobPermissionMatrix permissionMatrix;

	private static final String XPATH_PROPERTIES_SECURITY = "//maven2-moduleset/properties/hudson.security.AuthorizationMatrixProperty";
	private static final String XPATH_NOTIFICATION_RECIPIENTS = "//maven2-moduleset/reporters/hudson.maven.reporters.MavenMailer/recipients";
	private static final String XPATH_SCM_GIT_URL = "//maven2-moduleset/scm/userRemoteConfigs/hudson.plugins.git.UserRemoteConfig/url";

	public JobImpl(final String name) {
		this(name, JobDocumentProvider.createDefaultJobDocument());
	}

	public JobImpl(final String name, final Document document) {
		checkArgument(isNotEmpty(name), "name must be non-empty");

		this.name = name;
		this.document = checkNotNull(document, "document must be non-null");

		final Element element = findSingleElementInDocumentByXPath(document, XPATH_PROPERTIES_SECURITY);
		permissionMatrix = JobPermissionMatrixImpl.fromElement(element);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setScmUrl(final String scmUrl) {
		LOG.trace("Setting SCM URL to: {}", scmUrl);

		checkArgument(isNotEmpty(scmUrl), "scmUrl must be non-empty");

		final Element url = findSingleElementInDocumentByXPath(document, XPATH_SCM_GIT_URL);

		url.setContent(new Text(scmUrl));
	}

	@Override
	public String getScmUrl() {
		throw new NotImplementedException();
	}

	@Override
	public List<User> getUsers() {
		return permissionMatrix.getUsers();
	}

	@Override
	public void addPermissionsForUser(User user) {
		LOG.trace("Adding user with full permissions: {}", user);

		checkNotNull(user, "user");

		if (DEVHUB) {
			addDevHubPermissionsForUser(user);
		} else {
			addFullPermissionsForUser(user);
		}
	}

	@Override
	public void removePermissionsForUser(User user) {
		LOG.trace("Removing user: {}", user);

		checkNotNull(user, "user must be non-null");

		permissionMatrix.removeAllPermissionsForUser(user);

		Element authMatrix = findSingleElementInDocumentByXPath(document, XPATH_PROPERTIES_SECURITY);

		Iterator<Element> iterator = authMatrix.getChildren().iterator();
		while (iterator.hasNext()) {
			Element permission = iterator.next();
			if (permission.getText().endsWith(user.getName())) {
				iterator.remove();
			}
		}

	}

	private void addDevHubPermissionsForUser(User user) {
		permissionMatrix.addPermission(user, JobAuthMatrixPermission.JOB_DISCOVER);
		permissionMatrix.addPermission(user, JobAuthMatrixPermission.JOB_READ);
		permissionMatrix.addPermission(user, JobAuthMatrixPermission.JOB_WORKSPACE);
	}

	private void addFullPermissionsForUser(User user) {
		permissionMatrix.addPermission(user, JobAuthMatrixPermission.JOB_BUILD);
		permissionMatrix.addPermission(user, JobAuthMatrixPermission.JOB_CANCEL);
		permissionMatrix.addPermission(user, JobAuthMatrixPermission.JOB_CONFIGURE);
		permissionMatrix.addPermission(user, JobAuthMatrixPermission.JOB_DELETE);
		permissionMatrix.addPermission(user, JobAuthMatrixPermission.JOB_DISCOVER);
		permissionMatrix.addPermission(user, JobAuthMatrixPermission.JOB_READ);
		permissionMatrix.addPermission(user, JobAuthMatrixPermission.JOB_WORKSPACE);
		permissionMatrix.addPermission(user, JobAuthMatrixPermission.RUN_DELETE);
		permissionMatrix.addPermission(user, JobAuthMatrixPermission.RUN_UPDATE);
		permissionMatrix.addPermission(user, JobAuthMatrixPermission.SCM_TAG);
	}

	@Override
	public void clearNotificationRecipients() {
		LOG.trace("Clearing notification recipient list...");

		final Element recipients = findSingleElementInDocumentByXPath(document, XPATH_NOTIFICATION_RECIPIENTS);

		recipients.removeContent();
	}

	@Override
	public void addNotificationRecipient(final User recipient) {
		LOG.trace("Adding additional notification recipient: {}", recipient);

		checkNotNull(recipient, "recipient must be non-null");
		checkArgument(isNotEmpty(recipient.getEmail()), "recipient.email must be non-empty");
		checkArgument(recipient.getEmail().contains("@"), "recipient.email must contain @");

		final Element recipients = findSingleElementInDocumentByXPath(document, XPATH_NOTIFICATION_RECIPIENTS);

		final int contentSize = recipients.getContentSize();
		if (contentSize == 0) {
			recipients.setContent(new Text(recipient.getEmail()));
		} else if (contentSize == 1) {
			final Content content = recipients.getContent(0);
			final String value = content.getValue();
			recipients.setContent(new Text(value + " " + recipient.getEmail()));
		} else {
			throw new RuntimeException("Element on path " + XPATH_NOTIFICATION_RECIPIENTS + " contains multiple children. Single (text) element expected");
		}
	}

	@Override
	public void removeNotificationRecipient(User recipient) {
		LOG.trace("Removing notification recipient: {}", recipient);

		checkNotNull(recipient, "recipient must be non-null");
		checkArgument(isNotEmpty(recipient.getEmail()), "recipient.email must be non-empty");
		checkArgument(recipient.getEmail().contains("@"), "recipient.email must contain @");

		Element recipients = findSingleElementInDocumentByXPath(document, XPATH_NOTIFICATION_RECIPIENTS);
		String text = recipients.getText();
		String[] strings = text.split(" ");

		StringBuilder newText = new StringBuilder();
		for (String string : strings) {
			if (!string.isEmpty() && !string.equals(recipient.getEmail())) {
				newText.append(string);
				newText.append(" ");
			}
		}

		recipients.setText(newText.toString().trim());
	}

	@Override
	public String asXml() {

		LOG.trace("Generating XML representation...");

		final XMLOutputter outputter = new XMLOutputter();
		final String xml = outputter.outputString(document);

		return xml;

	}

	@Override
	public String toString() {
		final ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);

		builder.append("name", name);

		return builder.toString();
	}

	public static Job fromXml(final String name, final String xml) {
		LOG.trace("Creating job named {} from xml ...", name);

		checkArgument(isNotEmpty(name), "name must be non-empty");
		checkArgument(isNotEmpty(xml), "xml must be non-empty");

		final InputStream is = IOUtils.toInputStream(xml);

		final Document document = XmlUtils.createJobDocumentFrom(is);

		return new JobImpl(name, document);
	}

}
