package ru.nsu.icg.wireframe.gui.common;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import ru.nsu.icg.wireframe.model.BSplinesDriver;
import ru.nsu.icg.wireframe.model.DoublePoint2D;

import java.io.*;
import java.nio.charset.StandardCharsets;
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

    private EditorListener editorListener;

    public void setEditorListener(EditorListener editorListener) {
        this.editorListener = editorListener;
        editorListener.onPointsChange();
        editorListener.onEditorPositionChange();
    }

    @Getter
    private WireframeListener wireframeListener;

    public void setWireframeListener(WireframeListener wireframeListener) {
        this.wireframeListener = wireframeListener;
        wireframeListener.onPointsChange();
    }

    private EditorParamsListener editorParamsListener;

    @Getter
    private int wireframePos;

    @Getter
    @Setter
    private int editorWidth;

    @Getter
    @Setter
    private int editorHeight;

    @Getter
    private double zoom;

    @Getter
    private List<DoublePoint2D> pivotPoints;

    @Getter
    private List<DoublePoint2D> splinePoints;

    @Getter
    private int splinesColorR;
    @Getter
    private int splinesColorG;
    @Getter
    private int splinesColorB;

    @Getter
    private double angleX;

    @Getter
    private double angleY;

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
        angleX = 0;
        angleY = 0;
        wireframePos = 200;

        setInitialModel();

    }

    public void changeSplinePivotPoint(int position, DoublePoint2D newPoint){
        DoublePoint2D point = pivotPoints.get(position);
        point.u = newPoint.u;
        point.v = newPoint.v;
        editorParamsListener.onPointPosChange(this);
        splinePoints = BSplinesDriver.buildSplines(pivotPoints, countOfPointsInSpline);
        editorListener.onPointsChange();
    }
    public void removeSplinePivotPoint(int position){
        int newPos = NULL_POS;
        if (currPivotPointPos != NULL_POS) {
            // Оставляет выбранную точку подсвеченной
            if (position > currPivotPointPos) {
                newPos = currPivotPointPos;
            }
            if (position <= currPivotPointPos) {
                newPos = currPivotPointPos - 1;
            }

            currPivotPointPos = newPos;
        }

        pivotPoints.remove(position);



        splinePoints = BSplinesDriver.buildSplines(pivotPoints, countOfPointsInSpline);
        editorParamsListener.onPointPosChange(this);
        editorListener.onPointsChange();
    }

    public void addSplinePivotPoint(DoublePoint2D newPoint){
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

    public void setPivotPoints(List<DoublePoint2D> points){
        this.pivotPoints = points;
        splinePoints = BSplinesDriver.buildSplines(pivotPoints, countOfPointsInSpline);
        editorParamsListener.onPointPosChange(this);
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

    public void reset(){
        pivotPoints.clear();
        if(splinePoints != null) {
            splinePoints.clear();
        }
        center.u = 0;
        center.v = 0;
        zoom = 1;
        currPivotPointPos = NULL_POS;
        editorListener.onPointsChange();
        editorParamsListener.onPointPosChange(this);
    }

    public void setAngleX(double angleX) {
        this.angleX = angleX;
        wireframeListener.onPointsChange();
    }

    public void setAngleY(double angleY) {
        this.angleY = angleY;
        wireframeListener.onPointsChange();
    }

    public void resetAngles(){
        this.angleX = 0;
        this.angleY = 0;
        wireframeListener.onPointsChange();
    }

    public void setWireframePos(int wireframePos){
        if (wireframePos < 20 || wireframePos == this.wireframePos){
            return;
        }
        this.wireframePos = wireframePos;
        wireframeListener.onPointsChange();
    }

    public SettingsDTO getSettingsDTO(){
        return new SettingsDTO(countOfPointsInSpline,
                countOfPointsInCircle,
                countOfGenerating,
                currPivotPointPos,
                center,
                wireframePos,
                zoom,
                pivotPoints,
                splinesColorR,
                splinesColorG,
                splinesColorB,
                angleX,
                angleY);

    }

    public void setSettingsAndNotify(SettingsDTO settingsDTO){
        setSettings(settingsDTO);
        editorListener.onEditorPositionChange();
        editorListener.onPointsChange();
        editorParamsListener.onPointPosChange(this);
        editorParamsListener.onParamsChange(this);
        wireframeListener.onPointsChange();
    }

    private void setSettings(SettingsDTO settingsDTO){
        this.countOfPointsInSpline = settingsDTO.countOfPointsInSpline();
        this.countOfPointsInCircle = settingsDTO.countOfPointsInCircle();
        this.countOfGenerating = settingsDTO.countOfGenerating();
        this.currPivotPointPos = settingsDTO.currPivotPointPos();
        this.center = settingsDTO.center();
        this.wireframePos = settingsDTO.wireframePos();
        this.zoom = settingsDTO.zoom();
        this.pivotPoints = settingsDTO.pivotPoints();
        this.splinesColorR = settingsDTO.splinesColorR();
        this.splinesColorG = settingsDTO.splinesColorG();
        this.splinesColorB = settingsDTO.splinesColorB();
        this.angleX = settingsDTO.angleX();
        this.angleY = settingsDTO.angleY();
        this.splinePoints = BSplinesDriver.buildSplines(pivotPoints, countOfPointsInSpline);
    }

    private void setInitialModel(){
        try(InputStream reader = Context.class.getResourceAsStream("initial_model.wfm")){
            if (reader == null){
                throw new IOException("No file");
            }

            byte[] bytes = reader.readAllBytes();
            String json = new String(bytes, StandardCharsets.UTF_8);
            Gson gson = new Gson();
            SettingsDTO settingsDTO = gson.fromJson(json, SettingsDTO.class);
            setSettings(settingsDTO);
        } catch (IOException ex){
            System.out.println("No initial wireframe model " + ex.getLocalizedMessage());
            System.exit(0);
        }

    }

}
