package ua.knu.periodicals.model;

/**
 * Мова видання.
 */
public enum Language {

    UK("uk"),
    EN("en");

    public static final Language DEFAULT = UK;

    private final String xmlValue;

    Language(String xmlValue) {
        this.xmlValue = xmlValue;
    }

    public String getXmlValue() {
        return xmlValue;
    }

    public static Language fromXmlValue(String value) {
        if (value == null || value.isEmpty()) {
            return DEFAULT;
        }
        for (Language language : values()) {
            if (language.xmlValue.equals(value)) {
                return language;
            }
        }
        throw new IllegalArgumentException("Невідома мова: " + value);
    }
}
