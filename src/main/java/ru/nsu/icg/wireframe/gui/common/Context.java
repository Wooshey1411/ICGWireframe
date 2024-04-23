package ru.nsu.icg.wireframe.gui.common;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.List;

public class Context {
    @Getter
    private DoublePoint2D center;

    public final static int CELL_SIZE = 100;
    public final static int NULL_POS = -1;
    public final static int POINT_RADIUS = 13;

    @Getter
    private int countOfPointsInSpline;

    @Getter
    private int currPointPos;

    @Setter
    private EditorListener editorListener;

    private EditorParamsListener editorParamsListener;

    @Getter
    @Setter
    private int editorWidth;

    @Getter
    @Setter
    private int editorHeight;

    @Getter
    private double zoom;

    @Getter
    private final List<DoublePoint2D> pivotPoints;

    @Getter
    private List<DoublePoint2D> splinePoints;

    public Context(){
        center = new DoublePoint2D(0, 0);
        zoom = 1;
        pivotPoints = new ArrayList<>();
        splinePoints = new ArrayList<>();
        currPointPos = NULL_POS;
        countOfPointsInSpline = 1;
    }

    public void changeSplinePoint(int position, DoublePoint2D newPoint){
        DoublePoint2D point = pivotPoints.get(position);
        point.u = newPoint.u;
        point.v = newPoint.v;
        editorParamsListener.onPointPosChange(this);
        buildSplines();
        editorListener.onPointsChange();
    }
    public void removeSplinePoint(int position){
        int newPos = NULL_POS;

        // Оставляет выбранную точку подсвеченной
        if(position > currPointPos){
            newPos = currPointPos;
        }
        if(position < currPointPos){
            newPos = currPointPos - 1;
        }

        pivotPoints.remove(position);

        currPointPos = newPos;

        buildSplines();
        editorParamsListener.onPointPosChange(this);
        editorListener.onPointsChange();
    }

    public void addSplinePoint(DoublePoint2D newPoint){
        pivotPoints.add(newPoint);
        //currPointPos = pivotPoints.size() - 1; // если хотим, чтобы точка новая сразу выбиралась.
        buildSplines();
        editorParamsListener.onPointPosChange(this);
        editorListener.onPointsChange();
    }

    public void setCenter(DoublePoint2D center) {
        this.center = center;
        editorListener.onEditorPositionChange();
    }

    public void setZoom(double zoom){
        if(zoom < 0.18 || zoom > 5.02){
            return;
        }

        this.zoom = zoom;
        editorListener.onEditorPositionChange();
    }

    public void setCurrPointPos(int pos){
        if (pos >= pivotPoints.size()){
            return;
        }
        currPointPos = pos;
        editorParamsListener.onPointPosChange(this);
        editorListener.onPointsChange();
    }

    private static final double[][] M = {
            {-1.0/6, 3.0/6, -3.0/6, 1.0/6},
            {3.0/6, -6.0/6, 3.0/6, 0},
            {-3.0/6, 0, 3.0/6, 0},
            {1.0/6, 4.0/6, 1.0/6, 0}
    };

    private static final double[][] MG = new double[4][2];

    private void buildSplines(){
        if (pivotPoints.size() < 4){
            splinePoints.clear();
            return;
        }

        DoublePoint2D[] pivotPointsArr = pivotPoints.toArray(new DoublePoint2D[0]);
        double[][] G = new double[4][2];
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

        this.splinePoints = splinePoints;

    }

    public void setCountOfPointsInSpline(int countOfPointsInSpline){
        if (countOfPointsInSpline == this.countOfPointsInSpline){
            return;
        }

        this.countOfPointsInSpline = countOfPointsInSpline;
        buildSplines();
        editorListener.onPointsChange();
    }

    public void setEditorParamsListener(EditorParamsListener editorParamsListener){
        this.editorParamsListener = editorParamsListener;
        editorParamsListener.onParamsChange(this);
    }

}
