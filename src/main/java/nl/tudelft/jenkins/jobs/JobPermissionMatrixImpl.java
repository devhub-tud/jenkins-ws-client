package nl.tudelft.jenkins.jobs;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.tudelft.jenkins.auth.User;
import nl.tudelft.jenkins.auth.UserImpl;

import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class JobPermissionMatrixImpl implements JobPermissionMatrix {

	private static final Logger LOG = LoggerFactory.getLogger(JobPermissionMatrixImpl.class);

	private final Element element;
	private final Map<String, JobPermissionMask> permissions;

	private JobPermissionMatrixImpl(Element element) {
		this.element = checkNotNull(element, "element");
		permissions = new HashMap<String, JobPermissionMask>();
	}

	@Override
	public List<User> getUsers() {
		final List<User> users = new ArrayList<User>();

		for (String username : permissions.keySet()) {
			users.add(new UserImpl(username));
		}

		return Collections.unmodifiableList(users);
	}

	@Override
	public void addPermission(User user, JobAuthMatrixPermission permission) {
		LOG.trace("Adding permission for user: {}: {}", user, permission);

		checkNotNull(user, "user");
		String name = user.getName();
		checkArgument(isNotEmpty(name), "user.name must be non-empty");

		checkNotNull(permission, "permission");

		if (!permissions.containsKey(name)) {
			permissions.put(name, new JobPermissionMask());
			permissions.get(name).addPermission(permission);
		}

		if (!permissions.get(name).hasPermission(permission)) {
			permissions.get(name).addPermission(permission);
			addPermissionToDocument(name, permission);
		}

	}

	@Override
	public void removeAllPermissionsForUser(User user) {
		checkNotNull(user, "user must be non-null");
		checkArgument(isNotEmpty(user.getName()), "user.name must be non-empty");

		checkArgument(permissions.containsKey(user.getName()), "No permissions set for user: " + user);

		permissions.remove(user.getName());
	}

	private void addPermissionToDocument(String name, JobAuthMatrixPermission permission) {

		LOG.trace("Adding permission to document for user: {}: {}", name, permission);

		Element newElement = new Element("permission").setText(permission.getName() + ':' + name);
		element.getChildren("permission").add(newElement);

	}

	static JobPermissionMatrixImpl fromElement(Element element) {

		LOG.trace("Parsing authorization matrix from element: {}", element);

		JobPermissionMatrixImpl matrix = new JobPermissionMatrixImpl(element);

		List<Element> permissionElems = element.getChildren("permission");
		for (Element permissionElem : permissionElems) {
			String permissionElemText = permissionElem.getText();
			String[] splitted = permissionElemText.split(":");
			String permissionText = splitted[0];
			String userText = splitted[1];

			JobAuthMatrixPermission permission = JobAuthMatrixPermission.fromText(permissionText);

			if (!matrix.permissions.containsKey(userText)) {
				matrix.permissions.put(userText, new JobPermissionMask());
			}

			matrix.permissions.get(userText).addPermission(permission);
		}

		return matrix;

	}

}
