# Java wrapper for the Jenkins web service API

## Usage

Create a Guice injector with the **JenkinsWsClientGuiceModule** and retrieve an instance of **JenkinsClient**.

	Injector injector = Guice.createInjector(new JenkinsWsClientGuiceModule("http://my.jenkins.endpoint/jenkins"));
	JenkinsClient client = injector.getInstance(JenkinsClient.class);

New jobs can be created with **JenkinsClient.createJob(...)**. Existing jobs can be retrieved with **JenkinsClient.retrieveJob(...)**, and deleted with **JenkinsClient.deleteJob(...)**.

	// Prepare list of users that has access to the job.
	List<User> users = new ArrayList<>();
	users.add(new UserImpl("John Doe", "john@doe.com");

	Job newJob = client.createJob("MyFirstJob", "git://my.git.repo/myfirstproject.git", users);

	Job existingJob = client.retrieveJob("SomeOtherJob");

	client.deleteJob(existingJob);


Make sure to release all acquired resources with the **JenkinsClient.close()** method afterwards:

	client.close();

The JenkinsClient class is [AutoCloseable](http://docs.oracle.com/javase/7/docs/api/java/lang/AutoCloseable.html), so it can be used in a try-with-resources pattern.

## Testing
To run the integrationtest, copy the `src/test/resources/*.example` to files without the `.example` suffix, and edit them to point the integration tests to your own  Jenkins server.

Make sure that these files do *not* end with a newline!
