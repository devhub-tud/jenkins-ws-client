package nl.tudelft.jenkins.client.exceptions;

/**
 * A {@link NoSuchJobException} is thrown when the user tries to retrieve a job
 * which does not exist.
 */
@SuppressWarnings("serial")
public class NoSuchJobException extends JenkinsException {

	public NoSuchJobException(final String jobName) {
		super("No such job: " + jobName);
	}

}
