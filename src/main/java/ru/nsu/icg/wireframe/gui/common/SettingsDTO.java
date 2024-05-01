package ru.nsu.icg.wireframe.gui.common;

import ru.nsu.icg.wireframe.model.DoublePoint2D;

import java.util.List;

public record SettingsDTO(
        int countOfPointsInSpline,
        int countOfPointsInCircle,
        int countOfGenerating,
        int currPivotPointPos,
        DoublePoint2D center,
        int wireframePos,
        double zoom,
        List<DoublePoint2D> pivotPoints,
        int splinesColorR,
        int splinesColorG,
        int splinesColorB,
        double angleX,
        double angleY
)
{}
