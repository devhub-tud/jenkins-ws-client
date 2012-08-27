package nl.tudelft.jenkins.jobs;

import nl.tudelft.jenkins.auth.User;

public interface Job {

	String getName();

	void setScmUrl(String string);

	void setNotificationRecipient(User recipient);

	void addNotificationRecipient(User recipient);

	String asXml();

}
