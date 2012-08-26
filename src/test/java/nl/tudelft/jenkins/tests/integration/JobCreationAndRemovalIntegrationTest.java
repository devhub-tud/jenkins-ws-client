package nl.tudelft.jenkins.tests.integration;

import nl.tudelft.jenkins.client.JenkinsClient;
import nl.tudelft.jenkins.guice.JenkinsWsClientGuiceModule;
import nl.tudelft.jenkins.jobs.Job;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class JobCreationAndRemovalIntegrationTest {

	private static final Logger LOG = LoggerFactory.getLogger(JobCreationAndRemovalIntegrationTest.class);

	private static final String JOB_NAME = "X";
	private static final String JOB_SCM_URL = "git://xyz";

	@Test
	public void testCreationAndDeletionOfJob() {

		LOG.trace("Setting up Guice context...");
		final Injector injector = Guice.createInjector(new JenkinsWsClientGuiceModule());

		try (JenkinsClient client = injector.getInstance(JenkinsClient.class))
		{
			LOG.trace("Creating job {} ...", JOB_NAME);
			Job job = client.createJob(JOB_NAME, JOB_SCM_URL);
			job = null;

			try {
				Thread.sleep(10000);
			} catch (final InterruptedException e) {
				LOG.warn("Caught exception", e);
			}

			LOG.trace("Retrieving job {} ...", JOB_NAME);
			job = client.retrieveJob(JOB_NAME);
			LOG.trace("Found job {}");

			LOG.trace("Deleting job {} ...", JOB_NAME);
			client.deleteJob(job);

		}

	}

}
