package ua.knu.periodicals.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.knu.periodicals.model.Language;
import ua.knu.periodicals.model.Paper;
import ua.knu.periodicals.model.PublicationType;

class PaperBuilderTest {

    private PaperBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new PaperBuilder("m1", "en");
        builder.setValue(PaperXmlTag.TITLE, "  Журнал  ");
        builder.setValue(PaperXmlTag.TYPE, "magazine");
        builder.setValue(PaperXmlTag.MONTHLY, "1");
        builder.setValue(PaperXmlTag.COLORED, "true");
        builder.setValue(PaperXmlTag.PAGES, " 48 ");
        builder.setValue(PaperXmlTag.GLOSSY, "0");
    }

    @Test
    void buildsPaperFromValues() {
        Paper paper = builder.build();

        assertEquals("m1", paper.getId());
        assertEquals(Language.EN, paper.getLanguage());
        assertEquals("Журнал", paper.getTitle());
        assertEquals(PublicationType.MAGAZINE, paper.getType());
        assertTrue(paper.isMonthly());
        assertTrue(paper.getChars().isColored());
        assertEquals(48, paper.getChars().getPages());
        assertFalse(paper.getChars().isGlossy());
        assertFalse(paper.getChars().getSubscriptionIndex().isPresent());
    }

    @Test
    void subscriptionIndexIsStored() {
        builder.setValue(PaperXmlTag.SUBSCRIPTION_INDEX, "54321");

        assertEquals("54321", builder.build().getChars().getSubscriptionIndex().orElse(""));
    }

    @Test
    void containerTagsAreIgnored() {
        builder.setValue(PaperXmlTag.CHARS, "щось");
        builder.setValue(PaperXmlTag.PAPERS, null);

        assertEquals("Журнал", builder.build().getTitle());
    }

    @Test
    void invalidBooleanIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> builder.setValue(PaperXmlTag.MONTHLY, "так"));
    }

    @Test
    void missingRequiredValuesAreReported() {
        PaperXmlTag[] required = {PaperXmlTag.TYPE, PaperXmlTag.MONTHLY, PaperXmlTag.COLORED,
            PaperXmlTag.PAGES, PaperXmlTag.GLOSSY};
        for (PaperXmlTag missing : required) {
            PaperBuilder incomplete = new PaperBuilder("m1", null);
            incomplete.setValue(PaperXmlTag.TITLE, "Журнал");
            for (PaperXmlTag tag : required) {
                if (tag != missing) {
                    incomplete.setValue(tag, sampleValue(tag));
                }
            }
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, incomplete::build);
            assertTrue(exception.getMessage().contains(missing.getXmlName()));
        }
    }

    @Test
    void missingTitleIsRejected() {
        PaperBuilder withoutTitle = new PaperBuilder("m1", "uk");
        withoutTitle.setValue(PaperXmlTag.TYPE, "booklet");
        withoutTitle.setValue(PaperXmlTag.MONTHLY, "false");
        withoutTitle.setValue(PaperXmlTag.COLORED, "false");
        withoutTitle.setValue(PaperXmlTag.PAGES, "4");
        withoutTitle.setValue(PaperXmlTag.GLOSSY, "false");

        assertThrows(IllegalArgumentException.class, withoutTitle::build);
    }

    private static String sampleValue(PaperXmlTag tag) {
        switch (tag) {
            case TYPE:
                return "magazine";
            case PAGES:
                return "10";
            default:
                return "false";
        }
    }
}
