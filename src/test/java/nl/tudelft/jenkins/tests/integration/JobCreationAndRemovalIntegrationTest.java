package nl.tudelft.jenkins.tests.integration;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.jenkins.auth.User;
import nl.tudelft.jenkins.auth.UserImpl;
import nl.tudelft.jenkins.jobs.Job;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobCreationAndRemovalIntegrationTest extends AbstractJenkinsIntegrationTestBase {

	private static final Logger LOG = LoggerFactory.getLogger(JobCreationAndRemovalIntegrationTest.class);

	private static final String JOB_SCM_URL = "git://xyz";

	private static final User USER0 = new UserImpl("person", "person@example.com");
	private static final User USER1 = new UserImpl("other", "other@example.com");

	private static final List<User> USERS = new ArrayList<>();

	static {
		USERS.add(USER0);
		USERS.add(USER1);
	}

	@Test
	public void testCreationAndDeletionOfJob() {

		LOG.debug("Testing creation, then deletion of job ...");

		Job job = createJob(JOB_SCM_URL, USERS);

		job = retrieveJob();

		deleteJob(job);

	}

}
