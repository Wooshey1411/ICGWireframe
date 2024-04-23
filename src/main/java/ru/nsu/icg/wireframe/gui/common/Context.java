package ru.nsu.icg.wireframe.gui.common;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.icg.wireframe.model.BSplinesDriver;
import ru.nsu.icg.wireframe.model.DoublePoint2D;

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
        splinePoints = null;
        currPointPos = NULL_POS;
        countOfPointsInSpline = 1;
    }

    public void changeSplinePoint(int position, DoublePoint2D newPoint){
        DoublePoint2D point = pivotPoints.get(position);
        point.u = newPoint.u;
        point.v = newPoint.v;
        editorParamsListener.onPointPosChange(this);
        splinePoints = BSplinesDriver.buildSplines(pivotPoints, countOfPointsInSpline);
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

        splinePoints = BSplinesDriver.buildSplines(pivotPoints, countOfPointsInSpline);
        editorParamsListener.onPointPosChange(this);
        editorListener.onPointsChange();
    }

    public void addSplinePoint(DoublePoint2D newPoint){
        pivotPoints.add(newPoint);
        //currPointPos = pivotPoints.size() - 1; // если хотим, чтобы точка новая сразу выбиралась.
        splinePoints = BSplinesDriver.buildSplines(pivotPoints, countOfPointsInSpline);
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


    public void setCountOfPointsInSpline(int countOfPointsInSpline){
        if (countOfPointsInSpline == this.countOfPointsInSpline){
            return;
        }

        this.countOfPointsInSpline = countOfPointsInSpline;
        splinePoints = BSplinesDriver.buildSplines(pivotPoints, countOfPointsInSpline);
        editorListener.onPointsChange();
    }

    public void setEditorParamsListener(EditorParamsListener editorParamsListener){
        this.editorParamsListener = editorParamsListener;
        editorParamsListener.onParamsChange(this);
    }

}
