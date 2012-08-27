package nl.tudelft.jenkins.tests.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import nl.tudelft.jenkins.auth.User;
import nl.tudelft.jenkins.client.JenkinsClient;
import nl.tudelft.jenkins.client.exceptions.NoSuchJobException;
import nl.tudelft.jenkins.guice.JenkinsWsClientGuiceModule;
import nl.tudelft.jenkins.jobs.Job;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public abstract class AbstractJenkinsIntegrationTestBase {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractJenkinsIntegrationTestBase.class);

	private Injector injector;

	private JenkinsClient client;

	@Before
	public void setUp() {

		injector = Guice.createInjector(new JenkinsWsClientGuiceModule());

		client = injector.getInstance(JenkinsClient.class);

	}

	@After
	public void tearDown() {

		LOG.trace("Cleaning up job ...");

		try
		{
			final Job job = retrieveJob();
			deleteJob(job);
		} catch (final NoSuchJobException e) {
			LOG.trace("Job was already cleaned up");
		}

		client.close();

	}

	public final String getJobName() {
		return "test-job-" + this.getClass().getCanonicalName();
	}

	protected final Job createJob(final String scmUrl, final List<User> users) {

		LOG.trace("Creating job with name: {}, scmUrl: {} ...", getJobName(), scmUrl);

		final Job job = client.createJob(getJobName(), scmUrl, users);

		assertThatJobNameIsCorrectForCurrentTest(job);

		return job;

	}

	protected final Job retrieveJob() {

		LOG.trace("Retrieving job with name: {} ...", getJobName());

		return client.retrieveJob(getJobName());

	}

	protected final void deleteJob(final Job job) {

		LOG.trace("Deleting job with name: {} ...", getJobName());

		assertThatJobNameIsCorrectForCurrentTest(job);

		client.deleteJob(job);

	}

	private void assertThatJobNameIsCorrectForCurrentTest(final Job job) {
		assertThat("Job name must be set to a test-specific value", job.getName(), is(equalTo(getJobName())));
	}

}
