package ua.knu.periodicals.sorting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ua.knu.periodicals.TestData;
import ua.knu.periodicals.model.Chars;
import ua.knu.periodicals.model.Language;
import ua.knu.periodicals.model.Paper;
import ua.knu.periodicals.model.PublicationType;

class PaperComparatorsTest {

    private final List<Paper> papers = new ArrayList<>(List.of(TestData.BOOKLET, TestData.NEWSPAPER, TestData.MAGAZINE));

    @Test
    void sortsByTitle() {
        papers.sort(PaperComparators.BY_TITLE);

        assertEquals(List.of(TestData.MAGAZINE, TestData.BOOKLET, TestData.NEWSPAPER), papers);
    }

    @Test
    void sortsByPages() {
        papers.sort(PaperComparators.BY_PAGES);

        assertEquals(List.of(TestData.BOOKLET, TestData.NEWSPAPER, TestData.MAGAZINE), papers);
    }

    @Test
    void sortsByTypeThenTitle() {
        Paper otherNewspaper = new Paper("p4", Language.UK, "Вечірня газета", PublicationType.NEWSPAPER, true,
                new Chars(false, 12, false, null));
        papers.add(otherNewspaper);

        papers.sort(PaperComparators.BY_TYPE_THEN_TITLE);

        assertEquals(List.of(otherNewspaper, TestData.NEWSPAPER, TestData.MAGAZINE, TestData.BOOKLET), papers);
    }
}
