package ua.knu.periodicals.parser;

/**
 * Помилка розбору XML-документа з виданнями.
 */
public class PaperParseException extends Exception {

    private static final long serialVersionUID = 1L;

    public PaperParseException(String message) {
        super(message);
    }

    public PaperParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
