package nl.tudelft.jenkins.client;

import java.net.URL;
import java.util.List;

import nl.tudelft.jenkins.auth.User;
import nl.tudelft.jenkins.client.exceptions.NoSuchJobException;
import nl.tudelft.jenkins.client.exceptions.NoSuchUserException;
import nl.tudelft.jenkins.jobs.Job;

import com.google.inject.ImplementedBy;
import nl.tudelft.jenkins.jobs.ScmConfig;

/**
 * Jenkins Web Service API Client.
 */
@ImplementedBy(JenkinsClientImpl.class)
public interface JenkinsClient{

    /**
     * Validate the Jenkins server is ok
     *
     * @return
     */
    boolean validateServerOnEndpoint();

    /**
     * Get the Jenkins server root endpoint.
     *
     * @return A {@link URL} representing the URL of the Jenkins server.
     */
    URL getJenkinsEndpoint();

    /**
     * Create a new build job with the given parameters.
     *
     *
     * @param name   Job name.
     * @param scmConfig SCM URL.
     * @param owners {@link java.util.List} of authorized users for the new build job.
     * @return A {@link Job} representing the newly created build job.
     */
    Job createJob(String name, ScmConfig scmConfig, List<User> owners);

    /**
     * Retrieve the configuration of an existing build job.
     *
     * @param jobName The name of the job.
     * @return A {@link Job} representing the named job.
     * @throws @{link {@link NoSuchJobException} If no job exists with the given
     *                name.
     */
    Job retrieveJob(String jobName) throws NoSuchJobException;

    /**
     * Update the configuration of an existing build job.
     *
     * @param job A representation of the job configuration.
     */
    void updateJob(Job job);

    /**
     * Delete an existing build job.
     *
     * @param job The {@link Job} instance representing the job.
     */
    void deleteJob(Job job);

    /**
     * Create a new user in the Jenkins system.
     *
     * @param userName The login name of the user.
     * @param password The password of the user.
     * @param email    The user's email address.
     * @param fullName The full name or display name of the user.
     * @return A {@link User} instance representing the newly created user.
     */
    User createUser(String userName, String password, String email, String fullName);

    /**
     * Retrieve a user by its name.
     *
     * @param userName The name to look up.
     * @return A {@link User} instance representing the user.
     * @throws NoSuchUserException If no user exists corresponding to the given
     *                             name.
     */
    User retrieveUser(String userName) throws NoSuchUserException;

    /**
     * @param user The {@link User} instance representing the user's data that
     *             must be updated.
     * @return An {@link User} instance representing the updated user.
     * @throws NoSuchUserException If no user exists corresponding to the given
     *                             {@link User} instance.
     */
    User updateUser(User user) throws NoSuchUserException;

    /**
     * Delete a given user from the Jenkins system.
     *
     * @param user A {@link User} instance representing the user to delete.
     * @throws NoSuchUserException If no user exists corresponding to the given
     *                             {@link User} instance.
     */
    void deleteUser(User user) throws NoSuchUserException;

    /**
     * Release all acquired resources.
     * <p/>
     * Make sure the {@link JenkinsClient} instance is not reused after calling
     * this method.
     */
    void close();

}
