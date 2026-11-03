package ua.knu.periodicals.sorting;

import java.util.Comparator;

import ua.knu.periodicals.model.Paper;

/**
 * Набір компараторів для сортування видань.
 */
public final class PaperComparators {

    public static final Comparator<Paper> BY_TITLE = Comparator.comparing(Paper::getTitle);

    public static final Comparator<Paper> BY_PAGES = Comparator.comparingInt(paper -> paper.getChars().getPages());

    public static final Comparator<Paper> BY_TYPE_THEN_TITLE =
            Comparator.comparing(Paper::getType).thenComparing(BY_TITLE);

    private PaperComparators() {
    }
}
