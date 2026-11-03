package ua.knu.periodicals.parser;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import ua.knu.periodicals.model.Paper;

/**
 * Обробник подій SAX, що накопичує видання у список.
 */
public class PaperSaxHandler extends DefaultHandler {

    private final List<Paper> papers = new ArrayList<>();
    private final StringBuilder text = new StringBuilder();
    private PaperBuilder currentBuilder;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        text.setLength(0);
        if (PaperXmlTag.PAPER.getXmlName().equals(qName)) {
            currentBuilder = new PaperBuilder(
                    attributes.getValue(PaperXmlTag.ID.getXmlName()),
                    attributes.getValue(PaperXmlTag.LANGUAGE.getXmlName()));
        }
    }

    @Override
    public void characters(char[] chars, int start, int length) {
        text.append(chars, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (currentBuilder == null) {
            return;
        }
        if (PaperXmlTag.PAPER.getXmlName().equals(qName)) {
            papers.add(currentBuilder.build());
            currentBuilder = null;
        } else {
            PaperXmlTag.fromXmlName(qName).ifPresent(tag -> currentBuilder.setValue(tag, text.toString()));
        }
        text.setLength(0);
    }

    public List<Paper> getPapers() {
        return papers;
    }
}
