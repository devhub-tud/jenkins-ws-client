package nl.tudelft.jenkins.jobs;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobImpl implements Job {

	private static final Logger LOG = LoggerFactory.getLogger(JobImpl.class);

	private final String name;
	private final Document document;

	public JobImpl(final String name) {
		this(name, JobDocumentProvider.createDefaultJobDocument());
	}

	public JobImpl(final String name, final Document document) {

		checkArgument(isNotEmpty(name), "name must be non-empty");

		this.name = name;
		this.document = checkNotNull(document, "document must be non-null");

	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setScmUrl(final String scmUrl) {

		final Element root = document.getRootElement();
		final Element scm = root.getChild("scm");
		final Element userRemoteConfigs = scm.getChild("userRemoteConfigs");
		final Element gitConfig = userRemoteConfigs.getChild("hudson.plugins.git.UserRemoteConfig");
		final Element url = gitConfig.getChild("url");

		url.setContent(new Text(scmUrl));

	}

	@Override
	public String asXml() {

		LOG.trace("Generating XML representation...");

		final XMLOutputter outputter = new XMLOutputter();
		final String xml = outputter.outputString(document);

		return xml;

	}

	@Override
	public String toString() {

		final ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);

		builder.append("name", name);

		return builder.toString();

	}

	public static Job fromXml(final String name, final String xml) {

		final InputStream is = IOUtils.toInputStream(xml);

		final Document document = JobDocumentProvider.createJobDocumentFrom(is);

		return new JobImpl(name, document);

	}

}
