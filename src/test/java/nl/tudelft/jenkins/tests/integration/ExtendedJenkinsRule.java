package nl.tudelft.jenkins.tests.integration;

import org.jvnet.hudson.test.JenkinsRule;

public class ExtendedJenkinsRule extends JenkinsRule {

	public int getLocalPort() {
		return super.localPort;
	}

}
