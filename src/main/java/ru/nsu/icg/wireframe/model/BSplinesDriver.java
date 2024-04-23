package ru.nsu.icg.wireframe.model;

import java.util.ArrayList;
import java.util.List;

public class BSplinesDriver {
    private static final double[][] M = {
            {-1.0/6, 3.0/6, -3.0/6, 1.0/6},
            {3.0/6, -6.0/6, 3.0/6, 0},
            {-3.0/6, 0, 3.0/6, 0},
            {1.0/6, 4.0/6, 1.0/6, 0}
    };

    public static List<DoublePoint2D> buildSplines(List<DoublePoint2D> pivotPoints, int countOfPointsInSpline){
        if (pivotPoints.size() < 4){
            return new ArrayList<>();
        }

        DoublePoint2D[] pivotPointsArr = pivotPoints.toArray(new DoublePoint2D[0]);
        double[][] G = new double[4][2];
        double[][] MG = new double[4][2];
        List<DoublePoint2D> splinePoints = new ArrayList<>();
        for (int i = 1; i < pivotPointsArr.length - 2; i++){
            G[0][0] = pivotPointsArr[i-1].v;
            G[0][1] = pivotPointsArr[i-1].u;
            G[1][0] = pivotPointsArr[i].v;
            G[1][1] = pivotPointsArr[i].u;
            G[2][0] = pivotPointsArr[i+1].v;
            G[2][1] = pivotPointsArr[i+1].u;
            G[3][0] = pivotPointsArr[i+2].v;
            G[3][1] = pivotPointsArr[i+2].u;
            for (int q = 0; q < 4; q++){
                for (int w = 0; w < 2; w++){
                    MG[q][w] = 0;
                    for (int e = 0; e < 4; e++){
                        MG[q][w] += G[e][w] * M[q][e];
                    }
                }
            }

            for (int k = 0; k < countOfPointsInSpline+1; k++){
                double t = k*1.0 / countOfPointsInSpline;
                double u = t*t*t*MG[0][1] + t*t*MG[1][1] + t*MG[2][1] + MG[3][1];
                double v = t*t*t*MG[0][0] + t*t*MG[1][0] + t*MG[2][0] + MG[3][0];
                splinePoints.add(new DoublePoint2D(u,v));
            }

        }
        return splinePoints;
    }
}
