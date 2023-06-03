package org.vorlyanskiy.fixwidthviz.services;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CommandLineActions implements CommandLineRunner {

    public static final Pattern KEY_COMMAND_LINE = Pattern.compile("^--(\\w+)=");
    private Path sourceFile;

    private Path destinationFile;

    private final Transformer transformer;

    @Override
    public void run(String... args) throws Exception {
        Arrays.stream(args).
                forEach(this::workWithOneArg);
        transform();
    }

    private void transform() throws IOException, TransformerException {
        if (sourceFile == null || destinationFile == null) {
            throw new RuntimeException("Source and destinationi files should be set for transform");
        }
        transformer.transform(sourceFile, destinationFile);
    }

    private void workWithOneArg(String arg) {
        Matcher matcher = KEY_COMMAND_LINE.matcher(arg);
        if (matcher.find()) {
            String key = matcher.group(1);
            switch (key) {
                case "sourcefile" -> sourceFile = extractPath(arg);
                case "destinationfile" -> destinationFile = extractPath(arg);
                default -> throw new RuntimeException("Command line parameter %1$s is not definied");
            }
        }
    }

    private Path extractPath(String arg) {
        String[] splitted = arg.split("=");
        Path path = Paths.get(splitted[1]);
        return path;
    }
}
