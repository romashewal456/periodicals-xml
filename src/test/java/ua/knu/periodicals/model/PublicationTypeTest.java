package ua.knu.periodicals.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PublicationTypeTest {

    @Test
    void typesAreFoundByXmlValue() {
        assertEquals(PublicationType.NEWSPAPER, PublicationType.fromXmlValue("newspaper"));
        assertEquals(PublicationType.MAGAZINE, PublicationType.fromXmlValue("magazine"));
        assertEquals(PublicationType.BOOKLET, PublicationType.fromXmlValue("booklet"));
    }

    @Test
    void unknownTypeIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> PublicationType.fromXmlValue("book"));
    }

    @Test
    void onlyMagazinesAndBookletsCanBeGlossy() {
        assertFalse(PublicationType.NEWSPAPER.isGlossyAllowed());
        assertTrue(PublicationType.MAGAZINE.isGlossyAllowed());
        assertTrue(PublicationType.BOOKLET.isGlossyAllowed());
    }

    @Test
    void onlyNewspapersAndMagazinesHaveSubscriptionIndex() {
        assertTrue(PublicationType.NEWSPAPER.isSubscriptionIndexAllowed());
        assertTrue(PublicationType.MAGAZINE.isSubscriptionIndexAllowed());
        assertFalse(PublicationType.BOOKLET.isSubscriptionIndexAllowed());
    }

    @Test
    void xmlValueIsLowerCaseName() {
        assertEquals("magazine", PublicationType.MAGAZINE.getXmlValue());
    }
}
