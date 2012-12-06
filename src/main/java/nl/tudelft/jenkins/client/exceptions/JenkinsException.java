package nl.tudelft.jenkins.client.exceptions;

/**
 * Top class in the exception class hierarchy.
 */
@SuppressWarnings("serial")
public class JenkinsException extends RuntimeException {

	public JenkinsException(final String message) {
		this(message, null);
	}

	public JenkinsException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
