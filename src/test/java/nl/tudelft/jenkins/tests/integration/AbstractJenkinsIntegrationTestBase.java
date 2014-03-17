package nl.tudelft.jenkins.tests.integration;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import nl.tudelft.jenkins.auth.User;
import nl.tudelft.jenkins.jobs.GitScmConfig;
import nl.tudelft.jenkins.client.JenkinsClient;
import nl.tudelft.jenkins.client.exceptions.NoSuchJobException;
import nl.tudelft.jenkins.guice.JenkinsWsClientGuiceModule;
import nl.tudelft.jenkins.jobs.Job;

import nl.tudelft.jenkins.jobs.SVNScmConfig;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.inject.Guice;
import com.google.inject.Injector;

public abstract class AbstractJenkinsIntegrationTestBase {

    private static URL defaultJenkinsUrl;
    private static String defaultJenkinsUser;
    private static String defaultJenkinsPass;

    private static final Logger LOG = LoggerFactory.getLogger(AbstractJenkinsIntegrationTestBase.class);

    protected static final String JOB_SCM_URL = "git://github.com/dlhartveld/mini-project.git";

    private Injector injector;

    protected JenkinsClient client;

    @BeforeClass
    public static void loadResources() throws Exception {
        defaultJenkinsUrl = new URL(jenkinsUrl());
        defaultJenkinsUser = jenkinsUser();
        defaultJenkinsPass = jenkinsPassword();
    }

    protected static final URL getJenkinsURL() {
        return defaultJenkinsUrl;
    }

    protected static final String getUserName() {
        return defaultJenkinsUser;
    }

    protected static final String getPassword() {
        return defaultJenkinsPass;
    }

    @Before
    public void setUp() throws Exception {
        LOG.debug("Creating Guice injector for URL: {} == {}", defaultJenkinsUrl, defaultJenkinsUrl.toExternalForm());

        injector = Guice.createInjector(new JenkinsWsClientGuiceModule(defaultJenkinsUrl, defaultJenkinsUser, defaultJenkinsPass));

        client = injector.getInstance(JenkinsClient.class);

    }

    //@After
    public void tearDown() throws Exception {

        LOG.trace("Cleaning up job ...");

        try {
            final Job job = retrieveJob();
            deleteJob(job);
        } catch (final NoSuchJobException e) {
            LOG.trace("Job was already cleaned up");
        }

        client.close();

    }

    public final String getJobName() {
        return "这是中文的名的11";
    }

    protected final Job createJob(final String scmUrl, final List<User> users) {
        return createJob(getJobName(),  scmUrl, users);
    }

    protected final Job createJob(final String jobName ,final String scmUrl, final List<User> users) {

        LOG.trace("Creating job with name: {}, scmUrl: {} ...",jobName, scmUrl);



        SVNScmConfig scmConfig = new SVNScmConfig("http://local/project");
        //GitScmConfig scmConfig = new GitScmConfig("http://git");
        final Job job = client.createJob(jobName, scmConfig, users);

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

    protected User createUser(String userName, String password, String email, String fullName) {

        LOG.trace("Creating user: {} - {} - {}", userName, email, fullName);

        return client.createUser(userName, password, email, fullName);

    }

    protected void deleteUser(User user) {
        LOG.trace("Deleting user: {}", user);

        client.deleteUser(user);
    }

    protected User retrieveUser(String userName) {
        LOG.trace("Retrieving user: {}", userName);

        return client.retrieveUser(userName);
    }

    private static String jenkinsUser() {
        return readResource("/test.username").trim();
    }

    private static String jenkinsPassword() {
        return readResource("/test.password").trim();
    }

    private static String jenkinsUrl() {
        return readResource("/test.jenkinsUrl").trim();
    }

    private static String readResource(String resource) {
        try {
            URI file = AbstractJenkinsIntegrationTestBase.class.getResource(resource).toURI();
            return Files.toString(new File(file), Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read resource: " + resource, e);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to read resource: " + resource, e);
        }
    }

}
