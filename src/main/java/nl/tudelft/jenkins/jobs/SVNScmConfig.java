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
public class SVNScmConfig implements ScmConfig {

    private final static String SVNCONFIG_XML_PATH = "/nl/tudelft/jenkins/jobs/SVNScmConfig.xml";
    private static final String XPATH_SCM_SVN_URL = "//scm/locations/hudson.scm.SubversionSCM_-ModuleLocation/remote";


    private String address;
    private String username;
    private String password;

    public SVNScmConfig(String address, String username, String password) {
        checkNotNull(address, "svnAddress must be non-null");
        this.address = address;
        this.username = username;
        this.password = password;
    }


    @Override
    public Element getXmlContent() {
        Document doc = XmlUtils.createJobDocumentFrom(getClass().getResourceAsStream(SVNCONFIG_XML_PATH));
        final Element element = XmlUtils.findSingleElementInDocumentByXPath(doc, XPATH_SCM_SVN_URL);
        element.setContent(new Text(getAddress()));
        return doc.getRootElement().detach();
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "SVNScmConfig{" +
                "svnAddress='" + address + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


}
