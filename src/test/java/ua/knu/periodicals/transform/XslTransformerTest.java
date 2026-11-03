package ua.knu.periodicals.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ua.knu.periodicals.TestData;

class XslTransformerTest {

    private Document result;

    @BeforeEach
    void setUp() throws TransformerException, ParserConfigurationException, SAXException, IOException {
        XslTransformer transformer = new XslTransformer(TestData.resource("/papers-by-type.xsl"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        transformer.transform(TestData.resource("/papers.xml"), out);
        result = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new ByteArrayInputStream(out.toByteArray()));
    }

    @Test
    void rootElementIsRenamed() {
        assertEquals("papers-by-type", result.getDocumentElement().getTagName());
    }

    @Test
    void papersAreSplitIntoGroupsByTypeInAlphabeticalOrder() {
        NodeList groups = result.getElementsByTagName("group");

        assertEquals(3, groups.getLength());
        assertEquals("booklet", ((Element) groups.item(0)).getAttribute("type"));
        assertEquals("magazine", ((Element) groups.item(1)).getAttribute("type"));
        assertEquals("newspaper", ((Element) groups.item(2)).getAttribute("type"));
    }

    @Test
    void eachGroupContainsItsPapers() {
        NodeList groups = result.getElementsByTagName("group");
        for (int i = 0; i < groups.getLength(); i++) {
            Element group = (Element) groups.item(i);
            NodeList papers = group.getElementsByTagName("paper");
            assertEquals(group.getAttribute("count"), String.valueOf(papers.getLength()));
            assertEquals(2, papers.getLength());
        }
    }

    @Test
    void typeElementIsMovedToGroupAttribute() {
        assertEquals(0, result.getElementsByTagName("type").getLength());
        assertEquals(6, result.getElementsByTagName("title").getLength());
    }

    @Test
    void attributesOfPapersArePreserved() {
        Element magazineGroup = (Element) result.getElementsByTagName("group").item(1);
        Element firstMagazine = (Element) magazineGroup.getElementsByTagName("paper").item(0);

        assertEquals("p2", firstMagazine.getAttribute("id"));
        assertEquals("en", firstMagazine.getAttribute("language"));
    }

    @Test
    void invalidStylesheetIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new XslTransformer(TestData.stream("<xsl:broken")));
    }
}
