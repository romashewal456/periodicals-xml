package ua.knu.periodicals.parser;

import java.util.function.Supplier;

/**
 * Доступні реалізації парсера.
 */
public enum ParserType {

    DOM(DomPaperParser::new),
    SAX(SaxPaperParser::new),
    STAX(StaxPaperParser::new);

    private final Supplier<PaperParser> factory;

    ParserType(Supplier<PaperParser> factory) {
        this.factory = factory;
    }

    public PaperParser createParser() {
        return factory.get();
    }
}
