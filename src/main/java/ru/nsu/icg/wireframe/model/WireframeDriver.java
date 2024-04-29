package ru.nsu.icg.wireframe.model;

import lombok.Getter;
import lombok.Setter;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.List;

public class WireframeDriver {

    public static WireframePoints convertSplines(List<DoublePoint2D> splines, int countOfPointsInSpline, int countOfPointsInCircle, int countOfGenerating){
        WireframePoints wireframePoints = new WireframePoints();
        List<List<SimpleMatrix>> generatingPointsList = new ArrayList<>();
        double sectorAngleDeg = 360.0/countOfGenerating;
        double currAngle = sectorAngleDeg;
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
        System.out.println(splines.size());
        for (int i = 0; i < splines.size(); i+=1){
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
        double xMin = Double.MAX_VALUE;
        double yMin = Double.MAX_VALUE;
        double zMin = Double.MAX_VALUE;
        double xMax = Double.MIN_VALUE;
        double yMax = Double.MIN_VALUE;
        double zMax = Double.MIN_VALUE;
        for (List<SimpleMatrix> generating : generatingPointsList){
            for (SimpleMatrix point : generating){
                if (point.get(0) > xMax){
                    xMax = point.get(0);
                }
                if (point.get(1) > yMax){
                    yMax = point.get(1);
                }
                if (point.get(2) > zMax){
                    zMax = point.get(2);
                }
                if (point.get(0) < xMin){
                    xMin = point.get(0);
                }
                if (point.get(1) < yMin){
                    yMin = point.get(1);
                }
                if (point.get(2) < zMin){
                    zMin = point.get(2);
                }
            }
        }

        for (List<SimpleMatrix> generating : wireframePoints.getCirclesPointsList()){
            for (SimpleMatrix point : generating){
                if (point.get(0) > xMax){
                    xMax = point.get(0);
                }
                if (point.get(1) > yMax){
                    yMax = point.get(1);
                }
                if (point.get(2) > zMax){
                    zMax = point.get(2);
                }
                if (point.get(0) < xMin){
                    xMin = point.get(0);
                }
                if (point.get(1) < yMin){
                    yMin = point.get(1);
                }
                if (point.get(2) < zMin){
                    zMin = point.get(2);
                }
            }
        }



        double cubeEdgeLen = Math.max(Math.max(xMax - xMin, yMax - yMin), zMax - zMin) / 2;
        double[][] matrix =  {
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {-(xMax + xMin) / 2, -(yMax + yMin) / 2, -(zMax + zMin) / 2, 1},
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
