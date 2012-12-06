package nl.tudelft.jenkins.jobs;

import java.util.List;

import nl.tudelft.jenkins.auth.User;

interface JobPermissionMatrix {

	void addPermission(User user, JobAuthMatrixPermission permission);

	List<User> getUsers();

}
