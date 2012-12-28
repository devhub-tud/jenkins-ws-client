package nl.tudelft.jenkins.tests.integration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicHttpClientJenkinsServerTest extends AbstractJenkinsIntegrationTestBase {

	private static final Logger LOG = LoggerFactory.getLogger(BasicHttpClientJenkinsServerTest.class);

	@Override
	@Before
	public void setUp() {

	}

	@Override
	@After
	public void tearDown() {

	}

	@Test
	public void testThatJenkinsServiceOnLocalhostCanBeReached() throws Exception {

		String url = "http://localhost:8080/jenkins/login";

		LOG.info("Checking URL: {}", url);

		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);

		HttpResponse response = client.execute(get);

		assertThat(response.getStatusLine().getStatusCode(), is(HttpStatus.SC_OK));

		Header[] headers = response.getHeaders("X-Jenkins");
		String version = "";
		if (headers.length == 0) {
			Assert.fail("No X-Jenkins header in response.");
		} else if (headers.length == 1) {
			version = headers[0].getValue();
		} else {
			for (Header header : headers) {
				version += header.getValue();
			}
		}

		LOG.info("Version: {}", version);

	}

}
