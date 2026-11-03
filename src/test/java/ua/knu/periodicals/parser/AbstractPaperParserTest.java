package ua.knu.periodicals.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import ua.knu.periodicals.TestData;
import ua.knu.periodicals.model.Language;
import ua.knu.periodicals.model.Paper;

/**
 * Спільні тести для всіх парсерів. Кожен парсер має пройти однаковий набір перевірок.
 */
abstract class AbstractPaperParserTest {

    protected abstract PaperParser createParser();

    private List<Paper> parse(String xml) throws PaperParseException {
        return createParser().parse(TestData.stream(xml));
    }

    @Test
    void parsesAllPapersFromMainDocument() throws PaperParseException {
        List<Paper> papers = createParser().parse(TestData.resource("/papers.xml"));

        assertEquals(6, papers.size());
        assertEquals(TestData.NEWSPAPER, papers.get(0));
        assertEquals(TestData.MAGAZINE, papers.get(1));
        assertEquals(TestData.BOOKLET, papers.get(2));
    }

    @Test
    void missingLanguageAttributeGivesDefault() throws PaperParseException {
        Paper paper = parse(TestData.singlePaper("id=\"x1\"", TestData.newspaperBody())).get(0);

        assertEquals("x1", paper.getId());
        assertEquals(Language.DEFAULT, paper.getLanguage());
        assertEquals("12345", paper.getChars().getSubscriptionIndex().orElse(""));
    }

    @Test
    void entitiesInTextAreDecoded() throws PaperParseException {
        String body = TestData.newspaperBody().replace("Газета", "Ділова &amp; Політика");

        assertEquals("Ділова & Політика", parse(TestData.singlePaper("id=\"x1\"", body)).get(0).getTitle());
    }

    @Test
    void unknownElementsAreIgnored() throws PaperParseException {
        String body = TestData.newspaperBody() + "<note>примітка</note>";

        assertEquals(1, parse(TestData.singlePaper("id=\"x1\"", body)).size());
    }

    @Test
    void documentWithoutPapersGivesEmptyList() throws PaperParseException {
        assertTrue(parse("<papers/>").isEmpty());
    }

    @Test
    void malformedDocumentIsRejected() {
        assertThrows(PaperParseException.class, () -> parse("<papers><paper id=\"x1\">"));
    }

    @Test
    void invalidNumberIsRejected() {
        String body = TestData.newspaperBody().replace("<pages>8</pages>", "<pages>вісім</pages>");

        PaperParseException exception = assertThrows(PaperParseException.class,
                () -> parse(TestData.singlePaper("id=\"x1\"", body)));
        assertInstanceOf(NumberFormatException.class, exception.getCause());
    }

    @Test
    void missingElementIsRejected() {
        String body = TestData.newspaperBody().replace("<glossy>false</glossy>", "");

        assertThrows(PaperParseException.class, () -> parse(TestData.singlePaper("id=\"x1\"", body)));
    }

    @Test
    void businessRuleViolationIsRejected() {
        String body = TestData.newspaperBody().replace("<glossy>false</glossy>", "<glossy>true</glossy>");

        assertThrows(PaperParseException.class, () -> parse(TestData.singlePaper("id=\"x1\"", body)));
    }
}
