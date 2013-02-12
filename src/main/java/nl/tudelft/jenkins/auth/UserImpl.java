package nl.tudelft.jenkins.auth;

import static com.google.common.base.Preconditions.checkArgument;
import static nl.tudelft.commons.XmlUtils.findSingleElementInDocumentByXPath;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.io.InputStream;

import nl.tudelft.commons.XmlUtils;
import nl.tudelft.commons.XmlUtils.XmlUtilsException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserImpl implements User {

	private static final Logger LOG = LoggerFactory.getLogger(UserImpl.class);

	public static User fromXml(String xml) {
		LOG.trace("Parsing User from xml: " + xml);

		InputStream contents = IOUtils.toInputStream(xml);

		Document document = XmlUtils.createJobDocumentFrom(contents);

		Element idElement = findSingleElementInDocumentByXPath(document, "//user/id");
		String name = idElement.getText();

		String email;
		try {
			Element emailElement = null;
			emailElement = findSingleElementInDocumentByXPath(document, "//user/property/address");
			email = emailElement.getText();
		} catch (XmlUtilsException e) {
			email = null;
		}

		return new UserImpl(name, email);
	}

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
	public boolean equals(Object that) {
		if (that == null) {
			return false;
		}

		if (this == that) {
			return true;
		}

		if (that instanceof User) {
			return getName().equals(((User) that).getName());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(name)
				.hashCode();
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
