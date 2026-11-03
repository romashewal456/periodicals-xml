package ua.knu.periodicals.parser;

class SaxPaperParserTest extends AbstractPaperParserTest {

    @Override
    protected PaperParser createParser() {
        return new SaxPaperParser();
    }
}
