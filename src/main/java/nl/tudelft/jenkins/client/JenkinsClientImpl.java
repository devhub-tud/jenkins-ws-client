package nl.tudelft.jenkins.client;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import javax.inject.Inject;

import nl.tudelft.jenkins.jobs.Job;
import nl.tudelft.jenkins.jobs.JobImpl;

import org.jclouds.jenkins.v1.JenkinsApi;
import org.jclouds.jenkins.v1.JenkinsAsyncApi;
import org.jclouds.jenkins.v1.features.JobApi;
import org.jclouds.rest.RestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JenkinsClientImpl implements JenkinsClient {

	private static final Logger LOG = LoggerFactory.getLogger(JenkinsClientImpl.class);

	private final RestContext<JenkinsApi, JenkinsAsyncApi> restContext;

	private final JobApi jobApi;

	@Inject
	public JenkinsClientImpl(final RestContext<JenkinsApi, JenkinsAsyncApi> restContext) {

		LOG.trace("Initializing Jenkins client...");

		this.restContext = checkNotNull(restContext, "restContext must be non-null");
		jobApi = restContext.getApi().getJobApi();

	}

	@Override
	public Job createJob(final String name, final String scmUrl) {

		LOG.trace("Creating job {} @ {} ...", name, scmUrl);

		checkArgument(isNotEmpty(name), "name must be non-empty");
		checkArgument(isNotEmpty(scmUrl), "scmUrl must be non-empty");

		final Job job = new JobImpl(name);
		job.setScmUrl(scmUrl);

		final String xml = job.asXml();

		jobApi.createFromXML(name, xml);

		return job;

	}

	@Override
	public Job retrieveJob(final String name) {

		LOG.trace("Retrieving job {} ...", name);

		final String xml = jobApi.fetchConfigXML(name);
		final Job job = JobImpl.fromXml(name, xml);

		return job;

	}

	@Override
	public void deleteJob(final Job job) {

		LOG.trace("Deleting job {} ...", job);

		jobApi.delete(job.getName());

	}

	@Override
	public void close() {
		restContext.close();
	}

}
