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

        if ("hudson.model.Item.Delete".equals(permissionText)) {
            return JOB_DELETE;
        }

        if ("hudson.model.Item.Configure".equals(permissionText)) {
            return JOB_CONFIGURE;
        }
        if ("hudson.model.Item.Read".equals(permissionText)) {
            return JOB_READ;
        }
        if ("hudson.model.Item.Discover".equals(permissionText)) {
            return JOB_DISCOVER;
        }
        if ("hudson.model.Item.Build".equals(permissionText)) {
            return JOB_BUILD;
        }
        if ("hudson.model.Item.Workspace".equals(permissionText)) {
            return JOB_WORKSPACE;
        }
        if ("hudson.model.Item.Cancel".equals(permissionText)) {
            return JOB_CANCEL;
        }
        if ("hudson.model.Run.Delete".equals(permissionText)) {
            return RUN_DELETE;
        }
        if ("hudson.model.Run.Update".equals(permissionText)) {
            return RUN_UPDATE;
        }
        if ("hudson.scm.SCM.Tag".equals(permissionText)) {
            return SCM_TAG;
        }

        throw new IllegalArgumentException("Unknown permissionText: " + permissionText);
    }

}
