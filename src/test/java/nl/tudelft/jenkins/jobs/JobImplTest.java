package nl.tudelft.jenkins.jobs;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.InputStream;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;

public class JobImplTest {

	private static final String JOB_NAME = "X";

	@Test
	public void testThatNewlyConsructedJobAsXmlReturnsDefaultJobConfiguration() throws Exception {

		final Job job = new JobImpl(JOB_NAME);
		final String jobAsXml = job.asXml();

		final SAXBuilder builder = new SAXBuilder();
		final InputStream is = this.getClass().getResourceAsStream(JobDocumentProvider.DEFAULT_JOB_FILE_NAME);
		final Document defaultJobDocument = builder.build(is);

		final XMLOutputter xmlOutputter = new XMLOutputter();
		final String defaultJobAsXml = xmlOutputter.outputString(defaultJobDocument);

		assertThat(jobAsXml, is(equalTo(defaultJobAsXml)));

	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatConstructorWithNullNameThrowsException() {
		new JobImpl(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatConstructorWithEmptyNameArgumentThrowsException() {
		new JobImpl("");
	}

}
