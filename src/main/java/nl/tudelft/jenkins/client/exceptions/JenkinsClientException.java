package nl.tudelft.jenkins.client.exceptions;

@SuppressWarnings("serial")
public class JenkinsClientException extends RuntimeException {

	protected JenkinsClientException(final String message) {
		this(message, null);
	}

	protected JenkinsClientException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
