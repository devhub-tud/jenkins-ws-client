package nl.tudelft.jenkins.jobs;

import org.jdom2.Element;

/**
 * User: zjzhai
 * Date: 1/21/14
 * Time: 4:16 PM
 */
public interface ScmConfig {

    Element getXmlContent();

    String getUsername();

    String getPassword();


}
