package ua.knu.periodicals.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class LanguageTest {

    @Test
    void languagesAreFoundByXmlValue() {
        assertEquals(Language.UK, Language.fromXmlValue("uk"));
        assertEquals(Language.EN, Language.fromXmlValue("en"));
        assertEquals("en", Language.EN.getXmlValue());
    }

    @Test
    void missingLanguageGivesDefault() {
        assertEquals(Language.DEFAULT, Language.fromXmlValue(null));
        assertEquals(Language.DEFAULT, Language.fromXmlValue(""));
    }

    @Test
    void unknownLanguageIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> Language.fromXmlValue("de"));
    }
}
