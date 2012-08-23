package nl.tudelft.jenkins.client.apps;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateDeleteJobTestApp {

	private static final Logger LOG = LoggerFactory.getLogger(CreateDeleteJobTestApp.class);

	public static void main(final String[] args) throws Exception {

		LOG.info("Starting...");

		final HttpClient client = new DefaultHttpClient();

		final String existingJob = "test-job";
		final String getUrl = "http://" + DefaultTestSettings.HOST + "/job/" + existingJob + "/config.xml";
		final HttpGet get = new HttpGet(getUrl);

		LOG.info("Retrieving existing job config from: {}", getUrl);
		final HttpResponse getResponse = client.execute(get);

		final StatusLine getStatusLine = getResponse.getStatusLine();
		if (getStatusLine.getStatusCode() != HttpStatus.SC_OK) {
			LOG.warn("Got status: {}: {}", getStatusLine.getStatusCode(), getStatusLine.getReasonPhrase());
			get.abort();
		} else {

			final HttpEntity getEntity = getResponse.getEntity();
			final String getContents = EntityUtils.toString(getEntity);

			LOG.info("Got contents: {}", getContents);

			final String newJobName = "x";

			final String createUrl = "http://" + DefaultTestSettings.HOST +
					"/createItem?name=" + newJobName;
			final HttpPost post = new HttpPost(createUrl);
			// final String config =
			// IOUtils.readResource("/nl/tudelft/jenkins/job/cfg/tests/standard-job.xml");
			final String config = getContents;
			final HttpEntity entity = new StringEntity(config, ContentType.TEXT_XML);
			post.setEntity(entity);

			// LOG.info("POSTing new job config to: {}", createUrl);
			final HttpResponse response = client.execute(post);

			// final String copyUrl = "http://" + DefaultTestSettings.HOST +
			// "/createItem?name=" + newJobName + "&mode=copy&from=test-job";
			// final HttpPost post = new HttpPost(copyUrl);
			// post.setEntity(new StringEntity(" "));

			// LOG.info("POSTing copy job command: {}", copyUrl);
			// final HttpResponse response = client.execute(copy);

			final StatusLine postStatusLine = response.getStatusLine();
			if (postStatusLine.getStatusCode() != HttpStatus.SC_OK) {
				LOG.warn("Got status: {}: {}", postStatusLine.getStatusCode(), postStatusLine.getReasonPhrase());
				post.abort();
			} else {

				final HttpEntity responseEntity = response.getEntity();
				final String responseContents = EntityUtils.toString(responseEntity);

				LOG.info("Response: {}", responseContents);

			}

		}

		client.getConnectionManager().shutdown();

		LOG.info("Finished.");

		// Create: POST config.xml to:
		// http://dea.hartveld.com/createItem?name=JOBNAME
		// Delete: POST to: http://dea.hartveld.com/job/test-job/doDelete

		// Retrieve config.xml: GET:
		// http://dea.hartveld.com/job/test-job/config.xml
		// POST to the same URL to update it.

		// Set description: POST to:
		// http://dea.hartveld.com/job/test-job/description
		// POST form data with a "description" parameter to set the description.

		// Build: POST to: http://dea.hartveld.com/job/test-job/build

	}

}