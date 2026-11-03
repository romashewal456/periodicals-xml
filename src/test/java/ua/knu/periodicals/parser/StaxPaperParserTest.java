package ua.knu.periodicals.parser;

class StaxPaperParserTest extends AbstractPaperParserTest {

    @Override
    protected PaperParser createParser() {
        return new StaxPaperParser();
    }
}
