package nl.tudelft.jenkins.client;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

class HttpMethodFactory {

	private static final Logger LOG = LoggerFactory.getLogger(HttpMethodFactory.class);

	@Inject
	HttpMethodFactory() {}

	HttpPost createPost(String url, String contentType, String contents) {
		LOG.trace("Creating POST: {} - {}", url, contentType);

		StringEntity entity = createStringEntityForContents(contentType, contents);

		HttpPost post = new HttpPost(url);
		post.setEntity(entity);

		return post;
	}

	HttpGet createGet(String url) {
		LOG.trace("Creating GET: {}", url);

		return new HttpGet(url);
	}

	HttpPut createPut(String url, String contentType, String contents) {
		LOG.trace("Creating PUT: {} - {}", url, contentType);

		StringEntity entity = createStringEntityForContents(contentType, contents);

		HttpPut put = new HttpPut(url);
		put.setEntity(entity);

		return put;
	}

	HttpDelete createDelete(String url) {
		LOG.trace("Creating DELETE: {}", url);

		return new HttpDelete(url);
	}

	private StringEntity createStringEntityForContents(String contentType, String contents) {
		StringEntity entity;
		try {
			entity = new StringEntity(contents, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOG.error("Unsupported encoding in contents", e);
			throw new HttpMethodFactoryException("Unsupported encoding in contents", e);
		}

		entity.setContentType(contentType);

		return entity;
	}

	@SuppressWarnings("serial")
	private static class HttpMethodFactoryException extends RuntimeException {

		public HttpMethodFactoryException(String message, Throwable cause) {
			super(message, cause);
		}

	}

}
