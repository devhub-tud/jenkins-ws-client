package nl.tudelft.jenkins.jobs;

import java.util.List;

import nl.tudelft.jenkins.auth.User;

public interface Job {

	String getName();

	String getScmUrl();

	void setScmUrl(String string);

	List<User> getUsers();

	void addUser(User user);

	void setNotificationRecipient(User recipient);

	void addNotificationRecipient(User recipient);

	String asXml();

}
