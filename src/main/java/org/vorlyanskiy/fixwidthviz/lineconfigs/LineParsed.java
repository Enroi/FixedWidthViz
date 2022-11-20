package org.vorlyanskiy.fixwidthviz.lineconfigs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
public class LineParsed {

    private Collection<FieldValue> fieldValues;
}
