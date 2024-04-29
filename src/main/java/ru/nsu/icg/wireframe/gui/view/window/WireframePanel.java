package ru.nsu.icg.wireframe.gui.view.window;

import org.ejml.simple.SimpleMatrix;
import ru.nsu.icg.wireframe.gui.common.Context;
import ru.nsu.icg.wireframe.gui.controller.window.WireframePanelController;
import ru.nsu.icg.wireframe.model.WireframeDriver;
import ru.nsu.icg.wireframe.model.WireframePoints;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class WireframePanel extends JPanel {

    private final Context context;

    public WireframePanel(Context context, WireframePanelController wireframePanelController){
        setBackground(Color.WHITE);
        setDoubleBuffered(true);
        this.context = context;
        addMouseListener(wireframePanelController);
        addMouseMotionListener(wireframePanelController);
        addMouseWheelListener(wireframePanelController);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D)g;
        if(context.getSplinePoints() == null || context.getSplinePoints().isEmpty()){
            return;
        }
        WireframePoints wireframePoints = WireframeDriver.convertSplines(context.getSplinePoints(), context.getCountOfPointsInSpline(),
                context.getCountOfPointsInCircle(), context.getCountOfGenerating());
        if (wireframePoints.getGeneratingPointsList() == null){
            return;
        }

        g2D.setColor(Color.GREEN);
        SimpleMatrix normalizeMatrix = WireframeDriver.normalizeWireframePoints(wireframePoints);
        double[][] cameraMatrixRaw = {
                {1, 0, 0, 0},
                {0, 100, 0 , 0},
                {0, 0, 100, 0},
                {1, 0, 0, 10}
        };
        double angleY = Math.toRadians(context.getAngleY());
        double angleX = Math.toRadians(context.getAngleX());
        double[][] rotateMatrixYRaw = {
                {Math.cos(angleY), 0, Math.sin(angleY), 0},
                {0, 1, 0, 0},
                {-Math.sin(angleY), 0, Math.cos(angleY), 0},
                {0, 0, 0, 1}
        };
        double[][] rotateMatrixXRaw = {
                {1, 0, 0, 0},
                {0, Math.cos(angleX), -Math.sin(angleX), 0},
                {0, Math.sin(angleX), Math.cos(angleX), 0},
                {0, 0, 0, 1}
        };
        normalizeMatrix = normalizeMatrix.mult(new SimpleMatrix(rotateMatrixYRaw));
        normalizeMatrix = normalizeMatrix.mult(new SimpleMatrix(rotateMatrixXRaw));
        normalizeMatrix = normalizeMatrix.mult(new SimpleMatrix(cameraMatrixRaw));

        for (List<SimpleMatrix> spline : wireframePoints.getGeneratingPointsList()){
            for (int i = 0; i < spline.size() - 1; i++){
                SimpleMatrix m1 = spline.get(i).transpose().mult(normalizeMatrix);
                SimpleMatrix m2 = spline.get(i+1).transpose().mult(normalizeMatrix);

                g2D.drawLine(getWidth()/2 + (int)Math.round(m1.get(2)), getHeight()/2 + (int)Math.round(m1.get(1)),
                        getWidth()/2 + (int)Math.round(m2.get(2)), getHeight()/2 + (int)Math.round(m2.get(1)));
            }
        }

        for (List<SimpleMatrix> circle : wireframePoints.getCirclesPointsList()){
            for (int i = 0; i < circle.size(); i++){
                SimpleMatrix m1 = circle.get(i).transpose().mult(normalizeMatrix);
                SimpleMatrix m2;
                if (i == circle.size() - 1){
                    m2 = circle.get(0).transpose().mult(normalizeMatrix);
                } else {
                    m2 = circle.get(i + 1).transpose().mult(normalizeMatrix);
                }

                g2D.drawLine(getWidth()/2 + (int)Math.round(m1.get(2)), getHeight()/2 + (int)Math.round(m1.get(1)),
                        getWidth()/2 + (int)Math.round(m2.get(2)), getHeight()/2 + (int)Math.round(m2.get(1)));
            }
        }
    }
}
