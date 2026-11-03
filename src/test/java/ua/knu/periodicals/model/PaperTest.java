package ua.knu.periodicals.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ua.knu.periodicals.TestData;

class PaperTest {

    private static final Chars NEWSPAPER_CHARS = new Chars(false, 16, false, "61001");

    @Test
    void gettersReturnConstructorValues() {
        Paper paper = TestData.MAGAZINE;

        assertEquals("p2", paper.getId());
        assertEquals(Language.EN, paper.getLanguage());
        assertEquals("Tech Review", paper.getTitle());
        assertEquals(PublicationType.MAGAZINE, paper.getType());
        assertTrue(paper.isMonthly());
        assertEquals(84, paper.getChars().getPages());
    }

    @Test
    void missingLanguageGivesDefault() {
        Paper paper = new Paper("p1", null, "Газета", PublicationType.NEWSPAPER, false, NEWSPAPER_CHARS);

        assertEquals(Language.DEFAULT, paper.getLanguage());
    }

    @Test
    void blankIdOrTitleIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Paper(" ", Language.UK, "Газета", PublicationType.NEWSPAPER, false, NEWSPAPER_CHARS));
        assertThrows(IllegalArgumentException.class,
                () -> new Paper(null, Language.UK, "Газета", PublicationType.NEWSPAPER, false, NEWSPAPER_CHARS));
        assertThrows(IllegalArgumentException.class,
                () -> new Paper("p1", Language.UK, "", PublicationType.NEWSPAPER, false, NEWSPAPER_CHARS));
        assertThrows(IllegalArgumentException.class,
                () -> new Paper("p1", Language.UK, null, PublicationType.NEWSPAPER, false, NEWSPAPER_CHARS));
    }

    @Test
    void missingTypeOrCharsIsRejected() {
        assertThrows(NullPointerException.class,
                () -> new Paper("p1", Language.UK, "Газета", null, false, NEWSPAPER_CHARS));
        assertThrows(NullPointerException.class,
                () -> new Paper("p1", Language.UK, "Газета", PublicationType.NEWSPAPER, false, null));
    }

    @Test
    void newspaperCannotBeGlossy() {
        Chars glossy = new Chars(false, 16, true, null);

        assertThrows(IllegalArgumentException.class,
                () -> new Paper("p1", Language.UK, "Газета", PublicationType.NEWSPAPER, false, glossy));
    }

    @Test
    void bookletCannotHaveSubscriptionIndex() {
        Chars withIndex = new Chars(true, 8, true, "12345");

        assertThrows(IllegalArgumentException.class,
                () -> new Paper("p3", Language.UK, "Буклет", PublicationType.BOOKLET, false, withIndex));
    }

    @Test
    void equalPapersHaveSameHashCode() {
        Paper copy = new Paper("p1", Language.UK, "Київський вісник", PublicationType.NEWSPAPER, false,
                new Chars(false, 16, false, "61001"));

        assertEquals(TestData.NEWSPAPER, copy);
        assertEquals(TestData.NEWSPAPER.hashCode(), copy.hashCode());
        assertEquals(copy, copy);
    }

    @Test
    void papersWithDifferentFieldsAreNotEqual() {
        Paper paper = TestData.NEWSPAPER;

        assertNotEquals(paper, new Paper("p9", Language.UK, "Київський вісник", PublicationType.NEWSPAPER, false,
                NEWSPAPER_CHARS));
        assertNotEquals(paper, new Paper("p1", Language.EN, "Київський вісник", PublicationType.NEWSPAPER, false,
                NEWSPAPER_CHARS));
        assertNotEquals(paper, new Paper("p1", Language.UK, "Інша", PublicationType.NEWSPAPER, false,
                NEWSPAPER_CHARS));
        assertNotEquals(paper, new Paper("p1", Language.UK, "Київський вісник", PublicationType.MAGAZINE, false,
                NEWSPAPER_CHARS));
        assertNotEquals(paper, new Paper("p1", Language.UK, "Київський вісник", PublicationType.NEWSPAPER, true,
                NEWSPAPER_CHARS));
        assertNotEquals(paper, new Paper("p1", Language.UK, "Київський вісник", PublicationType.NEWSPAPER, false,
                new Chars(false, 17, false, "61001")));
        assertFalse(paper.equals("p1"));
    }

    @Test
    void toStringContainsMainFields() {
        String text = TestData.MAGAZINE.toString();

        assertTrue(text.contains("p2"));
        assertTrue(text.contains("Tech Review"));
        assertTrue(text.contains("magazine"));
    }
}
