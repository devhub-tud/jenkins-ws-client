package nl.tudelft.jenkins.jobs;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.io.InputStream;
import java.util.List;

import nl.tudelft.jenkins.auth.User;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.filter.Filters;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobImpl implements Job {

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

		final Element element = findSingleElementInDocumentByXPath(XPATH_PROPERTIES_SECURITY);
		permissionMatrix = JobPermissionMatrixImpl.fromElement(element);

	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setScmUrl(final String scmUrl) {

		checkArgument(isNotEmpty(scmUrl), "scmUrl must be non-empty");

		final Element url = findSingleElementInDocumentByXPath(XPATH_SCM_GIT_URL);

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
	public void addUser(User user) {
		checkNotNull(user, "user");

		addFullPermissionsForUser(user);
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
	public void setNotificationRecipient(final User recipient) {

		checkNotNull(recipient, "recipient must be non-null");

		final Element recipients = findSingleElementInDocumentByXPath(XPATH_NOTIFICATION_RECIPIENTS);

		recipients.setContent(new Text(recipient.getEmail()));

	}

	@Override
	public void addNotificationRecipient(final User recipient) {

		checkNotNull(recipient, "recipient must be non-null");

		final Element recipients = findSingleElementInDocumentByXPath(XPATH_NOTIFICATION_RECIPIENTS);

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

		final Document document = JobDocumentProvider.createJobDocumentFrom(is);

		return new JobImpl(name, document);

	}

	private Element findSingleElementInDocumentByXPath(final String xPath) {

		final XPathFactory xPathFactory = XPathFactory.instance();
		final XPathExpression<Element> xPathExpression = xPathFactory.compile(xPath, Filters.element());
		final Element element = xPathExpression.evaluateFirst(document);

		if (element == null) {
			throw new RuntimeException("Document does not contain element on path: " + xPath);
		}

		return element;

	}

}
