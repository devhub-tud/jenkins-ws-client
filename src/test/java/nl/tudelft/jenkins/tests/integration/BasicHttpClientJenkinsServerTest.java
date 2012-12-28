package nl.tudelft.jenkins.tests.integration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.net.URL;

import nl.tudelft.jenkins.client.JenkinsClientFactory;

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
	private static final String JENKINS_URL = "http://devhub.nl/jenkins";

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

		LOG.info("Checking URL: {}", JENKINS_URL + "/login");

		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(JENKINS_URL);

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

	@Test
	public void testThatJenkinsClientAcceptsJenkinsServiceOnLocalhost() throws Exception {

		JenkinsClientFactory factory = new JenkinsClientFactory(new URL(JENKINS_URL), "test", "x");
		factory.getJenkinsClient();

	}

}
