package nl.tudelft.jenkins.tests.integration;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import nl.tudelft.jenkins.auth.User;
import nl.tudelft.jenkins.client.JenkinsClient;
import nl.tudelft.jenkins.client.exceptions.NoSuchJobException;
import nl.tudelft.jenkins.guice.JenkinsWsClientGuiceModule;
import nl.tudelft.jenkins.jobs.Job;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public abstract class AbstractJenkinsIntegrationTestBase {

	private static final String JENKINS_HOST = "192.168.56.101";
	private static final int JENKINS_PORT = 8080;
	private static final String JENKINS_CONTEXT = "/jenkins";
	private static final String JENKINS_USER = "david";
	private static final String JENKINS_PASS = "x";

	private static final Logger LOG = LoggerFactory.getLogger(AbstractJenkinsIntegrationTestBase.class);

	protected static final String JOB_SCM_URL = "git://github.com/dlhartveld/mini-project.git";

	private Injector injector;

	private JenkinsClient client;

	@Before
	public void setUp() throws Exception {

		injector = Guice.createInjector(new JenkinsWsClientGuiceModule(JENKINS_HOST, JENKINS_PORT, JENKINS_CONTEXT, JENKINS_USER, JENKINS_PASS));
		client = injector.getInstance(JenkinsClient.class);

	}

	@After
	public void tearDown() throws Exception {

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

	protected void updateJob(Job job) {

		LOG.trace("Updating job with name: {} ...", getJobName());

		checkNotNull(job, "job");
		checkArgument(StringUtils.equals(job.getName(), getJobName()), "Job name not equal to test case job name");

		client.updateJob(job);

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
