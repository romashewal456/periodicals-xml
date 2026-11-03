package ua.knu.periodicals.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.knu.periodicals.model.Paper;

/**
 * StAX-парсер: сам витягує події з потоку за допомогою XMLStreamReader.
 */
public class StaxPaperParser implements PaperParser {

    private static final Logger LOGGER = LogManager.getLogger(StaxPaperParser.class);

    @Override
    public List<Paper> parse(InputStream xml) throws PaperParseException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        XMLStreamReader reader = null;
        try {
            reader = factory.createXMLStreamReader(xml);
            List<Paper> papers = new ArrayList<>();
            while (reader.hasNext()) {
                if (reader.next() == XMLStreamConstants.START_ELEMENT
                        && PaperXmlTag.PAPER.getXmlName().equals(reader.getLocalName())) {
                    papers.add(readPaper(reader));
                }
            }
            LOGGER.info("StAX: розібрано видань: {}", papers.size());
            return papers;
        } catch (XMLStreamException | IllegalArgumentException e) {
            throw new PaperParseException("StAX: помилка розбору XML: " + e.getMessage(), e);
        } finally {
            close(reader);
        }
    }

    private Paper readPaper(XMLStreamReader reader) throws XMLStreamException {
        PaperBuilder builder = new PaperBuilder(
                reader.getAttributeValue(null, PaperXmlTag.ID.getXmlName()),
                reader.getAttributeValue(null, PaperXmlTag.LANGUAGE.getXmlName()));
        StringBuilder text = new StringBuilder();
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                text.setLength(0);
            } else if (event == XMLStreamConstants.CHARACTERS) {
                text.append(reader.getText());
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                String name = reader.getLocalName();
                if (PaperXmlTag.PAPER.getXmlName().equals(name)) {
                    return builder.build();
                }
                PaperXmlTag.fromXmlName(name).ifPresent(tag -> builder.setValue(tag, text.toString()));
                text.setLength(0);
            }
        }
        throw new XMLStreamException("Неочікуваний кінець документа всередині елемента paper");
    }

    private static void close(XMLStreamReader reader) {
        if (reader == null) {
            return;
        }
        try {
            reader.close();
        } catch (XMLStreamException e) {
            LOGGER.warn("StAX: не вдалося закрити потік: {}", e.getMessage());
        }
    }
}
