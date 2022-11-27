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

    /**
     * Width of field
     */
    private Integer width;

    /**
     * Name of field
     */
    private String name;

    /**
     * Start position of current field in line
     */
    @JsonIgnore
    private Integer positionInLine;

    private BackgroundColors backgroundColor;

    private TextColors textColor;
}
