package ua.knu.periodicals.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import ua.knu.periodicals.model.Paper;

/**
 * SAX-парсер: читає документ потоково, реагуючи на події через PaperSaxHandler.
 */
public class SaxPaperParser implements PaperParser {

    private static final Logger LOGGER = LogManager.getLogger(SaxPaperParser.class);

    @Override
    public List<Paper> parse(InputStream xml) throws PaperParseException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            SAXParser parser = factory.newSAXParser();
            PaperSaxHandler handler = new PaperSaxHandler();
            parser.parse(xml, handler);
            List<Paper> papers = handler.getPapers();
            LOGGER.info("SAX: розібрано видань: {}", papers.size());
            return papers;
        } catch (ParserConfigurationException | SAXException | IOException | IllegalArgumentException e) {
            throw new PaperParseException("SAX: помилка розбору XML: " + e.getMessage(), e);
        }
    }
}
