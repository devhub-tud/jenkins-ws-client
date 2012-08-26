package nl.tudelft.jenkins.tests.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import nl.tudelft.jenkins.client.JenkinsClient;
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

		final Job job = retrieveJob();
		deleteJob(job);

		client.close();

	}

	public final String getJobName() {
		return "job-" + this.getClass().getCanonicalName();
	}

	protected final Job createJob(final String scmUrl) {

		LOG.trace("Creating job with name: {}, scmUrl: {} ...", getJobName(), scmUrl);

		final Job job = client.createJob(getJobName(), scmUrl);

		assertThatJobNameIsCorrectForCurrentTest(job);

		return job;

	}

	protected final Job retrieveJob() {
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
