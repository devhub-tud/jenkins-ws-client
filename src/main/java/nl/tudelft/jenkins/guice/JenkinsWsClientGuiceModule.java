package nl.tudelft.jenkins.guice;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import javax.inject.Named;

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

	private final String endpoint;

	private final HttpHost jenkinsHost;
	private final Credentials credentials;

	public JenkinsWsClientGuiceModule(final String hostname, int port, final String path, final String username, final String password) {

		LOG.trace("Creating new Jenkins WS Client Guice module for: {}@{}:{}{} ...", username, hostname, port, path);

		checkArgument(!isEmpty(hostname), "endpoint must be non-empty");
		checkArgument(!isEmpty(username), "username must be non-empty");
		checkArgument(!isEmpty(password), "password must be non-empty");

		final StringBuilder builder = new StringBuilder("http://");
		builder.append(hostname);
		builder.append(':');
		builder.append(port);
		if (path == null || path.isEmpty()) {
			builder.append('/');
		} else {
			builder.append(path);
			if (!path.endsWith("/")) {
				builder.append('/');
			}
		}

		endpoint = builder.toString();
		credentials = new UsernamePasswordCredentials(username, password);
		jenkinsHost = new HttpHost(hostname, port);

	}

	@Override
	protected void configure() {}

	@Provides
	@Named("JenkinsEndpoint")
	public String getJenkinsEndpoint() {
		return endpoint;
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
