
package nl.tudelft.jenkins;

/**
 * User: zjzhai
 * Date: 3/12/14
 * Time: 2:20 PM
 */
public class JenkinsWsClientException extends RuntimeException {

    public JenkinsWsClientException() {
        super();
    }

    public JenkinsWsClientException(String message) {
        super(message);
    }

    public JenkinsWsClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public JenkinsWsClientException(Throwable cause) {
        super(cause);
    }

    protected JenkinsWsClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
