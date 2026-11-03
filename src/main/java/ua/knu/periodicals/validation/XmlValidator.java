package ua.knu.periodicals.validation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Перевіряє XML-документ на відповідність XSD-схемі.
 */
public class XmlValidator {

    private static final Logger LOGGER = LogManager.getLogger(XmlValidator.class);

    private final Schema schema;

    public XmlValidator(InputStream xsd) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            this.schema = factory.newSchema(new StreamSource(xsd));
        } catch (SAXException e) {
            throw new IllegalArgumentException("Некоректна XSD-схема: " + e.getMessage(), e);
        }
    }

    /**
     * Повертає список знайдених помилок; порожній список означає, що документ валідний.
     */
    public List<String> validate(InputStream xml) {
        Validator validator = schema.newValidator();
        CollectingErrorHandler errorHandler = new CollectingErrorHandler();
        validator.setErrorHandler(errorHandler);
        try {
            validator.validate(new StreamSource(xml));
        } catch (SAXException | IOException e) {
            errorHandler.add(e.getMessage());
        }
        List<String> errors = errorHandler.getErrors();
        if (errors.isEmpty()) {
            LOGGER.info("XML-документ відповідає схемі");
        } else {
            errors.forEach(error -> LOGGER.error("Помилка валідації: {}", error));
        }
        return errors;
    }

    public boolean isValid(InputStream xml) {
        return validate(xml).isEmpty();
    }

    private static final class CollectingErrorHandler implements ErrorHandler {

        private final List<String> errors = new ArrayList<>();

        @Override
        public void warning(SAXParseException exception) {
            LOGGER.warn("Попередження валідації: {}", exception.getMessage());
        }

        @Override
        public void error(SAXParseException exception) {
            add(describe(exception));
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            add(describe(exception));
            throw exception;
        }

        void add(String message) {
            if (!errors.contains(message)) {
                errors.add(message);
            }
        }

        List<String> getErrors() {
            return Collections.unmodifiableList(errors);
        }

        private static String describe(SAXParseException exception) {
            return "рядок " + exception.getLineNumber() + ": " + exception.getMessage();
        }
    }
}
