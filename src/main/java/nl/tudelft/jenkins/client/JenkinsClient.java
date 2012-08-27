package nl.tudelft.jenkins.client;

import java.util.List;

import nl.tudelft.jenkins.auth.User;
import nl.tudelft.jenkins.jobs.Job;

import com.google.inject.ImplementedBy;

/**
 * Jenkins Web Service API Client
 */
@ImplementedBy(JenkinsClientImpl.class)
public interface JenkinsClient extends AutoCloseable {

	/**
	 * Create a new build job with the given parameters.
	 * 
	 * @param name Job name.
	 * @param scmUrl SCM URL.
	 * @param owners {@link List} of authorized users for the new build job.
	 * 
	 * @return A {@link Job} representing the newly created build job.
	 */
	Job createJob(String name, String scmUrl, List<User> owners);

	/**
	 * Retrieve the configuration of an existing build job.
	 * 
	 * @param jobName The name of the job.
	 * 
	 * @return A {@link Job} representing the named job.
	 */
	Job retrieveJob(String jobName);

	/**
	 * Delete an existing build job.
	 * 
	 * @param job The {@link Job} instance representing the job.
	 */
	void deleteJob(Job job);

	/**
	 * Release all acquired resources.
	 * 
	 * Make sure the {@link JenkinsClient} instance is not reused after calling
	 * this method.
	 */
	@Override
	public void close();

}
