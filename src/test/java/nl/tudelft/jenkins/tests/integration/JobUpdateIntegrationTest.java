package nl.tudelft.jenkins.tests.integration;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import nl.tudelft.jenkins.auth.User;
import nl.tudelft.jenkins.auth.UserImpl;
import nl.tudelft.jenkins.jobs.Job;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class JobUpdateIntegrationTest extends AbstractJenkinsIntegrationTestBase {

    private static final String USER_NAME = "u";
    private static final String USER_EMAIL = "e@mail";

    @Test
    public void testThatJobCanBeUpdated() throws Exception {
        User user = new UserImpl(USER_NAME, USER_EMAIL);

        List<User> users = newArrayList();

        Job createdJob = createJob(JOB_SCM_URL, users);

        assertThat(createdJob.getUsers(), is(empty()));

        createdJob.addPermissionsForUser(user);
        updateJob(createdJob);

        Job retrievedJob = retrieveJob();

        assertThat(retrievedJob.getUsers(), hasSize(1));
        assertThat(retrievedJob.getUsers().get(0).getName(), is(equalTo(user.getName())));

    }

}
