package org.vorlyanskiy.fixwidthviz.lineconfigs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LineConfig {

    @JsonProperty("line_test_regex")
    private String lineTestRegex;

    @JsonProperty("fields")
    private List<FieldConfig> fields;

    public List<FieldConfig> getFields() {
        if (fields.size() > 0 && fields.get(0).getPositionInLine() == null) {
            int currentPosition = 0;
            for (FieldConfig fieldConfig : fields) {
                fieldConfig.setPositionInLine(currentPosition);
                currentPosition += fieldConfig.getWidth();
            }
        }
        return fields;
    }
}
