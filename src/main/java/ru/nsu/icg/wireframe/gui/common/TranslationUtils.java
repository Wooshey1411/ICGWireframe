package ru.nsu.icg.wireframe.gui.common;

public class TranslationUtils {

    public static double xToU(DoublePoint2D center, int width, int x, double cellSize){
        return (-width * 1.0 / 2 - center.u * cellSize + x) / cellSize;
    }

    public static double yToV(DoublePoint2D center, int height, int y, double cellSize){
        return (height * 1.0 / 2 + center.v * cellSize - y) / cellSize;
    }

    public static int uToX(DoublePoint2D center, int width, double v , double cellSize){
        return (int)Math.round(v * cellSize + width*1.0/2 + center.u * cellSize);
    }

    public static int vToY(DoublePoint2D center, int height, double u , double cellSize){
        return (int)Math.round(-u * cellSize + height*1.0/2 + center.v * cellSize);
    }
}
