package nl.tudelft.dom4j;

import java.io.InputStream;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDOMTestApp {

	private static final Logger LOG = LoggerFactory.getLogger(JDOMTestApp.class);

	public static void main(final String[] args) throws Exception {

		LOG.info("Starting...");

		final SAXBuilder builder = new SAXBuilder();
		final InputStream is = JDOMTestApp.class.getResourceAsStream("/nl/tudelft/jenkins/server/job/standard-job.xml");
		final Document document = builder.build(is);

		final XMLOutputter out = new XMLOutputter();
		String xml = out.outputString(document);
		LOG.info("XML: {}", xml);

		final Element root = document.getRootElement();
		LOG.info("root: {}", root);
		final Element scm = root.getChild("scm");
		LOG.info("scm: {}", scm);
		final Element userRemoteConfig = scm.getChild("userRemoteConfigs");
		final Element gitConfig = userRemoteConfig.getChild("hudson.plugins.git.UserRemoteConfig");
		final Element url = gitConfig.getChild("url");
		url.setContent(new Text("Hello, world!"));

		// final XPathFactory xpathFactory = XPathFactory.instance();
		// final XPathExpression<Element> xpathExpression =
		// xpathFactory.compile("//maven2-moduleset/scm/userRemoteConfig/hudson.plugins.git.UserRemoteConfig",
		// new ElementFilter());
		// final List<Element> elements = xpathExpression.evaluate(root);

		xml = out.outputString(document);
		LOG.info("XML: {}", xml);

		LOG.info("Finished.");

	}

}
