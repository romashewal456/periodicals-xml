package ua.knu.periodicals.parser;

import java.util.Optional;

/**
 * Імена елементів і атрибутів XML-документа з виданнями.
 */
public enum PaperXmlTag {

    PAPERS("papers"),
    PAPER("paper"),
    ID("id"),
    LANGUAGE("language"),
    TITLE("title"),
    TYPE("type"),
    MONTHLY("monthly"),
    CHARS("chars"),
    COLORED("colored"),
    PAGES("pages"),
    GLOSSY("glossy"),
    SUBSCRIPTION_INDEX("subscriptionIndex");

    private final String xmlName;

    PaperXmlTag(String xmlName) {
        this.xmlName = xmlName;
    }

    public String getXmlName() {
        return xmlName;
    }

    public static Optional<PaperXmlTag> fromXmlName(String name) {
        for (PaperXmlTag tag : values()) {
            if (tag.xmlName.equals(name)) {
                return Optional.of(tag);
            }
        }
        return Optional.empty();
    }
}
