package ua.knu.periodicals.parser;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;

class ParserTypeTest {

    @Test
    void eachTypeCreatesItsParser() {
        assertInstanceOf(DomPaperParser.class, ParserType.DOM.createParser());
        assertInstanceOf(SaxPaperParser.class, ParserType.SAX.createParser());
        assertInstanceOf(StaxPaperParser.class, ParserType.STAX.createParser());
    }

    @Test
    void newParserIsCreatedEachTime() {
        assertNotSame(ParserType.SAX.createParser(), ParserType.SAX.createParser());
    }
}
