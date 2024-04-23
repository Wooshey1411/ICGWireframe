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
    private int countOfPointsInCircle;
    @Getter
    private int countOfGenerating;

    @Getter
    private int currPivotPointPos;

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

    @Getter
    private int splinesColorR;
    @Getter
    private int splinesColorG;
    @Getter
    private int splinesColorB;

    public Context(){
        center = new DoublePoint2D(0, 0);
        zoom = 1;
        pivotPoints = new ArrayList<>();
        splinePoints = null;
        currPivotPointPos = NULL_POS;
        countOfPointsInSpline = 1;
        splinesColorR = 255;
        splinesColorG = 255;
        splinesColorB = 255;
        countOfPointsInCircle = 1;
        countOfGenerating = 2;
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
        if(position > currPivotPointPos){
            newPos = currPivotPointPos;
        }
        if(position < currPivotPointPos){
            newPos = currPivotPointPos - 1;
        }

        pivotPoints.remove(position);

        currPivotPointPos = newPos;

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

    public void setCurrPivotPointPos(int pos){
        if (pos >= pivotPoints.size()){
            return;
        }
        currPivotPointPos = pos;
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

    public void setSplinesColorR(int r){
        if (splinesColorR == r){
            return;
        }

        this.splinesColorR = r;
        editorListener.onPointsChange();
    }
    public void setSplinesColorG(int g){
        if (splinesColorG == g){
            return;
        }

        this.splinesColorG = g;
        editorListener.onPointsChange();
    }

    public void setSplinesColorB(int b){
        if (splinesColorB == b){
            return;
        }

        this.splinesColorB = b;
        editorListener.onPointsChange();
    }

    public void setCountOfPointsInCircle(int countOfPointsInCircle){
        if (countOfPointsInCircle == this.countOfPointsInCircle){
            return;
        }

        this.countOfPointsInCircle = countOfPointsInCircle;
    }

    public void setCountOfGenerating(int countOfGenerating){
        if (countOfGenerating == this.countOfGenerating){
            return;
        }

        this.countOfGenerating = countOfGenerating;
    }

    public void setEditorParamsListener(EditorParamsListener editorParamsListener){
        this.editorParamsListener = editorParamsListener;
        editorParamsListener.onParamsChange(this);
    }

}
