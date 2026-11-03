package ua.knu.periodicals.parser;

import java.io.InputStream;
import java.util.List;

import ua.knu.periodicals.model.Paper;

/**
 * Розбирає XML-документ і повертає список видань.
 */
public interface PaperParser {

    List<Paper> parse(InputStream xml) throws PaperParseException;
}
