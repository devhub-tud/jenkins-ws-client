package nl.tudelft.jenkins.client;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class HttpRestResponseImpl implements HttpRestResponse {

	private static final Logger LOG = LoggerFactory.getLogger(HttpRestResponseImpl.class);

	private final HttpResponse response;
	private final String contents;

	HttpRestResponseImpl(HttpResponse response) {
		this.response = checkNotNull(response);

		LOG.trace("Creating response object. Status: {}", response.getStatusLine());

		contents = contentsOf(response.getEntity());
	}

	@Override
	public int getStatusCode() {
		return response.getStatusLine().getStatusCode();
	}

	@Override
	public boolean isOk() {
		return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
	}

	@Override
	public boolean isFound() {
		return response.getStatusLine().getStatusCode() == 302;
	}

	@Override
	public boolean isNotFound() {
		return response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND;
	}

	@Override
	public String getStatusLine() {
		return response.getStatusLine().toString();
	}

	@Override
	public boolean hasHeader(String name) {
		checkArgument(isNotEmpty(name), "name must be non-empty");

		return response.getHeaders(name).length > 0;
	}

	@Override
	public Header getHeader(String name) {
		checkArgument(isNotEmpty(name), "name must be non-empty");

		org.apache.http.Header[] headers = response.getHeaders(name);
		StringBuilder value = new StringBuilder();
		for (org.apache.http.Header header : headers) {
			value.append(header.getValue());
			value.append(",");
		}
		value.deleteCharAt(value.length() - 1);

		return new HttpRestResponseHeaderImpl(name, value.toString());
	}

	@Override
	public String getContents() {
		return contents;
	}

	@Override
	public void consume() {
		EntityUtils.consumeQuietly(response.getEntity());
	}

	private String contentsOf(HttpEntity entity) {
		String contents;

		try {
			contents = EntityUtils.toString(entity);
		} catch (ParseException e) {
			String message = "Header elements of POST response could not be parsed";
			LOG.error(message, e);
			throw new HttpRestClientException(message, e);
		} catch (IOException e) {
			String message = "Error occurred while reading network stream";
			LOG.error(message, e);
			throw new HttpRestClientException(message, e);
		} finally {
			EntityUtils.consumeQuietly(entity);
		}

		return contents;
	}

	private class HttpRestResponseHeaderImpl implements Header {

		private String name;
		private String value;

		private HttpRestResponseHeaderImpl(String name, String value) {
			this.name = name;
			this.value = value;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getValue() {
			return value;
		}

	}

}
