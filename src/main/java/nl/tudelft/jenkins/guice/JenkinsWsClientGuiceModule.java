package nl.tudelft.jenkins.guice;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class JenkinsWsClientGuiceModule extends AbstractModule {

	private static final Logger LOG = LoggerFactory.getLogger(JenkinsWsClientGuiceModule.class);

	private final URL endpoint;

	private final HttpHost jenkinsHost;
	private final Credentials credentials;

	public JenkinsWsClientGuiceModule(URL jenkinsUrl, final String username, final String password) {

		LOG.trace("Creating new Jenkins WS Client Guice module for: {}@{} ...", username, jenkinsUrl);

		checkArgument(isNotEmpty(username), "username must be non-empty");

		endpoint = checkNotNull(jenkinsUrl, "jenkinsUrl");
		credentials = new UsernamePasswordCredentials(username, password);

		int port = endpoint.getPort() == -1 ? 80 : endpoint.getPort();
		jenkinsHost = new HttpHost(endpoint.getHost(), port);
	}

	@Override
	protected void configure() {
		bind(URL.class).annotatedWith(JenkinsUrl.class).toInstance(endpoint);
	}

	@Provides
	public HttpClient getHttpClient() {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getCredentialsProvider().setCredentials(new AuthScope(jenkinsHost), credentials);
		return client;
	}

	@Provides
	public HttpContext getHttpContext() {
		AuthCache authCache = new BasicAuthCache();
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(jenkinsHost, basicAuth);

		HttpContext httpContext = new BasicHttpContext();
		httpContext.setAttribute(ClientContext.AUTH_CACHE, authCache);

		return httpContext;
	}

}
