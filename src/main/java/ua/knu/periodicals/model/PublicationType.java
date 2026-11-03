package ua.knu.periodicals.model;

/**
 * Тип періодичного видання.
 */
public enum PublicationType {

    NEWSPAPER("newspaper", false, true),
    MAGAZINE("magazine", true, true),
    BOOKLET("booklet", true, false);

    private final String xmlValue;
    private final boolean glossyAllowed;
    private final boolean subscriptionIndexAllowed;

    PublicationType(String xmlValue, boolean glossyAllowed, boolean subscriptionIndexAllowed) {
        this.xmlValue = xmlValue;
        this.glossyAllowed = glossyAllowed;
        this.subscriptionIndexAllowed = subscriptionIndexAllowed;
    }

    public String getXmlValue() {
        return xmlValue;
    }

    public boolean isGlossyAllowed() {
        return glossyAllowed;
    }

    public boolean isSubscriptionIndexAllowed() {
        return subscriptionIndexAllowed;
    }

    public static PublicationType fromXmlValue(String value) {
        for (PublicationType type : values()) {
            if (type.xmlValue.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Невідомий тип видання: " + value);
    }
}
