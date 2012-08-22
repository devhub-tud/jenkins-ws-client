package nl.tudelft.jenkins.client.apps;

import java.io.IOException;

import nl.tudelft.jenkins.server.cfg.JenkinsConfiguration;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.AuthCache;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TestApp {

	public static final Logger LOG = LoggerFactory.getLogger(TestApp.class);

	private static final String HOSTNAME = "dea.hartveld.com";
	private static final int PORT = 80;
	private static final String CONTEXTROOT = "/";

	private static final String URL = "http://" + HOSTNAME + ":" + PORT + CONTEXTROOT;

	public static void main(final String[] args) throws Exception {

		final HttpHost host = new HttpHost(HOSTNAME, PORT);

		final HttpContext context = createBasicAuthenticationContext(host);
		final DefaultHttpClient client = createHttpClient(host);

		final String url = URL + "api/json";

		// final String url = URL + "user/u0/" + "api/json";
		// final String url = URL + "job/" + URLEncoder.encode(JOB, "UTF-8") +
		// "/api/json?pretty=true";

		LOG.info("Working with URL: {}", url);

		try {

			final HttpGet get = new HttpGet(url);
			HttpResponse response = client.execute(get, context);

			HttpEntity entity = response.getEntity();

			String contents = EntityUtils.toString(entity);

			LOG.info("Response: {}", contents);

			final JsonParser p = new JsonParser();
			final JsonElement element = p.parse(contents);
			if (!element.isJsonObject()) {
				throw new RuntimeException("Not a JSON object");
			}
			final JsonObject json = element.getAsJsonObject();
			final JenkinsConfiguration cfg = new JenkinsConfiguration(json);

			cfg.setNumExecutors(1);

			contents = cfg.toString();

			LOG.info("Sending: {}", contents);

			final HttpPost post = new HttpPost(url);
			post.setEntity(new StringEntity(contents));

			response = client.execute(post, context);
			entity = response.getEntity();

			contents = EntityUtils.toString(entity);

			LOG.info("Response: {}", contents);

		} catch (final IOException e) {
			LOG.error("Something went wrong", e);
		} finally {
			client.getConnectionManager().shutdown();
		}

	}

	private static DefaultHttpClient createHttpClient(final HttpHost host) throws Exception {

		// final UsernamePasswordCredentials credentials = new
		// UsernamePasswordCredentials(USERNAME, PASSWORD);
		// final AuthScope authscope = new AuthScope(host);

		final DefaultHttpClient client = new DefaultHttpClient();
		// client.getCredentialsProvider().setCredentials(authscope, credentials);

		return client;

	}

	private static HttpContext createBasicAuthenticationContext(final HttpHost host) {

		final HttpContext context = new BasicHttpContext();
		final BasicScheme basicScheme = new BasicScheme();

		final AuthCache authCache = new BasicAuthCache();
		authCache.put(host, basicScheme);

		context.setAttribute(ClientContext.AUTH_CACHE, authCache);

		return context;

	}

}
