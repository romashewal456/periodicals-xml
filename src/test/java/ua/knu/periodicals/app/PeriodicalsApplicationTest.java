package ua.knu.periodicals.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PeriodicalsApplicationTest {

    @TempDir
    Path tempDir;

    @Test
    void runCreatesTransformedFile() throws IOException {
        Path output = tempDir.resolve("result.xml");

        int exitCode = new PeriodicalsApplication().run(new String[] {output.toString()});

        assertEquals(0, exitCode);
        String content = Files.readString(output, StandardCharsets.UTF_8);
        assertTrue(content.contains("<papers-by-type>"));
        assertTrue(content.contains("type=\"newspaper\""));
    }

    @Test
    void unwritableOutputGivesErrorCode() {
        Path output = tempDir.resolve("missing-dir").resolve("result.xml");

        assertEquals(1, new PeriodicalsApplication().run(new String[] {output.toString()}));
    }

    @Test
    void existingResourceIsOpened() throws IOException {
        try (InputStream stream = PeriodicalsApplication.openResource(PeriodicalsApplication.XSD_RESOURCE)) {
            assertTrue(stream.read() >= 0);
        }
    }

    @Test
    void missingResourceIsReported() {
        assertThrows(IOException.class, () -> PeriodicalsApplication.openResource("/absent.xml"));
    }

    @Test
    void defaultOutputFileNameIsDefined() {
        assertEquals("papers-by-type.xml", PeriodicalsApplication.DEFAULT_OUTPUT);
    }

    @Test
    void mainRunsSuccessfully() throws IOException {
        Path output = tempDir.resolve("main-result.xml");

        PeriodicalsApplication.main(new String[] {output.toString()});

        assertTrue(Files.exists(output));
    }
}
