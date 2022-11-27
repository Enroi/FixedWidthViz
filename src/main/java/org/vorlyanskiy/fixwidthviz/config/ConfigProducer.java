package org.vorlyanskiy.fixwidthviz.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.vorlyanskiy.fixwidthviz.lineconfigs.FieldConfig;
import org.vorlyanskiy.fixwidthviz.lineconfigs.LineConfig;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
public class ConfigProducer {

    @Value("classpath:TransformerConfig.json")
    private Resource resourceFile;

    public Collection<LineConfig> getLineConfigs() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<LineConfig> lineConfigs = objectMapper.readValue(resourceFile.getInputStream(), new TypeReference<>() {
            });
            fillColors(lineConfigs);
            return lineConfigs;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillColors(List<LineConfig> lineConfigs) {
        Map<Integer, LineConfig> mapped = IntStream.range(0, lineConfigs.size())
                .boxed()
                .collect(Collectors.toMap(i -> i, lineConfigs::get));
        mapped.entrySet()
                .forEach(lineConfigEntry -> {
                    List<FieldConfig> fieldConfigs = lineConfigEntry.getValue().getFields();
                    Map<Integer, FieldConfig> mappedConfigs = IntStream.range(0, fieldConfigs.size())
                                    .boxed()
                                    .collect(Collectors.toMap(i -> i, fieldConfigs::get));
                    mappedConfigs.entrySet()
                            .forEach(entryFieldConfig -> {
                                setBackgroundColor(lineConfigEntry, entryFieldConfig);
                                setTextColorForBackgroundColor(entryFieldConfig);
                            });
                });

    }

    private static void setBackgroundColor(Map.Entry<Integer, LineConfig> lineConfigEntry,
                                           Map.Entry<Integer, FieldConfig> entryFieldConfig) {
        int rod = entryFieldConfig.getKey() % 7;
        entryFieldConfig.getValue()
                .setBackgroundColor(BackgroundColors.rainbow(lineConfigEntry.getKey()).get(rod));
    }

    private static void setTextColorForBackgroundColor(Map.Entry<Integer, FieldConfig> entryFieldConfig) {
        TextColors textColor = switch (entryFieldConfig.getValue().getBackgroundColor()) {
            case indigo -> TextColors.white;
            default -> TextColors.black;
        };
        entryFieldConfig.getValue().setTextColor(textColor);
    }
}