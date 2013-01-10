package nl.tudelft.jenkins.client;

public interface HttpRestResponse {

	int getStatusCode();

	boolean isOk();

	boolean isFound();

	boolean isNotFound();

	String getStatusLine();

	boolean hasHeader(String name);

	Header getHeader(String name);

	String getContents();

	void consume();

	interface Header {

		String getName();

		String getValue();

	}

}
