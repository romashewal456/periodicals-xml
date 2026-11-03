package ua.knu.periodicals.model;

import java.util.Objects;
import java.util.Optional;

/**
 * Характеристики видання.
 */
public final class Chars {

    private static final int MAX_PAGES = 1000;

    private final boolean colored;
    private final int pages;
    private final boolean glossy;
    private final String subscriptionIndex;

    /**
     * @param subscriptionIndex п'ятизначний підписний індекс або null, якщо його немає
     */
    public Chars(boolean colored, int pages, boolean glossy, String subscriptionIndex) {
        if (pages < 1 || pages > MAX_PAGES) {
            throw new IllegalArgumentException("Обсяг має бути від 1 до " + MAX_PAGES + " сторінок: " + pages);
        }
        if (subscriptionIndex != null && !subscriptionIndex.matches("\\d{5}")) {
            throw new IllegalArgumentException("Підписний індекс має складатися з 5 цифр: " + subscriptionIndex);
        }
        this.colored = colored;
        this.pages = pages;
        this.glossy = glossy;
        this.subscriptionIndex = subscriptionIndex;
    }

    public boolean isColored() {
        return colored;
    }

    public int getPages() {
        return pages;
    }

    public boolean isGlossy() {
        return glossy;
    }

    public Optional<String> getSubscriptionIndex() {
        return Optional.ofNullable(subscriptionIndex);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Chars)) {
            return false;
        }
        Chars that = (Chars) other;
        return colored == that.colored && pages == that.pages && glossy == that.glossy
                && Objects.equals(subscriptionIndex, that.subscriptionIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(colored, pages, glossy, subscriptionIndex);
    }

    @Override
    public String toString() {
        return "Chars{colored=" + colored + ", pages=" + pages + ", glossy=" + glossy
                + ", subscriptionIndex=" + subscriptionIndex + '}';
    }
}
