package ru.nsu.icg.wireframe.gui.common.parser;

public class FileValidator {
    public static int validateIntValue(int value, int min, int max){
        if (value > max || value < min){
            throw new BadFileException("Integer value not between " + min + " and " + max);
        }
        return value;
    }

    public static double validateDoubleValue(double value, double min, double max){
        if (value > max || value < min){
            throw new BadFileException("Double value not between " + min + " and " + max);
        }
        return value;
    }
}
