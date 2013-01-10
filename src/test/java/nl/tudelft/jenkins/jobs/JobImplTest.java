package nl.tudelft.jenkins.jobs;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
	private static final User JOB_NOTIFICATION_RECIPIENT0 = new UserImpl("person", "person@example.com");
	private static final User JOB_NOTIFICATION_RECIPIENT1 = new UserImpl("other", "other@otherexample.com");

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
		job.setScmUrl(JOB_SCM_URL);

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
	public void testThatAddingRecipientWithNullNameThrowsException() {
		job.addNotificationRecipient(new UserImpl("test", null));
	}

	@Test
	public void testThatSingleRecipientCanBeSet() throws Exception {
		job.clearNotificationRecipients();
		job.addNotificationRecipient(JOB_NOTIFICATION_RECIPIENT0);

		final String xml = job.asXml();

		assertThat(xml, containsString("<recipients>" + JOB_NOTIFICATION_RECIPIENT0.getEmail() + "</recipients>"));
	}

	@Test
	public void testThatSecondRecipientCanBeAdded() throws Exception {
		job.clearNotificationRecipients();
		job.addNotificationRecipient(JOB_NOTIFICATION_RECIPIENT0);
		job.addNotificationRecipient(JOB_NOTIFICATION_RECIPIENT1);

		final String xml = job.asXml();

		assertThat(xml, containsString("<recipients>" + JOB_NOTIFICATION_RECIPIENT0.getEmail() + " " + JOB_NOTIFICATION_RECIPIENT1.getEmail() + "</recipients>"));
	}

	@Test
	public void testThatUserCanBeAdded() throws Exception {
		User user = new UserImpl("name", "email");
		job.addUser(user);

		List<User> users = job.getUsers();

		assertThat(users, hasSize(1));

		User u = users.iterator().next();
		assertThat(u.getName(), is(equalTo(user.getName())));
	}

}
