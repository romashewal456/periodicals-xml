package ua.knu.periodicals.model;

import java.util.Objects;

/**
 * Періодичне видання, що відповідає елементу paper схеми papers.xsd.
 */
public final class Paper {

    private final String id;
    private final Language language;
    private final String title;
    private final PublicationType type;
    private final boolean monthly;
    private final Chars chars;

    public Paper(String id, Language language, String title, PublicationType type, boolean monthly, Chars chars) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Ідентифікатор видання не може бути порожнім");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Назва видання не може бути порожньою");
        }
        Objects.requireNonNull(type, "Тип видання не задано");
        Objects.requireNonNull(chars, "Характеристики видання не задано");
        if (chars.isGlossy() && !type.isGlossyAllowed()) {
            throw new IllegalArgumentException("Видання типу " + type.getXmlValue() + " не може бути глянцевим");
        }
        if (chars.getSubscriptionIndex().isPresent() && !type.isSubscriptionIndexAllowed()) {
            throw new IllegalArgumentException("Видання типу " + type.getXmlValue() + " не має підписного індексу");
        }
        this.id = id;
        this.language = language == null ? Language.DEFAULT : language;
        this.title = title;
        this.type = type;
        this.monthly = monthly;
        this.chars = chars;
    }

    public String getId() {
        return id;
    }

    public Language getLanguage() {
        return language;
    }

    public String getTitle() {
        return title;
    }

    public PublicationType getType() {
        return type;
    }

    public boolean isMonthly() {
        return monthly;
    }

    public Chars getChars() {
        return chars;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Paper)) {
            return false;
        }
        Paper that = (Paper) other;
        return monthly == that.monthly && id.equals(that.id) && language == that.language
                && title.equals(that.title) && type == that.type && chars.equals(that.chars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, language, title, type, monthly, chars);
    }

    @Override
    public String toString() {
        return "Paper{id='" + id + "', language=" + language.getXmlValue() + ", title='" + title
                + "', type=" + type.getXmlValue() + ", monthly=" + monthly + ", " + chars + '}';
    }
}
