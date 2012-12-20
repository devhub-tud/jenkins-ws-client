package nl.tudelft.jenkins.client;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import nl.tudelft.jenkins.client.HttpRestResponse.Header;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JenkinsClientImplTest {

	private static final String ENDPOINT = "http://www.example.com/jenkins";

	private JenkinsClientImpl client;

	@Mock private HttpRestClient restClient;
	@Mock private HttpRestResponse response;
	@Mock private Header header;

	@Before
	public void setUp() {
		when(restClient.get(ENDPOINT + "/login")).thenReturn(response);
		when(response.isOk()).thenReturn(true);
		when(response.hasHeader("X-Jenkins")).thenReturn(true);
		when(response.getHeader("X-Jenkins")).thenReturn(header);
		when(header.getValue()).thenReturn(JenkinsVersion.SUPPORTED_JENKINS_VERSION);

		client = new JenkinsClientImpl(restClient, ENDPOINT);
	}

	@Test
	public void testThatJenkinsClientSetsServerURLCorrectly() throws Exception {
		assertThat(client.getJenkinsEndpoint(), is(equalTo(ENDPOINT)));
	}

}
