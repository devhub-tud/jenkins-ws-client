package nl.tudelft.jenkins.client;

import nl.tudelft.jenkins.Job;
import nl.tudelft.jenkins.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JenkinsClientImpl implements JenkinsClient {

	private static final Logger LOG = LoggerFactory.getLogger(JenkinsClientImpl.class);

	public JenkinsClientImpl() {

		LOG.trace("Initializing Jenkins client...");

	}

	@Override
	public Job createJob(final User owner, final String name) {

		LOG.trace("Create job {} for user {} ...", name, owner);

		throw new RuntimeException("Method not implemented");

	}

	@Override
	public Job retrieveJob(final String name) {

		LOG.trace("Retrieving job with name {} ...", name);

		throw new RuntimeException("Method not implemented");

	}

	@Override
	public Job allowJobAccessFor(final Job job, final User user) {

		LOG.trace("Allowing access to job {} for user {} ...", job, user);

		throw new RuntimeException("Method not implemented");

	}

	@Override
	public void deleteJob(final Job job) {

		LOG.trace("Deleting job {} ...", job);

		throw new RuntimeException("Method not implemented");

	}

}
