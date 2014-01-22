package nl.tudelft.jenkins.jobs;

import java.io.InputStream;

import nl.tudelft.commons.XmlUtils;

import org.jdom2.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class JobDocumentProvider {

	public static final String DEFAULT_JOB_FILE_NAME = "/nl/tudelft/jenkins/jobs/default-job.xml";

	private static final Logger LOG = LoggerFactory.getLogger(JobDocumentProvider.class);

	public static Document createDefaultJobDocument() {
		LOG.trace("Creating default job document ...");
		final InputStream is = JobDocumentProvider.class.getResourceAsStream(DEFAULT_JOB_FILE_NAME);
        return XmlUtils.createJobDocumentFrom(is);
	}

	private JobDocumentProvider() {}

}
