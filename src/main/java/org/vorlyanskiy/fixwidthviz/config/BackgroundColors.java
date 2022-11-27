package org.vorlyanskiy.fixwidthviz.config;

import java.util.ArrayList;
import java.util.List;

public enum BackgroundColors {
    violet,
    indigo,
    lightblue,
    lightgreen,
    lightyellow,
    orangered,
    red,
    white;

    public static List<BackgroundColors> rainbow(int colorSwitch) {
        List<BackgroundColors> rainbow = new ArrayList<>(7);
        int selectionColorIndex = colorSwitch % 7;
        for(int i = 0; i < 7; i++) {
            BackgroundColors colorForAdd = switch (selectionColorIndex) {
                case 0 -> violet;
                case 1 -> indigo;
                case 2 -> lightblue;
                case 3 -> lightgreen;
                case 4 -> lightyellow;
                case 5 -> orangered;
                case 6 -> red;
                default -> throw new RuntimeException("Impossible Color index for Rainbow: %1$d".formatted(selectionColorIndex));
            };
            rainbow.add(i, colorForAdd);
            selectionColorIndex++;
            if (selectionColorIndex > 6) {
                selectionColorIndex = 0;
            }
        }
        return rainbow;
    }
}
