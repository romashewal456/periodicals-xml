package ua.knu.periodicals.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class CharsTest {

    @Test
    void gettersReturnConstructorValues() {
        Chars chars = new Chars(true, 40, true, "12345");

        assertTrue(chars.isColored());
        assertEquals(40, chars.getPages());
        assertTrue(chars.isGlossy());
        assertEquals(Optional.of("12345"), chars.getSubscriptionIndex());
    }

    @Test
    void subscriptionIndexIsOptional() {
        assertFalse(new Chars(false, 1, false, null).getSubscriptionIndex().isPresent());
    }

    @Test
    void pagesOutsideBoundsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Chars(false, 0, false, null));
        assertThrows(IllegalArgumentException.class, () -> new Chars(false, 1001, false, null));
    }

    @Test
    void invalidSubscriptionIndexIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Chars(false, 10, false, "1234"));
        assertThrows(IllegalArgumentException.class, () -> new Chars(false, 10, false, "12a45"));
    }

    @Test
    void equalCharsHaveSameHashCode() {
        Chars first = new Chars(true, 10, false, "12345");
        Chars second = new Chars(true, 10, false, "12345");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertEquals(first, first);
    }

    @Test
    void differentCharsAreNotEqual() {
        Chars chars = new Chars(true, 10, false, "12345");

        assertNotEquals(chars, new Chars(false, 10, false, "12345"));
        assertNotEquals(chars, new Chars(true, 11, false, "12345"));
        assertNotEquals(chars, new Chars(true, 10, true, "12345"));
        assertNotEquals(chars, new Chars(true, 10, false, null));
        assertFalse(chars.equals("chars"));
    }

    @Test
    void toStringContainsValues() {
        assertTrue(new Chars(true, 10, false, "12345").toString().contains("pages=10"));
    }
}
