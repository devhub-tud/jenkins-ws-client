package nl.tudelft.jenkins.client;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.net.URL;

import nl.tudelft.jenkins.guice.JenkinsUrl;
import nl.tudelft.jenkins.guice.JenkinsWsClientGuiceModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class JenkinsClientFactory {

	private Injector injector;

	public JenkinsClientFactory(@JenkinsUrl URL jenkinsUrl, String username, String password) {
		checkNotNull(jenkinsUrl, "jenkinsUrl must be non-null");
		checkArgument(isNotEmpty(username), "username must be non-empty");

		injector = Guice.createInjector(new JenkinsWsClientGuiceModule(jenkinsUrl, username, password));
	}

	public JenkinsClient getJenkinsClient() {
		return injector.getInstance(JenkinsClient.class);
	}

}
