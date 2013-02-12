package nl.tudelft.jenkins.jobs;

import java.util.List;

import nl.tudelft.jenkins.auth.User;

interface JobPermissionMatrix {

	List<User> getUsers();

	void addPermission(User user, JobAuthMatrixPermission permission);

	void removeAllPermissionsForUser(User user);

}
