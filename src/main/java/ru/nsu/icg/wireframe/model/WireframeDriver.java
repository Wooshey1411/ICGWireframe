package ru.nsu.icg.wireframe.model;

import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class WireframeDriver {

    public static WireframePoints convertSplines(List<DoublePoint2D> splines, int countOfPointsInSpline, int countOfPointsInCircle, int countOfGenerating){
        WireframePoints wireframePoints = new WireframePoints();
        List<List<SimpleMatrix>> generatingPointsList = new ArrayList<>();
        double sectorAngleDeg = 360.0/countOfGenerating;
        double currAngle = 0;
        for (int i = 0; i < countOfGenerating; i++){
            List<SimpleMatrix> points = new ArrayList<>();
            for (DoublePoint2D point2D : splines){
                double angle = Math.toRadians(currAngle);
                double[] p = {
                        point2D.v*Math.sin(angle),
                        point2D.v * Math.cos(angle),
                        point2D.u,
                        1};
                points.add(new SimpleMatrix(p));
            }
            generatingPointsList.add(points);
            currAngle += sectorAngleDeg;
        }
        wireframePoints.setGeneratingPointsList(generatingPointsList);

        sectorAngleDeg /= countOfPointsInCircle;
        List<List<SimpleMatrix>> circlesPointsList = new ArrayList<>();
        for (int i = 0; i < splines.size(); i+=countOfPointsInSpline){
            currAngle = 0;
            List<SimpleMatrix> points = new ArrayList<>();
            for (int j = 0; j < countOfGenerating*countOfPointsInCircle; j++){
                double[] p = {
                        splines.get(i).v * Math.sin(Math.toRadians(currAngle)),
                        splines.get(i).v * Math.cos(Math.toRadians(currAngle)),
                        splines.get(i).u,
                        1};
                points.add(new SimpleMatrix(p));
                currAngle += sectorAngleDeg;
            }
            circlesPointsList.add(points);
        }

        wireframePoints.setCirclesPointsList(circlesPointsList);

        return wireframePoints;
    }

    public static SimpleMatrix normalizeWireframePoints(WireframePoints wireframePoints){
        List<List<SimpleMatrix>> generatingPointsList = wireframePoints.getGeneratingPointsList();
        final double[] xMin = {Double.MAX_VALUE};
        final double[] yMin = {Double.MAX_VALUE};
        final double[] zMin = {Double.MAX_VALUE};
        final double[] xMax = {Double.MIN_VALUE};
        final double[] yMax = {Double.MIN_VALUE};
        final double[] zMax = {Double.MIN_VALUE};

        Stream.concat(generatingPointsList.stream().flatMap(List::stream),
                        wireframePoints.getCirclesPointsList().stream().flatMap(List::stream))
                .forEach(simpleMatrix -> {
                    if (simpleMatrix.get(0) > xMax[0]){
                        xMax[0] = simpleMatrix.get(0);
                    }
                    if (simpleMatrix.get(1) > yMax[0]){
                        yMax[0] = simpleMatrix.get(1);
                    }
                    if (simpleMatrix.get(2) > zMax[0]){
                        zMax[0] = simpleMatrix.get(2);
                    }
                    if (simpleMatrix.get(0) < xMin[0]){
                        xMin[0] = simpleMatrix.get(0);
                    }
                    if (simpleMatrix.get(1) < yMin[0]){
                        yMin[0] = simpleMatrix.get(1);
                    }
                    if (simpleMatrix.get(2) < zMin[0]){
                        zMin[0] = simpleMatrix.get(2);
                    }
                });

        double cubeEdgeLen = Math.max(Math.max(xMax[0] - xMin[0], yMax[0] - yMin[0]), zMax[0] - zMin[0]) / 2;

        double[][] matrix =  {
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {-(xMax[0] + xMin[0]) / 2, -(yMax[0] + yMin[0]) / 2, -(zMax[0] + zMin[0]) / 2, 1},
        };
        SimpleMatrix shiftMatrix = new SimpleMatrix(matrix);
        matrix = new double[][]{
                {1 / cubeEdgeLen, 0, 0, 0},
                {0, 1 / cubeEdgeLen, 0, 0},
                {0, 0, 1 / cubeEdgeLen, 0},
                {0, 0, 0, 1},
        };

        SimpleMatrix normMatrix = new SimpleMatrix(matrix);
        shiftMatrix = shiftMatrix.mult(normMatrix);
        return shiftMatrix;
    }

}
