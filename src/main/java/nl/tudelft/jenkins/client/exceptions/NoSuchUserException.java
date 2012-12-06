package nl.tudelft.jenkins.client.exceptions;

/**
 * A {@link NoSuchUserException} is thrown when the user tries to retrieve a
 * user that does not exist.
 */
@SuppressWarnings("serial")
public class NoSuchUserException extends JenkinsException {

	public NoSuchUserException(String userName) {
		super("No such user: " + userName);
	}

}
