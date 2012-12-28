package nl.tudelft.jenkins.client;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.net.URL;

import nl.tudelft.jenkins.client.exceptions.NoJenkinsServerException;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientServerValidationIntegrationTest {

	private static final Logger LOG = LoggerFactory.getLogger(ClientServerValidationIntegrationTest.class);

	private HttpRestClient restClient;
	private JenkinsClient jenkinsClient;

	@Before
	public void setUp() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext httpContext = new BasicHttpContext();
		HttpMethodFactory methodFactory = new HttpMethodFactory();
		restClient = new HttpRestClientImpl(httpClient, httpContext, methodFactory);
	}

	@After
	public void tearDown() {
		if (jenkinsClient != null) {
			jenkinsClient.close();
		}
		if (restClient != null) {
			restClient.close();
		}
	}

	@Test
	@Ignore("Constructor currently does not validate Jenkins endpoint")
	public void testThatClientRejectsInvalidServer() throws Exception {
		String url = "http://www.google.com";

		LOG.trace("Testing that {} is not a Jenkins server (anonymous check)...", url);

		boolean exceptionWasThrown = false;
		try {
			jenkinsClient = new JenkinsClientImpl(restClient, new URL(url));
		} catch (NoJenkinsServerException e) {
			exceptionWasThrown = true;
		}
		assertThat(exceptionWasThrown, is(true));
	}

	@Test
	public void testThatClientAcceptsValidJenkinsServer() throws Exception {
		String url = "http://devhub.nl/jenkins";

		LOG.trace("Testing that {} is a valid Jenkins server (anonymous check)...", url);

		jenkinsClient = new JenkinsClientImpl(restClient, new URL(url));
	}

}
