package nl.tudelft.jenkins.client;

public interface HttpRestResponse {

	int getStatusCode();

	boolean isOk();

	boolean isFound();

	boolean isNotFound();

	String getStatusLine();

	String getContents();

	void consume();

}
