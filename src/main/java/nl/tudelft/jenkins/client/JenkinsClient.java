package nl.tudelft.jenkins.client;

import nl.tudelft.jenkins.Job;
import nl.tudelft.jenkins.User;

public interface JenkinsClient {

	Job createJob(User owner, String name);

	Job retrieveJob(String name);

	Job allowJobAccessFor(Job job, User user);

	void deleteJob(Job job);

}
