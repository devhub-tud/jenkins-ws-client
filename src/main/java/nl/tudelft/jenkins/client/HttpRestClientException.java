package nl.tudelft.jenkins.client;

@SuppressWarnings("serial")
public class HttpRestClientException extends RuntimeException {

	private final int statusCode;

	public HttpRestClientException(String message, Throwable cause) {
		super(message, cause);
		statusCode = -1;
	}

	public HttpRestClientException(String message, Throwable cause, int statusCode) {
		super(message, cause);
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

}
