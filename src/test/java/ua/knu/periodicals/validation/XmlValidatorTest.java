package ua.knu.periodicals.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.knu.periodicals.TestData;

class XmlValidatorTest {

    private XmlValidator validator;

    @BeforeEach
    void setUp() {
        validator = new XmlValidator(TestData.resource("/papers.xsd"));
    }

    private boolean isValid(String xml) {
        return validator.isValid(TestData.stream(xml));
    }

    @Test
    void mainDocumentIsValid() {
        assertTrue(validator.validate(TestData.resource("/papers.xml")).isEmpty());
    }

    @Test
    void minimalDocumentIsValid() {
        assertTrue(isValid(TestData.singlePaper("id=\"x1\" language=\"en\"", TestData.newspaperBody())));
    }

    @Test
    void unknownEnumerationValueIsInvalid() {
        String body = TestData.newspaperBody().replace("newspaper", "book");

        assertFalse(isValid(TestData.singlePaper("id=\"x1\"", body)));
    }

    @Test
    void pagesOutsideBoundsAreInvalid() {
        String body = TestData.newspaperBody().replace("<pages>8</pages>", "<pages>1001</pages>");

        assertFalse(isValid(TestData.singlePaper("id=\"x1\"", body)));
    }

    @Test
    void wrongSubscriptionIndexPatternIsInvalid() {
        String body = TestData.newspaperBody().replace("12345", "123");

        assertFalse(isValid(TestData.singlePaper("id=\"x1\"", body)));
    }

    @Test
    void missingRequiredIdAttributeIsInvalid() {
        assertFalse(isValid(TestData.singlePaper("", TestData.newspaperBody())));
    }

    @Test
    void idMustBeValidXmlName() {
        assertFalse(isValid(TestData.singlePaper("id=\"1abc\"", TestData.newspaperBody())));
    }

    @Test
    void duplicateIdsAreInvalid() {
        String paper = "<paper id=\"x1\">" + TestData.newspaperBody() + "</paper>";

        assertFalse(isValid("<papers>" + paper + paper + "</papers>"));
    }

    @Test
    void unknownLanguageIsInvalid() {
        assertFalse(isValid(TestData.singlePaper("id=\"x1\" language=\"de\"", TestData.newspaperBody())));
    }

    @Test
    void malformedDocumentIsInvalidAndErrorIsReported() {
        List<String> errors = validator.validate(TestData.stream("<papers><paper>"));

        assertFalse(errors.isEmpty());
    }

    @Test
    void invalidSchemaIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new XmlValidator(TestData.stream("<not-a-schema")));
    }
}
