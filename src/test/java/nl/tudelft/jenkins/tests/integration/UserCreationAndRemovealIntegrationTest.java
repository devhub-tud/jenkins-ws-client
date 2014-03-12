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

    private static final User USER0 = new UserImpl("persxon", "person@example.com");
    private static final User USER1 = new UserImpl("other国", "other@example.com");

    private static final List<User> USERS = new ArrayList<User>();

    static {
        USERS.add(USER0);
        USERS.add(USER1);
    }

    @Test
    public void testCreateUser() throws Exception {

        String username = "bbbbxxb4443";

        User user = createUser(username, "password", "ij@dd.com", "全名中国xx");

        User retrieveUser = retrieveUser(username);

        assert user.getEmail().equals(retrieveUser.getEmail());


    }
}
