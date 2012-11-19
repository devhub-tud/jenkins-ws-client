package nl.tudelft.jenkins.auth;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class UserImpl implements User {

	private final String name;
	private final String email;

	public UserImpl(final String name) {
		this(name, null);
	}

	public UserImpl(final String name, final String email) {

		checkArgument(isNotEmpty(name), "name must be non-empty");

		this.name = name;
		this.email = email;

	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
