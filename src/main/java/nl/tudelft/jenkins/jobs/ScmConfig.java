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

    private String username;

    private String password;

    public abstract Element getXmlContent();

    protected ScmConfig(String address, String username, String password) {
        checkNotNull(address, "address must be non-null");
        this.address = address;
        this.username = username;
        this.password = password;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
