package ru.nsu.icg.wireframe.gui.controller.editor;

import ru.nsu.icg.wireframe.gui.common.Context;
import ru.nsu.icg.wireframe.model.DoublePoint2D;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class EditorButtonsController {

    private final Context context;

    public EditorButtonsController(Context context){
        this.context = context;
    }

    public ActionListener onApplyBtnClicked(){
        return e -> System.out.println("APPLY CLICKED");
    }

    public ActionListener onZoomInClicked(){
        return e -> {
            double zoom = context.getZoom();
            if (zoom < 0.3){
                return;
            }
            context.setZoom(zoom - 0.2);
        };
    }

    public ActionListener onZoomOutClicked(){
        return e -> {
            double zoom = context.getZoom();
            if (zoom > 5){
                return;
            }
            context.setZoom(zoom + 0.2);
        };
    }

    public ActionListener onClearClicked(){
        return e -> context.reset();
    }

    public ActionListener onNormalizeClicked(){
        return e -> {
            List<DoublePoint2D> pivotPoints = context.getPivotPoints();
            double uMax = Double.MIN_VALUE;
            double vMax = Double.MIN_VALUE;
            double uMin = Double.MAX_VALUE;
            double vMin = Double.MAX_VALUE;
            for (DoublePoint2D point2D : pivotPoints){
                if(point2D.u > uMax){
                    uMax = point2D.u;
                }
                if(point2D.v > vMax){
                    vMax = point2D.v;
                }
                if(point2D.u < uMin){
                    uMin = point2D.u;
                }
                if(point2D.v < vMin){
                    vMin = point2D.v;
                }

            }

            double width = uMax - uMin;
            double height = vMax - vMin;
            List<DoublePoint2D> newPoints = new ArrayList<>();

            for (DoublePoint2D point2D : pivotPoints) {
                newPoints.add(new DoublePoint2D(point2D.u / width, point2D.v / height));
            }
            context.setZoom(1);
            context.setCenter(new DoublePoint2D(0,0));
            context.setPivotPoints(newPoints);
        };
    }

    public ActionListener onAddPointClicked(){
        return e -> {
            DoublePoint2D point2D = new DoublePoint2D(0, 0);
            context.addSplinePivotPoint(point2D);
        };
    }

    public ActionListener onDeletePointClicked(){
        return e -> {
            int pos = context.getCurrPivotPointPos();
            if (pos == Context.NULL_POS){
                return;
            }
            context.removeSplinePivotPoint(pos);
        };
    }
}
