package nl.tudelft.jenkins.auth;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import nl.tudelft.jenkins.client.exceptions.NoSuchUserException;
import nl.tudelft.jenkins.tests.integration.AbstractJenkinsIntegrationTestBase;

import org.junit.Ignore;
import org.junit.Test;

public class UserManagementIntegrationTest extends AbstractJenkinsIntegrationTestBase {

	private static final String USER = "mrx";
	private static final String PASSWORD = "x";
	private static final String EMAIL = "mrx@example.com";
	private static final String FULLNAME = "Mister X";

	@Test
	@Ignore("Deleting a user only removes it from the security realm - it still exists in the people list")
	public void testThatUserCanBeCreatedAndRemoved() throws Exception {
		User user = createUser(USER, PASSWORD, EMAIL, FULLNAME);

		assertThat(user.getName(), is(USER));
		assertThat(user.getEmail(), is(EMAIL));

		deleteUser(user);

		boolean exceptionWasThrown = false;
		try {
			retrieveUser(USER);
		} catch (NoSuchUserException e) {
			exceptionWasThrown = true;
		}
		assertThat(exceptionWasThrown, is(true));
	}

}
