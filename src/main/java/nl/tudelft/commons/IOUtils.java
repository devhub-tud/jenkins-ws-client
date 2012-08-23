package nl.tudelft.commons;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOUtils {

	private static final Logger LOG = LoggerFactory.getLogger(IOUtils.class);

	/**
	 * Read a resource from the classpath and return the contents as
	 * {@link String}.
	 * 
	 * @param name The name of the resource, for example,
	 *           <code>/nl/tudelft/commons/IOUtils.java</code>.
	 * 
	 * @return The contents of the resource.
	 * 
	 * @throws IOException An I/O error occurred while reading the resource.
	 */
	public static String readResource(final String name) throws IOException {

		LOG.info("Reading resource: {}", name);

		checkNotNull(name, "name must not be null");

		final InputStream is = IOUtils.class.getResourceAsStream(name);
		final InputStreamReader isr = new InputStreamReader(is);
		final BufferedReader br = new BufferedReader(isr);

		final StringBuilder sb = new StringBuilder();
		while (br.ready()) {
			final String line = br.readLine();
			sb.append(line);
		}

		return sb.toString();

	}

}
