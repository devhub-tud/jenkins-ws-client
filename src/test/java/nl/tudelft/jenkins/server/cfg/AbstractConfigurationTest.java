package nl.tudelft.jenkins.server.cfg;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class AbstractConfigurationTest {

	protected abstract String getInputFileName();

	private JsonObject input;

	@Before
	public void setUp() throws Exception {

		final InputStream in = this.getClass().getResourceAsStream(getInputFileName());

		final InputStreamReader isr = new InputStreamReader(in);
		final BufferedReader br = new BufferedReader(isr);

		final JsonParser parser = new JsonParser();
		final JsonElement element = parser.parse(br);

		if (!element.isJsonObject()) {
			throw new RuntimeException("File " + getInputFileName() + " does not contain a JSON object");
		}

		input = element.getAsJsonObject();

	}

	@Test
	public void testAcceptance() throws Exception {
		new JenkinsConfiguration(input);
	}

}
