package nl.tudelft.jenkins.jobs;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import nl.tudelft.jenkins.auth.User;
import nl.tudelft.jenkins.auth.UserImpl;

import org.junit.Test;

public class JobPermissionTest {

	@Test
	public void testThatJobPermissionReturnsCorrectXmlString() throws Exception {

		String name = "name";
		String email = "email";

		User user = new UserImpl(name, email);

		String xml = JobAuthMatrixPermission.JOB_WORKSPACE.asXmlforUser(user);

		assertThat(xml, is(equalTo("<permission>hudson.model.Item.Workspace:" + name + "</permission>")));

	}

}
