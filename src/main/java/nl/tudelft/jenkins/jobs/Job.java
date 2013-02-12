package nl.tudelft.jenkins.jobs;

import java.util.List;

import nl.tudelft.jenkins.auth.User;

public interface Job {

	String getName();

	String getScmUrl();

	void setScmUrl(String string);

	List<User> getUsers();

	void addUser(User user);

	void removeUser(User user);

	void clearNotificationRecipients();

	void addNotificationRecipient(User recipient);

	void removeNotificationRecipient(User recipient);

	String asXml();

}
