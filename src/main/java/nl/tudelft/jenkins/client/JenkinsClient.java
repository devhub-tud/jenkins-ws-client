package nl.tudelft.jenkins.client;

import java.util.List;

import nl.tudelft.jenkins.auth.User;
import nl.tudelft.jenkins.jobs.Job;

import com.google.inject.ImplementedBy;

@ImplementedBy(JenkinsClientImpl.class)
public interface JenkinsClient extends AutoCloseable {

	Job createJob(String name, String scmUrl, List<User> owners);

	Job retrieveJob(String jobName);

	void deleteJob(Job job);

	@Override
	public void close();

}
