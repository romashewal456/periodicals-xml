package ua.knu.periodicals.parser;

import ua.knu.periodicals.model.Chars;
import ua.knu.periodicals.model.Language;
import ua.knu.periodicals.model.Paper;
import ua.knu.periodicals.model.PublicationType;

/**
 * Збирає об'єкт Paper зі значень елементів XML.
 * Спільний для всіх парсерів, тому логіка перетворення значень не дублюється.
 */
public class PaperBuilder {

    private String id;
    private Language language;
    private String title;
    private PublicationType type;
    private Boolean monthly;
    private Boolean colored;
    private Integer pages;
    private Boolean glossy;
    private String subscriptionIndex;

    public PaperBuilder(String id, String language) {
        this.id = id;
        this.language = Language.fromXmlValue(language);
    }

    /**
     * Записує текстове значення елемента. Елементи-контейнери ігноруються.
     */
    public void setValue(PaperXmlTag tag, String rawValue) {
        String value = rawValue == null ? "" : rawValue.trim();
        switch (tag) {
            case TITLE:
                title = value;
                break;
            case TYPE:
                type = PublicationType.fromXmlValue(value);
                break;
            case MONTHLY:
                monthly = parseBoolean(value);
                break;
            case COLORED:
                colored = parseBoolean(value);
                break;
            case PAGES:
                pages = Integer.parseInt(value);
                break;
            case GLOSSY:
                glossy = parseBoolean(value);
                break;
            case SUBSCRIPTION_INDEX:
                subscriptionIndex = value;
                break;
            default:
                break;
        }
    }

    public Paper build() {
        requireSet(type, PaperXmlTag.TYPE);
        requireSet(monthly, PaperXmlTag.MONTHLY);
        requireSet(colored, PaperXmlTag.COLORED);
        requireSet(pages, PaperXmlTag.PAGES);
        requireSet(glossy, PaperXmlTag.GLOSSY);
        Chars chars = new Chars(colored, pages, glossy, subscriptionIndex);
        return new Paper(id, language, title, type, monthly, chars);
    }

    private void requireSet(Object value, PaperXmlTag tag) {
        if (value == null) {
            throw new IllegalArgumentException("Відсутній елемент " + tag.getXmlName() + " у виданні " + id);
        }
    }

    private static boolean parseBoolean(String value) {
        if ("true".equals(value) || "1".equals(value)) {
            return true;
        }
        if ("false".equals(value) || "0".equals(value)) {
            return false;
        }
        throw new IllegalArgumentException("Некоректне логічне значення: " + value);
    }
}
