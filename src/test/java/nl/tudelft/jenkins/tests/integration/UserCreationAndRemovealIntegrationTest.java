package nl.tudelft.jenkins.tests.integration;

import nl.tudelft.jenkins.auth.User;
import nl.tudelft.jenkins.auth.UserImpl;
import nl.tudelft.jenkins.jobs.Job;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zjzhai
 * Date: 1/26/14
 * Time: 10:18 AM
 */
@Ignore
public class UserCreationAndRemovealIntegrationTest extends AbstractJenkinsIntegrationTestBase {

    private static final User USER0 = new UserImpl("person", "person@example.com");
    private static final User USER1 = new UserImpl("other", "other@example.com");

    private static final List<User> USERS = new ArrayList<User>();

    static {
        USERS.add(USER0);
        USERS.add(USER1);
    }

    @Test
    public void testCreateUser() throws Exception {

        Job job = createJob(JOB_SCM_URL, USERS);

        job = retrieveJob();

        User user = createUser("user1", "password", "ij@dd.com", "fllName");

        User retrieveUser = retrieveUser("user1");

        assert user.getEmail().equals(retrieveUser.getEmail());


    }
}
