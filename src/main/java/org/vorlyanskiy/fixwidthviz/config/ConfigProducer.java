package org.vorlyanskiy.fixwidthviz.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.vorlyanskiy.fixwidthviz.lineconfigs.LineConfig;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

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
            return lineConfigs;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}