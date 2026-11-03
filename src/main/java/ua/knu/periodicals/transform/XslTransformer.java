package ua.knu.periodicals.transform;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Перетворює XML-документ за допомогою XSL-шаблону.
 */
public class XslTransformer {

    private static final Logger LOGGER = LogManager.getLogger(XslTransformer.class);

    private final Templates templates;

    public XslTransformer(InputStream xsl) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setErrorListener(new LoggingErrorListener());
            this.templates = factory.newTemplates(new StreamSource(xsl));
        } catch (TransformerConfigurationException e) {
            throw new IllegalArgumentException("Некоректний XSL-шаблон: " + e.getMessage(), e);
        }
    }

    public void transform(InputStream xml, OutputStream result) throws TransformerException {
        templates.newTransformer().transform(new StreamSource(xml), new StreamResult(result));
        LOGGER.info("XSL-перетворення виконано");
    }

    private static final class LoggingErrorListener implements ErrorListener {

        @Override
        public void warning(TransformerException exception) {
            LOGGER.warn("XSL: {}", exception.getMessage());
        }

        @Override
        public void error(TransformerException exception) throws TransformerException {
            throw exception;
        }

        @Override
        public void fatalError(TransformerException exception) throws TransformerException {
            throw exception;
        }
    }
}
