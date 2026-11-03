package ua.knu.periodicals.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.knu.periodicals.model.Paper;
import ua.knu.periodicals.parser.PaperParseException;
import ua.knu.periodicals.parser.ParserType;
import ua.knu.periodicals.sorting.PaperComparators;
import ua.knu.periodicals.transform.XslTransformer;
import ua.knu.periodicals.validation.XmlValidator;

/**
 * Точка входу: валідація, розбір трьома парсерами, сортування та XSL-перетворення.
 * Першим аргументом можна передати шлях до файлу результату перетворення.
 */
public class PeriodicalsApplication {

    static final String XML_RESOURCE = "/papers.xml";
    static final String XSD_RESOURCE = "/papers.xsd";
    static final String XSL_RESOURCE = "/papers-by-type.xsl";
    static final String DEFAULT_OUTPUT = "papers-by-type.xml";

    private static final Logger LOGGER = LogManager.getLogger(PeriodicalsApplication.class);

    public static void main(String[] args) {
        int exitCode = new PeriodicalsApplication().run(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    public int run(String[] args) {
        Path output = Paths.get(args.length > 0 ? args[0] : DEFAULT_OUTPUT);
        try {
            if (!validate()) {
                LOGGER.error("Документ не пройшов валідацію, обробку зупинено");
                return 1;
            }
            List<Paper> papers = parseWithAllParsers();
            logSorted("за назвою", papers, PaperComparators.BY_TITLE);
            logSorted("за обсягом", papers, PaperComparators.BY_PAGES);
            logSorted("за типом і назвою", papers, PaperComparators.BY_TYPE_THEN_TITLE);
            transform(output);
            return 0;
        } catch (IOException | PaperParseException | TransformerException e) {
            LOGGER.error("Помилка виконання: {}", e.getMessage(), e);
            return 1;
        }
    }

    private boolean validate() throws IOException {
        try (InputStream xsd = openResource(XSD_RESOURCE); InputStream xml = openResource(XML_RESOURCE)) {
            return new XmlValidator(xsd).isValid(xml);
        }
    }

    private List<Paper> parseWithAllParsers() throws IOException, PaperParseException {
        List<Paper> reference = null;
        for (ParserType type : ParserType.values()) {
            try (InputStream xml = openResource(XML_RESOURCE)) {
                List<Paper> papers = type.createParser().parse(xml);
                if (reference == null) {
                    reference = papers;
                } else if (!reference.equals(papers)) {
                    throw new PaperParseException("Результат парсера " + type + " відрізняється від інших");
                }
            }
        }
        LOGGER.info("Усі парсери повернули однаковий результат");
        return reference;
    }

    private void logSorted(String description, List<Paper> papers, Comparator<Paper> comparator) {
        List<Paper> sorted = new ArrayList<>(papers);
        sorted.sort(comparator);
        LOGGER.info("Видання, відсортовані {}:", description);
        sorted.forEach(paper -> LOGGER.info("  {}", paper));
    }

    private void transform(Path output) throws IOException, TransformerException {
        try (InputStream xsl = openResource(XSL_RESOURCE);
             InputStream xml = openResource(XML_RESOURCE);
             OutputStream out = Files.newOutputStream(output)) {
            new XslTransformer(xsl).transform(xml, out);
        }
        LOGGER.info("Результат перетворення збережено у файл {}", output.toAbsolutePath());
    }

    static InputStream openResource(String name) throws IOException {
        InputStream stream = PeriodicalsApplication.class.getResourceAsStream(name);
        if (stream == null) {
            throw new IOException("Не знайдено ресурс " + name);
        }
        return stream;
    }
}
