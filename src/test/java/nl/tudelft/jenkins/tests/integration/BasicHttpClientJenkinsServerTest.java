package nl.tudelft.jenkins.tests.integration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.net.URL;

import nl.tudelft.jenkins.client.JenkinsClient;
import nl.tudelft.jenkins.client.JenkinsClientFactory;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Ignore
public class BasicHttpClientJenkinsServerTest extends AbstractJenkinsIntegrationTestBase {

	private static final Logger LOG = LoggerFactory.getLogger(BasicHttpClientJenkinsServerTest.class);

	private static final URL JENKINS_URL = getJenkinsURL();
	private static final String JENKINS_USER = getUserName();
	private static final String JENKINS_PASS = getPassword();

	@Override
	@Before
	public void setUp() {
		// Ignore creation of injector, etc. in superclass.
	}

	@Override
	@After
	public void tearDown() {
		// Ignore superclass.
	}

	@Test
	public void testThatJenkinsServiceOnConfiguredHostCanBeReached() throws Exception {

		String url = JENKINS_URL + "/login";
		LOG.info("Checking URL: {}", url);

		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);

		HttpResponse response = client.execute(get);

		assertThat("Status is not OK", response.getStatusLine().getStatusCode(), is(HttpStatus.SC_OK));

		Header[] headers = response.getHeaders("X-Jenkins");
		String version = "";
		if (headers.length == 1) {
			version = headers[0].getValue();
		} else {
			for (Header header : headers) {
				version += header.getValue();
			}
		}

		EntityUtils.consumeQuietly(response.getEntity());
		client.getConnectionManager().shutdown();

		if (version.isEmpty()) {
			fail("No X-Jenkins header in response.");
		} else {
			LOG.info("Version: {}", version);
		}

	}

	@Test
	public void testThatJenkinsClientAcceptsJenkinsServiceOnConfiguredHost() throws Exception {

		LOG.info("Testing that JenkinsClientFactory creates JenkinsClient for URL: {}", JENKINS_URL);
		JenkinsClientFactory factory = new JenkinsClientFactory(JENKINS_URL, JENKINS_USER, JENKINS_PASS);
		JenkinsClient client = factory.getJenkinsClient();

		LOG.info("Tested succesfully. Closing client...");
		client.close();

	}

}
