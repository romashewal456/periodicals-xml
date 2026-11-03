package ua.knu.periodicals.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class PaperXmlTagTest {

    @Test
    void everyTagIsFoundByItsXmlName() {
        for (PaperXmlTag tag : PaperXmlTag.values()) {
            assertEquals(Optional.of(tag), PaperXmlTag.fromXmlName(tag.getXmlName()));
        }
    }

    @Test
    void camelCaseNameIsUsedForSubscriptionIndex() {
        assertEquals("subscriptionIndex", PaperXmlTag.SUBSCRIPTION_INDEX.getXmlName());
    }

    @Test
    void unknownNameGivesEmptyResult() {
        assertTrue(PaperXmlTag.fromXmlName("price").isEmpty());
    }
}
