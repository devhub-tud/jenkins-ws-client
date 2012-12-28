package nl.tudelft.jenkins.tests.integration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;

public class BasicHttpClientJenkinsServerTest extends AbstractJenkinsIntegrationTestBase {

	@Override
	@Before
	public void setUp() {

	}

	@Override
	@After
	public void tearDown() {

	}

	public void testThatJenkinsServiceOnLocalhostCanBeReached() throws Exception {

		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet("http://localhost:8080/jenkins/login");

		HttpResponse response = client.execute(get);

		assertThat(response.getStatusLine().getStatusCode(), is(HttpStatus.SC_OK));

	}

}
