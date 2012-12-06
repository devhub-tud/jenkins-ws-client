package nl.tudelft.jenkins.auth;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class UserImplTest {

	@Test
	public void testThatParsingFromCorrectXmlWorks() throws Exception {

		InputStream is = getClass().getResourceAsStream("misterx.xml");
		String xml = IOUtils.toString(is);

		User user = UserImpl.fromXml(xml);

		assertThat(user, is(notNullValue()));
		assertThat(user.getName(), is("mrx"));
		assertThat(user.getEmail(), is("mrx@example.com"));

	}

}
