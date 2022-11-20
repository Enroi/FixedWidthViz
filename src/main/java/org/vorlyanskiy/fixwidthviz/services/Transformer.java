package org.vorlyanskiy.fixwidthviz.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.vorlyanskiy.fixwidthviz.config.ConfigProducer;
import org.vorlyanskiy.fixwidthviz.lineconfigs.FieldConfig;
import org.vorlyanskiy.fixwidthviz.lineconfigs.FieldValue;
import org.vorlyanskiy.fixwidthviz.lineconfigs.LineConfig;
import org.vorlyanskiy.fixwidthviz.lineconfigs.LineParsed;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class Transformer {

    private final ConfigProducer configProducer;

    @Value("classpath:transformer.xsl")
    private Resource xsl;

    private static final Logger logger = LoggerFactory.getLogger(Transformer.class);

    public void transform(Path sourceFile, Path destinationFile) throws IOException, TransformerException {
        Collection<LineConfig> lineConfigs = configProducer.getLineConfigs();
        List<LineParsed> fieldValues = Files.lines(sourceFile)
                .map(line -> convertToValue(lineConfigs, line))
                .toList();
        writeResult(fieldValues, destinationFile);
    }

    private void writeResult(List<LineParsed> fieldValues, Path destinationFile) throws IOException, TransformerException {
        Path jsonTempFile = Files.createTempFile("FixedWidthConverterResult.", ".json");
        logger.info("Temp JSON file: %1$s".formatted(jsonTempFile));

        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsBytes(fieldValues);
        Files.write(jsonTempFile, bytes);

        Path tempXml = Files.createTempFile("FixedWidthConverterResult.", ".xml");
        logger.info("Temp XML file: %1$s".formatted(tempXml));
        XmlMapper xmlMapper = new XmlMapper();
        byte[] bytesXml = xmlMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsBytes(fieldValues);
        Files.write(tempXml, bytesXml);
        transformToHtml(tempXml, destinationFile);
    }

    private void transformToHtml(Path tempXml, Path destinationFile) throws TransformerException, IOException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try (InputStream inputStream = xsl.getInputStream()) {
            javax.xml.transform.Transformer transformer = transformerFactory.newTransformer(new StreamSource(inputStream));
            transformer.transform(new StreamSource(tempXml.toFile()), new StreamResult(destinationFile.toFile()));
        }
        logger.info("Result saved to: %1$s".formatted(destinationFile));
    }

    private LineParsed convertToValue(Collection<LineConfig> lineConfigs, String line) {
        LineConfig lineConfig = lineConfigs.stream()
                .filter(config -> Pattern.matches(config.getLineTestRegex(), line))
                .findAny()
                .orElse(getDefaultLineConfig(line));
        return parseLine(line, lineConfig);
    }

    private LineParsed parseLine(String line, LineConfig lineConfig) {
        List<FieldValue> fieldValues = lineConfig.getFields()
                .stream()
                .sorted(Comparator.comparing(FieldConfig::getPositionInLine))
                .map(fieldConfig -> convert(fieldConfig, line))
                .toList();
        return new LineParsed(fieldValues);
    }

    private FieldValue convert(FieldConfig fieldConfig, String line) {
        if (line.length() >= fieldConfig.getPositionInLine() + fieldConfig.getWidth()) {
            return new FieldValue(fieldConfig, line.substring(fieldConfig.getPositionInLine(), fieldConfig.getPositionInLine() + fieldConfig.getWidth()));
        } else if (line.length() >= fieldConfig.getPositionInLine()) {
            return new FieldValue(fieldConfig, line.substring(fieldConfig.getPositionInLine()));
        } else {
            return new FieldValue(fieldConfig, "");
        }
    }

    private LineConfig getDefaultLineConfig(String line) {
        ArrayList<FieldConfig> fieldConfigs = new ArrayList<>(1);
        fieldConfigs.add(new FieldConfig(line.length(), "Not Parsed", 0, 0));
        LineConfig lineConfig = new LineConfig("", fieldConfigs);
        return lineConfig;
    }

}
