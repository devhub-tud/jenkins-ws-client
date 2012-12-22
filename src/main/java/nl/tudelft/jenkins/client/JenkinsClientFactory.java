package nl.tudelft.jenkins.client;

import java.net.URL;

import nl.tudelft.jenkins.guice.JenkinsWsClientGuiceModule;
import nl.tudelft.jenkins.guice.JenkinsWsClientGuiceModule.JenkinsUrl;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class JenkinsClientFactory {

	private Injector injector;

	public JenkinsClientFactory(@JenkinsUrl URL jenkinsUrl, String username, String password) {
		injector = Guice.createInjector(new JenkinsWsClientGuiceModule(jenkinsUrl, username, password));
	}

	public JenkinsClient getJenkinsClient() {
		return injector.getInstance(JenkinsClient.class);
	}

}
