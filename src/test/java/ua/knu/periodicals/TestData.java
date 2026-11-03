package ua.knu.periodicals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import ua.knu.periodicals.model.Chars;
import ua.knu.periodicals.model.Language;
import ua.knu.periodicals.model.Paper;
import ua.knu.periodicals.model.PublicationType;

/**
 * Спільні тестові дані.
 */
public final class TestData {

    public static final Paper NEWSPAPER = new Paper("p1", Language.UK, "Київський вісник",
            PublicationType.NEWSPAPER, false, new Chars(false, 16, false, "61001"));

    public static final Paper MAGAZINE = new Paper("p2", Language.EN, "Tech Review",
            PublicationType.MAGAZINE, true, new Chars(true, 84, true, "74215"));

    public static final Paper BOOKLET = new Paper("p3", Language.UK, "Відпочинок у Карпатах",
            PublicationType.BOOKLET, false, new Chars(true, 8, true, null));

    private TestData() {
    }

    public static InputStream stream(String text) {
        return new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    }

    public static InputStream resource(String name) {
        return TestData.class.getResourceAsStream(name);
    }

    /**
     * Документ з одним виданням, тіло якого передається параметром.
     */
    public static String singlePaper(String attributes, String body) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><papers><paper " + attributes + ">"
                + body + "</paper></papers>";
    }

    public static String newspaperBody() {
        return "<title>Газета</title><type>newspaper</type><monthly>true</monthly>"
                + "<chars><colored>false</colored><pages>8</pages><glossy>false</glossy>"
                + "<subscriptionIndex>12345</subscriptionIndex></chars>";
    }
}
