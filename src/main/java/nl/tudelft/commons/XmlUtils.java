package nl.tudelft.commons;

import java.io.*;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlUtils {

    private static final Logger LOG = LoggerFactory.getLogger(XmlUtils.class);


    public static Document createJobDocumentFrom(InputStream inputStream) {
        System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
        final SAXBuilder builder = new SAXBuilder();

        final Document document;
        try {
            document = builder.build(inputStream);
        } catch (final JDOMException e) {
            LOG.warn("Failed to parse XML in input stream");
            LOG.warn("Exception follows:", e);
            throw new RuntimeException(e.getMessage(), e);
        } catch (final IOException e) {
            LOG.warn("I/O error occurred while reading input stream");
            LOG.warn("Exception follows:", e);
            throw new RuntimeException(e.getMessage(), e);
        }

        return document;

    }

    public static Element findSingleElementInDocumentByXPath(Document document, String xPath) {

        final XPathFactory xPathFactory = XPathFactory.instance();
        final XPathExpression<Element> xPathExpression = xPathFactory.compile(xPath, Filters.element());
        final Element element = xPathExpression.evaluateFirst(document);

        if (element == null) {
            throw new XmlUtilsException("Document does not contain element on path: " + xPath);
        }

        return element;

    }

    @SuppressWarnings("serial")
    public static class XmlUtilsException extends RuntimeException {
        public XmlUtilsException(String message) {
            super(message);
        }
    }

    private XmlUtils() {
    }

}
