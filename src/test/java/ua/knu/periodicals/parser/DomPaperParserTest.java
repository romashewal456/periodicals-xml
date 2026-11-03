package ua.knu.periodicals.parser;

class DomPaperParserTest extends AbstractPaperParserTest {

    @Override
    protected PaperParser createParser() {
        return new DomPaperParser();
    }
}
