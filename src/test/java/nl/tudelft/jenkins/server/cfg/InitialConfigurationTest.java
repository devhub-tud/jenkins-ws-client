package nl.tudelft.jenkins.server.cfg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitialConfigurationTest extends AbstractConfigurationTest {

	public static final Logger LOG = LoggerFactory.getLogger(InitialConfigurationTest.class);

	@Override
	protected String getInputFileName() {
		return "/nl/tudelft/jenkins/server/cfg/tests/initial.json";
	}

}
