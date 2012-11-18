package nl.tudelft.jenkins.tests.integration;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import nl.tudelft.jenkins.auth.User;
import nl.tudelft.jenkins.auth.UserImpl;
import nl.tudelft.jenkins.jobs.Job;

import org.junit.Test;

public class JobUpdateIntegrationTest extends AbstractJenkinsIntegrationTestBase {

	private static final String USER_NAME = "u";
	private static final String USER_EMAIL = "e@mail";

	@Test
	public void testThatJobCanBeUpdated() throws Exception {
		User user = new UserImpl(USER_NAME, USER_EMAIL);

		List<User> users = newArrayList();

		Job job = createJob(JOB_SCM_URL, users);
		job.addUser(user);

		updateJob(job);

		job = retrieveJob();

		users = job.getUsers();

		assertThat(users, hasSize(1));
		User storedUser = users.get(0);
		assertThat(storedUser.getName(), is(equalTo(user.getName())));
		assertThat(storedUser.getEmail(), is(equalTo(user.getEmail())));

	}

}
