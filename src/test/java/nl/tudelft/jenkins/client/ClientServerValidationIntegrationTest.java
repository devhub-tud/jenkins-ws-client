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
import org.junit.Test;

public class ClientServerValidationIntegrationTest {

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
	}

	@Test
	public void testThatClientRejectsInvalidServer() throws Exception {
		boolean exceptionWasThrown = false;
		try {
			jenkinsClient = new JenkinsClientImpl(restClient, new URL("http://www.google.com/"));
		} catch (NoJenkinsServerException e) {
			exceptionWasThrown = true;
		}
		assertThat(exceptionWasThrown, is(true));
	}

	@Test
	public void testThatClientAcceptsValidJenkinsServer() throws Exception {
		jenkinsClient = new JenkinsClientImpl(restClient, new URL("http://devhub.nl/jenkins"));
	}

}
