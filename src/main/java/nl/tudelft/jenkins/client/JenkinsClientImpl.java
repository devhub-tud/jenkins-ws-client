package nl.tudelft.jenkins.client;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import nl.tudelft.jenkins.auth.User;
import nl.tudelft.jenkins.client.exceptions.NoSuchJobException;
import nl.tudelft.jenkins.jobs.Job;
import nl.tudelft.jenkins.jobs.JobImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class JenkinsClientImpl implements JenkinsClient {

	private static final Logger LOG = LoggerFactory.getLogger(JenkinsClientImpl.class);

	private final String endpoint;
	private final HttpRestClient client;

	@Inject
	JenkinsClientImpl(HttpRestClient client, @Named("JenkinsEndpoint") String endpoint) {

		LOG.trace("Initializing Jenkins client ...");

		checkArgument(isNotEmpty(endpoint), "endpoint must be non-empty");

		this.endpoint = endpoint;
		this.client = checkNotNull(client, "client");

	}

	@Override
	public Job createJob(final String name, final String scmUrl, final List<User> users) {

		LOG.trace("Creating job {} @ {} ...", name, scmUrl);

		checkArgument(isNotEmpty(name), "name must be non-empty");
		checkArgument(isNotEmpty(scmUrl), "scmUrl must be non-empty");

		final Job job = new JobImpl(name);
		job.setScmUrl(scmUrl);

		final String url = endpoint + "createItem?name=" + name;
		final String xml = job.asXml();

		LOG.trace("Creating job ...");
		HttpRestResponse response = client.post(url, "application/xml", xml);

		if (response.isOk()) {
			response.consume();
			return job;
		} else {
			String message = "Error occurred while attempting to create job: " + response.getStatusLine();
			LOG.error(message);
			throw new RuntimeException(message);
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
			throw new RuntimeException(message);
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
			throw new RuntimeException(message);
		}

	}

	@Override
	public void deleteJob(final Job job) {

		LOG.trace("Deleting job {} ...", job);

		final String url = endpoint + "job/" + job.getName() + "/doDelete";

		LOG.trace("Deleting job ...");
		HttpRestResponse response = client.post(url, "text/plain", "");
		response.consume();

		if (!response.isFound()) {
			String message = "Failed to delete job: " + response.getStatusLine();
			LOG.error(message);
			throw new RuntimeException(message);
		}

	}

	@Override
	public void close() {
		client.close();
	}

	private String urlForJob(final String name) {
		return endpoint + "job/" + name + "/config.xml";
	}

	private String urlForJob(Job job) {
		return urlForJob(job.getName());
	}

}
