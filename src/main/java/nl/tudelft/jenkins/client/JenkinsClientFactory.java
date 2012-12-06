package nl.tudelft.jenkins.client;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import nl.tudelft.jenkins.guice.JenkinsWsClientGuiceModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class JenkinsClientFactory {

	private Injector injector;

	public JenkinsClientFactory(String hostname, int port, String context, String username, String password) {

		checkArgument(isNotEmpty(hostname), "hostname must be non-empty");

		injector = Guice.createInjector(new JenkinsWsClientGuiceModule(hostname, port, context, username, password));

	}

	public JenkinsClient getJenkinsClient() {
		return injector.getInstance(JenkinsClient.class);
	}

}
