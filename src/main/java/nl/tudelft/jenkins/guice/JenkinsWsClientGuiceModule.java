package nl.tudelft.jenkins.guice;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
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
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;

public class JenkinsWsClientGuiceModule extends AbstractModule {

	private static final Logger LOG = LoggerFactory.getLogger(JenkinsWsClientGuiceModule.class);

	private final URL endpoint;

	private final HttpHost jenkinsHost;
	private final Credentials credentials;

	public JenkinsWsClientGuiceModule(URL jenkinsUrl, final String username, final String password) {

		LOG.trace("Creating new Jenkins WS Client Guice module for: {}@{} ...", username, jenkinsUrl);

		endpoint = jenkinsUrl;
		credentials = new UsernamePasswordCredentials(username, password);
		jenkinsHost = new HttpHost(endpoint.getHost(), endpoint.getPort());

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

	@BindingAnnotation
	@Retention(RetentionPolicy.RUNTIME)
	@Target({FIELD, PARAMETER, METHOD})
	public static @interface JenkinsUrl {}

}
