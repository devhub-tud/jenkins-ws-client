package nl.tudelft.jenkins.client.exceptions;

@SuppressWarnings("serial")
public class NoSuchJobException extends JenkinsClientException {

	public NoSuchJobException(final String jobName) {
		super("No such job: " + jobName);
	}

}
