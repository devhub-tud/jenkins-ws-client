package nl.tudelft.jenkins.jobs;

import nl.tudelft.commons.XmlUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Text;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * User: zjzhai
 * Date: 1/21/14
 * Time: 4:17 PM
 */
public class GitScmConfig extends ScmConfig {
    private static final String XPATH_SCM_GIT_URL = "//scm/userRemoteConfigs/hudson.plugins.git.UserRemoteConfig/url";
    private final static String GITCONFIG_XML_PATH = "/nl/tudelft/jenkins/jobs/GitScmConfig.xml";


    public GitScmConfig(String address) {
        super(address);
    }

    @Override
    public Element getXmlContent() {
        Document doc = XmlUtils.createJobDocumentFrom(getClass().getResourceAsStream(GITCONFIG_XML_PATH));
        final Element element = XmlUtils.findSingleElementInDocumentByXPath(doc, XPATH_SCM_GIT_URL);
        element.setContent(new Text(getAddress()));
        return doc.getRootElement().detach();
    }

}
