
package nl.tudelft.jenkins.client;

import java.io.StringBufferInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import nl.tudelft.jenkins.JenkinsWsClientException;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

class HttpMethodFactory {

    private static final Logger LOG = LoggerFactory.getLogger(HttpMethodFactory.class);

    @Inject
    HttpMethodFactory() {
    }

    /**
     * @param url
     * @param params
     * @return
     * @throws JenkinsWsClientException
     */
    HttpPost createFormPost(String url, List<NameValuePair> params) {
        LOG.trace("Creating FORM POST: {}", url);
        HttpPost post = new HttpPost(url);
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            post.setEntity(entity);
        } catch (UnsupportedEncodingException e) {
            throw new JenkinsWsClientException(e);
        }
        return post;
    }

    HttpPost createPost(String url, String contentType, String contents) {
        LOG.trace("Creating POST: {} - {}", url, contentType);
        StringEntity entity = createStringEntityForContents(contentType, contents);
        HttpPost post = new HttpPost(url);
        post.setEntity(entity);
        return post;
    }

    HttpGet createGet(String url) {
        LOG.trace("Creating GET: {}", url);
        return new HttpGet(url);
    }

    HttpPut createPut(String url, String contentType, String contents) {
        LOG.trace("Creating PUT: {} - {}", url, contentType);

        StringEntity entity = createStringEntityForContents(contentType, contents);

        HttpPut put = new HttpPut(url);
        put.setEntity(entity);

        return put;
    }

    HttpDelete createDelete(String url) {
        LOG.trace("Creating DELETE: {}", url);

        return new HttpDelete(url);
    }

    private StringEntity createStringEntityForContents(String contentType, String contents) {

        StringEntity entity = null;
        try {
            entity = new StringEntity(contents);
            entity.setContentType(contentType);
        } catch (UnsupportedEncodingException e) {
            throw new JenkinsWsClientException(e);
        }
        return entity;
    }

    @SuppressWarnings("serial")
    private static class HttpMethodFactoryException extends RuntimeException {

        public HttpMethodFactoryException(String message, Throwable cause) {
            super(message, cause);
        }

    }

}
