package nl.tudelft.jenkins.jobs;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.util.List;

import nl.tudelft.jenkins.auth.User;
import nl.tudelft.jenkins.auth.UserImpl;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.junit.Before;
import org.junit.Test;

public class JobImplTest {

	private static final String JOB_NAME = "X";
	private static final String JOB_SCM_URL = "git://xyz";

	private static final String NAME0 = "person";
	private static final String NAME1 = "other";
	private static final String EMAIL0 = "person@example.com";
	private static final String EMAIL1 = "other@otherexample.com";

	private static final User USER0 = new UserImpl(NAME0, EMAIL0);
	private static final User USER1 = new UserImpl(NAME1, EMAIL1);

	private Job job;

	@Before
	public void setUp() {
		job = new JobImpl(JOB_NAME);
	}

	@Test
	public void testThatNewlyConsructedJobAsXmlReturnsDefaultJobConfiguration() throws Exception {
		final String jobAsXml = job.asXml();

		final SAXBuilder builder = new SAXBuilder();
		final InputStream is = this.getClass().getResourceAsStream(JobDocumentProvider.DEFAULT_JOB_FILE_NAME);
		final Document defaultJobDocument = builder.build(is);

		final XMLOutputter xmlOutputter = new XMLOutputter();
		final String defaultJobAsXml = xmlOutputter.outputString(defaultJobDocument);

		assertThat(jobAsXml, is(equalTo(defaultJobAsXml)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatConstructorWithNullNameThrowsException() {
		new JobImpl(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatConstructorWithEmptyNameArgumentThrowsException() {
		new JobImpl("");
	}

	@Test
	public void testThatScmUrlIsSetCorrectly() throws Exception {
        GitScmConfig scmConfig = new GitScmConfig(JOB_SCM_URL);
		job.setScmConfig(scmConfig);

		final String xml = job.asXml();

		assertThat(xml, containsString("<url>" + JOB_SCM_URL + "</url>"));
	}

	@Test
	public void testThatRecipientListCanBeCleared() throws Exception {
		job.clearNotificationRecipients();

		final String xml = job.asXml();

		assertThat(xml, containsString("<recipients />"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatAddingRecipientWithNullEmailThrowsException() {
		job.addNotificationRecipient(new UserImpl("test", null));
	}

	@Test
	public void testThatAddingRecipientWithEmailWithoutAtSignThrowsException() {
		User recipient = new UserImpl("my-name", "my-email");

		boolean exceptionWasCaught = false;
		try {
			job.addNotificationRecipient(recipient);
		} catch (IllegalArgumentException e) {
			exceptionWasCaught = true;
		}
		assertThat(exceptionWasCaught, is(true));
	}

	@Test
	public void testThatSingleNotificationRecipientCanBeSet() throws Exception {
		job.clearNotificationRecipients();
		job.addNotificationRecipient(USER0);

		final String xml = job.asXml();

		assertThat(xml, containsString("<recipients>" + USER0.getEmail() + "</recipients>"));
	}

	@Test
	public void testThatSecondNotificationRecipientCanBeAdded() throws Exception {
		job.clearNotificationRecipients();
		job.addNotificationRecipient(USER0);
		job.addNotificationRecipient(USER1);

		final String xml = job.asXml();

		assertThat(xml, containsString("<recipients>" + USER0.getEmail() + " " + USER1.getEmail() + "</recipients>"));
	}

	@Test
	public void testThatNotificationRecipientCanBeRemoved() throws Exception {
		job.clearNotificationRecipients();

		job.addNotificationRecipient(USER0);
		job.addNotificationRecipient(USER1);

		job.removeNotificationRecipient(USER0);

		assertThat(job.asXml(), containsString("<recipients>other@otherexample.com</recipients>"));
	}

	@Test
	public void testThatUserPermissionsCanBeAdded() throws Exception {
		job.addPermissionsForUser(USER0);

		List<User> users = job.getUsers();

		assertThat(users, hasSize(1));

		User u = users.iterator().next();
		assertThat(u.getName(), is(equalTo(USER0.getName())));
	}

	@Test
	public void testThatUserPermissionsCanBeRemovedFromJob() throws Exception {
		job.addPermissionsForUser(USER0);
		job.addPermissionsForUser(USER1);

		assertThat(job.asXml(), containsString("<permission>hudson.model.Item.Read:" + NAME0 + "</permission>"));
		assertThat(job.asXml(), containsString("<permission>hudson.model.Item.Workspace:" + NAME0 + "</permission>"));
		assertThat(job.asXml(), containsString("<permission>hudson.model.Item.Read:" + NAME1 + "</permission>"));
		assertThat(job.asXml(), containsString("<permission>hudson.model.Item.Workspace:" + NAME1 + "</permission>"));

		User user = new UserImpl(NAME0, EMAIL0);
		job.removePermissionsForUser(user);

		assertThat(job.asXml(), not(containsString("<permission>hudson.model.Item.Read:" + NAME0 + "</permission>")));
		assertThat(job.asXml(), not(containsString("<permission>hudson.model.Item.Workspace:" + NAME0 + "</permission>")));
		assertThat(job.asXml(), containsString("<permission>hudson.model.Item.Read:" + NAME1 + "</permission>"));
		assertThat(job.asXml(), containsString("<permission>hudson.model.Item.Workspace:" + NAME1 + "</permission>"));
	}

}
