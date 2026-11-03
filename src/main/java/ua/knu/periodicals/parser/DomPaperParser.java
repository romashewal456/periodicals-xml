package ua.knu.periodicals.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ua.knu.periodicals.model.Paper;

/**
 * DOM-парсер: завантажує весь документ у дерево і обходить його.
 */
public class DomPaperParser implements PaperParser {

    private static final Logger LOGGER = LogManager.getLogger(DomPaperParser.class);

    @Override
    public List<Paper> parse(InputStream xml) throws PaperParseException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new DefaultHandler());
            Document document = builder.parse(xml);
            NodeList paperNodes = document.getElementsByTagName(PaperXmlTag.PAPER.getXmlName());
            List<Paper> papers = new ArrayList<>();
            for (int i = 0; i < paperNodes.getLength(); i++) {
                papers.add(buildPaper((Element) paperNodes.item(i)));
            }
            LOGGER.info("DOM: розібрано видань: {}", papers.size());
            return papers;
        } catch (ParserConfigurationException | SAXException | IOException | IllegalArgumentException e) {
            throw new PaperParseException("DOM: помилка розбору XML: " + e.getMessage(), e);
        }
    }

    private Paper buildPaper(Element paperElement) {
        PaperBuilder builder = new PaperBuilder(
                paperElement.getAttribute(PaperXmlTag.ID.getXmlName()),
                paperElement.getAttribute(PaperXmlTag.LANGUAGE.getXmlName()));
        fillValues(paperElement, builder);
        return builder.build();
    }

    private void fillValues(Element parent, PaperBuilder builder) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) child;
                PaperXmlTag.fromXmlName(element.getTagName())
                        .ifPresent(tag -> builder.setValue(tag, element.getTextContent()));
                fillValues(element, builder);
            }
        }
    }
}
