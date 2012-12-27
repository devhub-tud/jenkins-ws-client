package nl.tudelft.jenkins.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.jenkins.auth.User;
import nl.tudelft.jenkins.auth.UserImpl;
import nl.tudelft.jenkins.jobs.Job;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientTestApp {

	private static final Logger LOG = LoggerFactory.getLogger(ClientTestApp.class);

	public static void main(String[] args) throws MalformedURLException {

		LOG.info("Starting ...");

		HttpHost jenkinsHost = new HttpHost("192.168.56.101", 8080, "http");
		Credentials credentials = new UsernamePasswordCredentials("david", "x");

		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.getCredentialsProvider().setCredentials(new AuthScope(jenkinsHost), credentials);

		AuthCache authCache = new BasicAuthCache();
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(jenkinsHost, basicAuth);

		HttpContext httpContext = new BasicHttpContext();
		httpContext.setAttribute(ClientContext.AUTH_CACHE, authCache);

		HttpMethodFactory methodFactory = new HttpMethodFactory();
		HttpRestClient httpRestClient = new HttpRestClientImpl(httpClient, httpContext, methodFactory);
		URL endpoint = new URL("http://192.168.56.101:8080/");

		try (JenkinsClient client = new JenkinsClientImpl(httpRestClient, endpoint)) {

			LOG.info("Booted.");

			Job job = client.retrieveJob("mini-project");
			LOG.info("Got job xml: {}", job.asXml());

			List<User> users = new ArrayList<>();
			users.add(new UserImpl("xyz"));
			Job job2 = client.createJob("xyz", "git://0", users);

			LOG.info("Got job2 xml: {}", job2.asXml());

			job2.addUser(new UserImpl("x"));

			client.updateJob(job2);

			Job job3 = client.retrieveJob("xyz");

			LOG.info("Got job3 xml: {}", job3.asXml());

		}

		LOG.info("Done.");

	}

}
