package nl.tudelft.jenkins.jobs;

import org.jdom2.Element;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * User: zjzhai
 * Date: 1/21/14
 * Time: 4:16 PM
 */
public abstract class ScmConfig {

    private String address;

    public abstract Element getXmlContent();

    public ScmConfig(String address) {
        checkNotNull(address, "address must be non-null");
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
