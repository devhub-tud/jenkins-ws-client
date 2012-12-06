package nl.tudelft.jenkins.client.exceptions;

import nl.tudelft.jenkins.client.HttpRestResponse;

import org.apache.http.HttpResponse;

@SuppressWarnings("serial")
public class JenkinsRestException extends JenkinsException {

	public JenkinsRestException(HttpRestResponse response) {
		super("Server returned non-OK response: " + response.getStatusLine());
	}

	public JenkinsRestException(HttpResponse response) {
		super("Server returned non-OK response: " + response.getStatusLine());
	}

}
