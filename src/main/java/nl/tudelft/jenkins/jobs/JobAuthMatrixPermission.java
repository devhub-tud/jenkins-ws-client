package nl.tudelft.jenkins.jobs;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import nl.tudelft.jenkins.auth.User;

public enum JobAuthMatrixPermission {

	JOB_DELETE("hudson.model.Item.Delete"),
	JOB_CONFIGURE("hudson.model.Item.Configure"),
	JOB_READ("hudson.model.Item.Read"),
	JOB_DISCOVER("hudson.model.Item.Discover"),
	JOB_BUILD("hudson.model.Item.Build"),
	JOB_WORKSPACE("hudson.model.Item.Workspace"),
	JOB_CANCEL("hudson.model.Item.Cancel"),
	RUN_DELETE("hudson.model.Run.Delete"),
	RUN_UPDATE("hudson.model.Run.Update"),
	SCM_TAG("hudson.scm.SCM.Tag");

	private final String name;

	private JobAuthMatrixPermission(String name) {
		checkArgument(isNotEmpty(name), "name must be non-empty");

		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String asXmlforUser(User user) {
		checkNotNull(user, "user");
		checkArgument(isNotEmpty(user.getName()), "user.name must be non-empty");

		final StringBuilder builder = new StringBuilder();
		builder.append("<permission>");
		builder.append(name);
		builder.append(':');
		builder.append(user.getName());
		builder.append("</permission>");
		return builder.toString();
	}

	public static JobAuthMatrixPermission fromText(String permissionText) {
		switch (permissionText) {
			case "hudson.model.Item.Delete":
				return JOB_DELETE;
			case "hudson.model.Item.Configure":
				return JOB_CONFIGURE;
			case "hudson.model.Item.Read":
				return JOB_READ;
			case "hudson.model.Item.Discover":
				return JOB_DISCOVER;
			case "hudson.model.Item.Build":
				return JOB_BUILD;
			case "hudson.model.Item.Workspace":
				return JOB_WORKSPACE;
			case "hudson.model.Item.Cancel":
				return JOB_CANCEL;
			case "hudson.model.Run.Delete":
				return RUN_DELETE;
			case "hudson.model.Run.Update":
				return RUN_UPDATE;
			case "hudson.scm.SCM.Tag":
				return SCM_TAG;

			default:
				throw new IllegalArgumentException("Unknown permissionText: " + permissionText);
		}
	}

}
