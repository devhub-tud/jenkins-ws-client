package nl.tudelft.jenkins.client;

import java.util.List;

import org.apache.http.NameValuePair;

import com.google.inject.ImplementedBy;

@ImplementedBy(HttpRestClientImpl.class)
public interface HttpRestClient {

	HttpRestResponse put(String url, String contentType, String contents);

	HttpRestResponse get(String url);

	HttpRestResponse post(String url, String contentType, String contents);

	HttpRestResponse postForm(String url, List<NameValuePair> params);

	HttpRestResponse delete(String url);

	void close();

}
