package nl.tudelft.jenkins.client;

import com.google.inject.ImplementedBy;

@ImplementedBy(HttpRestClientImpl.class)
public interface HttpRestClient extends AutoCloseable {

	HttpRestResponse post(String url, String contentType, String contents);

	HttpRestResponse get(String url);

	HttpRestResponse put(String url, String contentType, String contents);

	HttpRestResponse delete(String url);

	@Override
	void close();

}
