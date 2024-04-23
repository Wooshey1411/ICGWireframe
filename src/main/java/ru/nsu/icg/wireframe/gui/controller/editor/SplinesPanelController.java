package ru.nsu.icg.wireframe.gui.controller.editor;

import ru.nsu.icg.wireframe.gui.common.Context;
import ru.nsu.icg.wireframe.gui.common.DoublePoint2D;
import ru.nsu.icg.wireframe.gui.common.TranslationUtils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class SplinesPanelController extends MouseAdapter {

    private final Context context;
    private int prevX;
    private int prevY;

    private double prevV;
    private double prevU;
    private boolean isPlaneMoving;
    private boolean isPointMoving;

    public SplinesPanelController(Context context){
        this.context = context;
        isPlaneMoving = false;
        isPointMoving = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if (e.getButton() == MouseEvent.BUTTON3) {
            DoublePoint2D center = context.getCenter();
            double cellSize = Context.CELL_SIZE / context.getZoom();
            int pos = findPositionOnPointByClick(e.getX(), e.getY());
            if(pos != Context.NULL_POS){
                context.removeSplinePoint(pos);
                return;
            }
            context.addSplinePoint(new DoublePoint2D(TranslationUtils.xToU(center, context.getEditorWidth(), e.getX(), cellSize), TranslationUtils.yToV(center, context.getEditorHeight(), e.getY(), cellSize)));
            return;
        }
        if (e.getButton() == MouseEvent.BUTTON1){
            int pos = findPositionOnPointByClick(e.getX(), e.getY());
            if (pos != Context.NULL_POS){
                context.setCurrPointPos(pos);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        if (e.getButton() == MouseEvent.BUTTON1) {
            double cellSize = Context.CELL_SIZE / context.getZoom();
            DoublePoint2D center = context.getCenter();
            prevV = TranslationUtils.xToU(center, context.getEditorWidth(), e.getX(), cellSize);
            prevU = TranslationUtils.yToV(center, context.getEditorHeight(), e.getY(), cellSize);

            int pos = findPositionOnPointByClick(e.getX(), e.getY());
            if (pos != Context.NULL_POS){
                context.setCurrPointPos(pos);
                isPointMoving = true;
                return;
            }
            isPlaneMoving = true;
            prevX = e.getX();
            prevY = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        isPlaneMoving = false;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        super.mouseWheelMoved(e);
        double coeff = e.getScrollAmount() * 0.05;
        if (e.getWheelRotation() < 0){
            coeff = -coeff;
        }

        context.setZoom(context.getZoom() + coeff);

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        if (isPlaneMoving) {
            DoublePoint2D center = context.getCenter();
            double cellSize = Context.CELL_SIZE / context.getZoom();
            center.u += ((e.getX() - prevX) / cellSize);
            center.v += ((e.getY() - prevY) / cellSize);
            prevX = e.getX();
            prevY = e.getY();
            context.setCenter(center);
            return;
        }
        if (isPointMoving){
            double cellSize = Context.CELL_SIZE / context.getZoom();
            DoublePoint2D center = context.getCenter();
            double v = TranslationUtils.xToU(center, context.getEditorWidth(), e.getX(), cellSize);
            double u = TranslationUtils.yToV(center, context.getEditorHeight(), e.getY(), cellSize);
            double deltaV = v - prevV;
            double deltaU = u - prevU;
            prevV = v;
            prevU = u;
            DoublePoint2D point2D = context.getPivotPoints().get(context.getCurrPointPos());
            point2D.u += deltaV;
            point2D.v += deltaU;
            context.changeSplinePoint(context.getCurrPointPos(), point2D);
        }

    }

    private int findPositionOnPointByClick(int x, int y) {
        double cellSize = Context.CELL_SIZE / context.getZoom();
        DoublePoint2D center = context.getCenter();
        double v = TranslationUtils.xToU(center, context.getEditorWidth(), x, cellSize);
        double u = TranslationUtils.yToV(center, context.getEditorHeight(), y, cellSize);
        double uvDistance = Context.POINT_RADIUS * Context.POINT_RADIUS / cellSize / cellSize;
        int pos = Context.NULL_POS;
        int calcPos = 0;
        for (DoublePoint2D point2D : context.getPivotPoints()) {
            double distance = (point2D.u - v) * (point2D.u - v) + (point2D.v - u) * (point2D.v - u);
            if (distance <= uvDistance) {
                pos = calcPos;
            }
            calcPos++;
        }
        return pos;
    }
}
