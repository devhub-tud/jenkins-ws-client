package nl.tudelft.jenkins.client;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import nl.tudelft.jenkins.auth.User;
import nl.tudelft.jenkins.auth.UserImpl;
import nl.tudelft.jenkins.client.exceptions.JenkinsException;
import nl.tudelft.jenkins.client.exceptions.NoJenkinsServerException;
import nl.tudelft.jenkins.client.exceptions.NoSuchJobException;
import nl.tudelft.jenkins.client.exceptions.NoSuchUserException;
import nl.tudelft.jenkins.guice.JenkinsUrl;
import nl.tudelft.jenkins.jobs.Job;
import nl.tudelft.jenkins.jobs.JobImpl;

import nl.tudelft.jenkins.jobs.ScmConfig;
import org.apache.commons.lang.NotImplementedException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class JenkinsClientImpl implements JenkinsClient {

    private static final String JENKINS_VERSION_HEADER_NAME = "X-Jenkins";

    private static final Logger LOG = LoggerFactory.getLogger(JenkinsClientImpl.class);

    private final URL endpoint;
    private final HttpRestClient client;

    @Inject
    JenkinsClientImpl(HttpRestClient client, @JenkinsUrl URL endpoint) {
        LOG.trace("Initializing Jenkins client for endpoint: {}", endpoint.toExternalForm());
        this.endpoint = checkNotNull(endpoint, "endpoint");
        this.client = checkNotNull(client, "client");
    }

    public boolean validateServerOnEndpoint() {
        String url = endpoint.toExternalForm() + "/login";

        LOG.trace("Validating Jenkins server on endpoint: {}", url);
        HttpRestResponse response = client.get(url);

        if (response.isOk() && response.hasHeader(JENKINS_VERSION_HEADER_NAME)) {
            HttpRestResponse.Header header = response.getHeader(JENKINS_VERSION_HEADER_NAME);
            LOG.trace("Jenkins server validated on endpoint: {}", endpoint);
            return true;
        }

        LOG.error("No Jenkins server found on endpoint: {} - response: {}", url, response);
        throw new NoJenkinsServerException(url);
    }

    @Override
    public URL getJenkinsEndpoint() {
        return endpoint;
    }

    @Override
    public Job createJob(final String name, final ScmConfig scmConfig, final List<User> users) {

        LOG.trace("Creating job {0} @ {1} ...", name, scmConfig);

        checkArgument(isNotEmpty(name), "name must be non-empty");

        final Job job = new JobImpl(name);
        job.setScmConfig(scmConfig);

        job.clearNotificationRecipients();
        for (User user : users) {
            job.addPermissionsForUser(user);
            job.addNotificationRecipient(user);
        }

        final String url = endpoint.toExternalForm() + "/createItem?name=" + name;
        final String xml = job.asXml();

        LOG.trace("Creating job ...");
        HttpRestResponse response = client.post(url, "text/xml", xml);

        if (response.isOk()) {
            response.consume();
            return job;
        } else {
            String message = "Error occurred while attempting to create job: " + response.getStatusLine();
            LOG.error(message);
            throw new JenkinsException(message);
        }

    }

    @Override
    public Job retrieveJob(final String name) {

        LOG.trace("Retrieving job {} ...", name);

        checkArgument(isNotEmpty(name), "name must be non-empty");

        final String url = urlForJob(name);

        LOG.trace("Retrieving config.xml ...");
        HttpRestResponse response = client.get(url);

        if (response.isOk()) {
            String xml = response.getContents();
            return JobImpl.fromXml(name, xml);
        } else if (response.isNotFound()) {
            response.consume();
            throw new NoSuchJobException(name);
        } else {
            response.consume();
            String message = "Error while attempting to retrieve job config.xml: " + response.getStatusLine();
            LOG.error(message);
            throw new JenkinsException(message);
        }

    }

    @Override
    public void updateJob(final Job job) {

        LOG.trace("Updating job: {} ...", job);

        checkNotNull(job, "job");

        final String url = urlForJob(job);
        final String contents = job.asXml();

        LOG.trace("Creating job from XML ...");
        HttpRestResponse response = client.post(url, "application/xml", contents);
        response.consume();

        if (!response.isOk()) {
            String message = "Failed to update job config.xml: " + response.getStatusLine();
            LOG.error(message);
            throw new JenkinsException(message);
        }

    }

    @Override
    public void deleteJob(final Job job) {

        LOG.trace("Deleting job {} ...", job);

        final String url = endpoint.toExternalForm() + "/job/" + job.getName() + "/doDelete";

        LOG.trace("Deleting job ...");
        HttpRestResponse response = client.post(url, "text/plain", "");
        response.consume();

        if (!response.isFound()) {
            String message = "Failed to delete job: " + response.getStatusLine();
            LOG.error(message);
            throw new JenkinsException(message);
        }

    }

    private String urlForJob(final String name) {
        return endpoint.toExternalForm() + "/job/" + name + "/config.xml";
    }

    private String urlForJob(Job job) {
        return urlForJob(job.getName());
    }

    @Override
    public User createUser(String userName, String password, String email, String fullName) {
        LOG.trace("Creating user: {} - {} - {}", userName, email, fullName);

        String url = endpoint.toExternalForm() + "/securityRealm/createAccountByAdmin";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", userName));
        params.add(new BasicNameValuePair("password1", password));
        params.add(new BasicNameValuePair("password2", password));
        params.add(new BasicNameValuePair("fullname", fullName));
        params.add(new BasicNameValuePair("email", email));

        HttpRestResponse response = client.postForm(url, params);

        if (response.isFound() || response.isOk() ) {
            return retrieveUser(userName);
        } else {
            LOG.error("Failed to create user: {}", response.getStatusLine());
            throw new JenkinsException("Failed to create user: " + response.getStatusLine());
        }
    }

    @Override
    public User retrieveUser(String userName) throws NoSuchUserException {
        LOG.trace("Retrieving user data: {}", userName);

        String url = endpoint.toExternalForm() + "/user/" + userName + "/api/xml";

        HttpRestResponse response = client.get(url);

        if (response.isOk()) {
            return UserImpl.fromXml(response.getContents());
        } else {
            LOG.warn("Failed to retrieve user: {}", response.getStatusLine());
            throw new JenkinsException("Failed to retrieve user: " + response.getStatusLine());
        }
    }

    @Override
    public User updateUser(User user) throws NoSuchUserException {
        throw new NotImplementedException();
    }

    @Override
    public void deleteUser(User user) throws NoSuchUserException {
        LOG.trace("Deleting user: {}", user);

        String url = endpoint.toExternalForm() + "/user/" + user.getName() + "/doDelete";

        HttpRestResponse response = client.postForm(url, new ArrayList<NameValuePair>());

        if (!response.isFound()) {
            LOG.trace("Failed to delete user: {}", response.getStatusLine());
            throw new JenkinsException("Failed to delete user: " + response.getStatusLine());
        }
    }

    @Override
    public void close() {
        client.close();
    }

}
