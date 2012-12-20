package nl.tudelft.jenkins.client;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

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

	@Before
	public void setUp() {
		client = new JenkinsClientImpl(restClient, ENDPOINT);
	}

	@Test
	public void testThatJenkinsClientSetsServerURLCorrectly() throws Exception {
		assertThat(client.getJenkinsEndpoint(), is(equalTo(ENDPOINT)));
	}

}
