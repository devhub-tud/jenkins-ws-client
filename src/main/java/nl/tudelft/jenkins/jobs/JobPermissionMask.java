package nl.tudelft.jenkins.jobs;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

public class JobPermissionMask {

	private final List<JobAuthMatrixPermission> permissions;

	public JobPermissionMask() {
		permissions = new ArrayList<JobAuthMatrixPermission>();
	}

	public void addPermission(JobAuthMatrixPermission permission) {
		checkNotNull(permission, "permission");

		if (permissions.contains(permission)) {
			return;
		}

		permissions.add(permission);
	}

	public boolean hasPermission(JobAuthMatrixPermission permission) {
		return permissions.contains(permission);
	}

}
