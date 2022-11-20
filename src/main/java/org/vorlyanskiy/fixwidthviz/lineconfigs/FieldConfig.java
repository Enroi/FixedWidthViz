package org.vorlyanskiy.fixwidthviz.lineconfigs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldConfig {
    private Integer width;

    private String name;

    private Integer position;

    @JsonIgnore
    private Integer positionInLine;
}
