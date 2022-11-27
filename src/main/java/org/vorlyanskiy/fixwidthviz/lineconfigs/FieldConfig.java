package org.vorlyanskiy.fixwidthviz.lineconfigs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vorlyanskiy.fixwidthviz.config.BackgroundColors;
import org.vorlyanskiy.fixwidthviz.config.TextColors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldConfig {
    private Integer width;

    private String name;

    @JsonIgnore
    private Integer positionInLine;

    private BackgroundColors backgroundColor;

    private TextColors textColor;
}
