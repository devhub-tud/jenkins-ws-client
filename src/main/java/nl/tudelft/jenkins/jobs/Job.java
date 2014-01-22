package nl.tudelft.jenkins.jobs;

import java.util.List;

import nl.tudelft.jenkins.auth.User;

public interface Job {

    String getName();

    void setScmConfig(ScmConfig scmConfig);

    List<User> getUsers();

    void addPermissionsForUser(User user);

    void removePermissionsForUser(User user);

    void clearNotificationRecipients();

    void addNotificationRecipient(User recipient);

    void removeNotificationRecipient(User recipient);

    String asXml();

}
