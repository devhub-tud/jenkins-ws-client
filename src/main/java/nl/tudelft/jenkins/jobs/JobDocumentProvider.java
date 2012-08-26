package nl.tudelft.jenkins.jobs;

import java.io.IOException;
import java.io.InputStream;

import nl.tudelft.commons.IOUtils;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class JobDocumentProvider {

	public static final String DEFAULT_JOB_FILE_NAME = "/nl/tudelft/jenkins/jobs/default-job.xml";

	private static final Logger LOG = LoggerFactory.getLogger(JobDocumentProvider.class);

	public static Document createDefaultJobDocument() {

		final InputStream is = IOUtils.class.getResourceAsStream(DEFAULT_JOB_FILE_NAME);
		return createJobDocumentFrom(is);

	}

	public static Document createJobDocumentFrom(final InputStream inputStream) {

		final SAXBuilder builder = new SAXBuilder();

		final Document document;
		try {
			document = builder.build(inputStream);
		} catch (final JDOMException e) {
			LOG.warn("Failed to parse XML in {}", DEFAULT_JOB_FILE_NAME);
			LOG.warn("Exception follows:", e);
			throw new RuntimeException(e.getMessage(), e);
		} catch (final IOException e) {
			LOG.warn("I/O error occurred while reading {}", DEFAULT_JOB_FILE_NAME);
			LOG.warn("Exception follows:", e);
			throw new RuntimeException(e.getMessage(), e);
		}

		return document;

	}

}
