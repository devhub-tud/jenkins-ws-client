package nl.tudelft.jenkins.client;

@SuppressWarnings("serial")
public class HttpRestClientException extends RuntimeException {

	private final int errorcode;

	public HttpRestClientException(String message, Throwable cause) {
		super(message, cause);
		errorcode = -1;
	}

	public HttpRestClientException(String message, Throwable cause, int errorcode) {
		super(message, cause);
		this.errorcode = errorcode;
	}

	public int getErrorcode() {
		return errorcode;
	}

}
