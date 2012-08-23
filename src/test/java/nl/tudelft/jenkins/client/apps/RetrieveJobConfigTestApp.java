package nl.tudelft.jenkins.client.apps;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetrieveJobConfigTestApp {

	private static final Logger LOG = LoggerFactory.getLogger(RetrieveJobConfigTestApp.class);

	public static void main(final String[] args) throws Exception {

		LOG.info("Starting...");

		final String jobName = "test-job";
		final String url = "http://" + DefaultTestSettings.HOST + ":" + DefaultTestSettings.PORT + "/job/" + jobName + "/config.xml";

		final HttpClient client = new DefaultHttpClient();
		final HttpGet get = new HttpGet(url);

		final HttpResponse response = client.execute(get);
		final StatusLine statusLine = response.getStatusLine();
		if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
			LOG.warn("Failed: {}: {}", statusLine.getStatusCode(), statusLine.getReasonPhrase());
			get.abort();
		} else {

			final HttpEntity entity = response.getEntity();
			final String contents = EntityUtils.toString(entity);

			LOG.info("Contents: {}", contents);

		}

		client.getConnectionManager().shutdown();

		LOG.info("Finished.");

	}

}
