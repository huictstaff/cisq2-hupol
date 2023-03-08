package nl.hu.cisq2.hupol;

import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Fixture {
    public static String fromFile(String name) {
        try {
            final File file = new ClassPathResource("fixtures/" + name).getFile();
            return Files.readString(file.toPath());
        } catch (IOException exception) {
            throw new RuntimeException(String.format("Could not read fixture '%s'", name));
        }
    }
}
