package nl.tudelft.jenkins.tests.integration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.jenkins.auth.User;
import nl.tudelft.jenkins.auth.UserImpl;
import nl.tudelft.jenkins.client.exceptions.NoSuchJobException;
import nl.tudelft.jenkins.jobs.Job;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobCreationAndRemovalIntegrationTest extends AbstractJenkinsIntegrationTestBase {

	private static final Logger LOG = LoggerFactory.getLogger(JobCreationAndRemovalIntegrationTest.class);

	private static final User USER0 = new UserImpl("person", "person@example.com");
	private static final User USER1 = new UserImpl("other", "other@example.com");

	private static final List<User> USERS = new ArrayList<>();

	static {
		USERS.add(USER0);
		USERS.add(USER1);
	}

	@Test
	public void testThatCreationAndDeletionOfJobWorks() {

		LOG.debug("Testing creation, then deletion of job ...");

		Job job = createJob(JOB_SCM_URL, USERS);

		job = retrieveJob();

		deleteJob(job);

	}

	@Test
	public void testThatRetrievalOfNonExistingJobThrowsException() {

		LOG.debug("Testing deletion of non-existing job ...");

		final Job job = createJob(JOB_SCM_URL, USERS);

		deleteJob(job);

		boolean exceptionWasThrown = false;
		try {
			retrieveJob();
		} catch (final NoSuchJobException e) {
			exceptionWasThrown = true;
		}

		assertThat(exceptionWasThrown, is(true));

	}

}
