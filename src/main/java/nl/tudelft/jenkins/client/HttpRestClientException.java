package nl.tudelft.jenkins.client;

@SuppressWarnings("serial")
class HttpRestClientException extends RuntimeException {

	public HttpRestClientException(String message, Throwable cause) {
		super(message, cause);
	}

}
