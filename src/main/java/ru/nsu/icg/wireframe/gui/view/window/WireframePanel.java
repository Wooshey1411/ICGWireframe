package ru.nsu.icg.wireframe.gui.view.window;

import org.ejml.simple.SimpleMatrix;
import ru.nsu.icg.wireframe.gui.common.Context;
import ru.nsu.icg.wireframe.gui.controller.window.WireframePanelController;
import ru.nsu.icg.wireframe.model.WireframeDriver;
import ru.nsu.icg.wireframe.model.WireframePoints;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class WireframePanel extends JPanel {

    private final Context context;

    public WireframePanel(Context context, WireframePanelController wireframePanelController){
        setBackground(Color.BLACK);
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

        g2D.setColor(new Color(context.getSplinesColorR(), context.getSplinesColorG(), context.getSplinesColorB()));
        SimpleMatrix normalizeMatrix = WireframeDriver.normalizeWireframePoints(wireframePoints);
        double[][] cameraMatrixRaw = {
                {1, 0, 0, 0},
                {0, context.getWireframePos(), 0 , 0},
                {0, 0, context.getWireframePos(), 0},
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
        /*double[][] rotateMatrixXRaw = {
                {1, 0, 0, 0},
                {0, Math.cos(angleX), -Math.sin(angleX), 0},
                {0, Math.sin(angleX), Math.cos(angleX), 0},
                {0, 0, 0, 1}
        };*/

        double[][] rotateMatrixXRaw = {
                {Math.cos(angleX), -Math.sin(angleX), 0, 0},
                {Math.sin(angleX), Math.cos(angleX), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        normalizeMatrix = normalizeMatrix.mult(new SimpleMatrix(rotateMatrixYRaw));
        normalizeMatrix = normalizeMatrix.mult(new SimpleMatrix(rotateMatrixXRaw));
        normalizeMatrix = normalizeMatrix.mult(new SimpleMatrix(cameraMatrixRaw).transpose());


        for (List<SimpleMatrix> spline : wireframePoints.getGeneratingPointsList()){
            for (int i = 0; i < spline.size() - 1; i++){
                SimpleMatrix m1 = spline.get(i).transpose().mult(normalizeMatrix);
                SimpleMatrix m2 = spline.get(i+1).transpose().mult(normalizeMatrix);
                g2D.setColor(getColor(m1.get(0), m2.get(0), context.getSplinesColorR(), context.getSplinesColorG(), context.getSplinesColorB()));
                g2D.drawLine(getWidth()/2 + (int)Math.round(m1.get(2) / m1.get(3)), getHeight()/2 + (int)Math.round(m1.get(1) / m1.get(3)),
                        getWidth()/2 + (int)Math.round(m2.get(2) / m2.get(3)), getHeight()/2 + (int)Math.round(m2.get(1) / m2.get(3)));
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
                g2D.setColor(getColor(m1.get(0), m2.get(0), context.getSplinesColorR(), context.getSplinesColorG(), context.getSplinesColorB()));
                g2D.drawLine(getWidth()/2 + (int)Math.round(m1.get(2) / m1.get(3)), getHeight()/2 + (int)Math.round(m1.get(1) / m1.get(3)),
                        getWidth()/2 + (int)Math.round(m2.get(2) / m2.get(3)), getHeight()/2 + (int)Math.round(m2.get(1) / m2.get(3)));
            }
        }

        g2D.setColor(Color.GREEN);
        double[] matrix = {80, 80, 80, 1};
        SimpleMatrix center = new SimpleMatrix(matrix);
        matrix = new double[]{60, 0, 0, 1};
        SimpleMatrix axisX = new SimpleMatrix(matrix);
        matrix = new double[]{0, -60, 0, 1};
        SimpleMatrix axisY = new SimpleMatrix(matrix);
        matrix = new double[]{0, 0, 60, 1};
        SimpleMatrix axisZ = new SimpleMatrix(matrix);

        SimpleMatrix rotate = new SimpleMatrix(rotateMatrixXRaw).mult(new SimpleMatrix(rotateMatrixYRaw));
        axisX = axisX.transpose().mult(rotate);
        axisY = axisY.transpose().mult(rotate);
        axisZ = axisZ.transpose().mult(rotate);

        g2D.drawLine((int)Math.round(center.get(2)), (int)Math.round(center.get(1)),
                (int)Math.round(center.get(2)) + (int)Math.round(axisX.get(2)), (int)Math.round(center.get(1) + (int)Math.round(axisX.get(1))));

        g2D.drawString("x", (int)Math.round(center.get(2)) + (int)Math.round(axisX.get(2))+5,
                (int)Math.round(center.get(1) + (int)Math.round(axisX.get(1))) - 5);

        g2D.setColor(Color.BLUE);

        g2D.drawLine((int)Math.round(center.get(2)), (int)Math.round(center.get(1)),
                (int)Math.round(center.get(2)) + (int)Math.round(axisY.get(2)), (int)Math.round(center.get(1) + (int)Math.round(axisY.get(1))));


        g2D.drawString("y", (int)Math.round(center.get(2)) + (int)Math.round(axisY.get(2))+5,
                (int)Math.round(center.get(1) + (int)Math.round(axisY.get(1))) - 5);

        g2D.setColor(Color.RED);

        g2D.drawLine((int)Math.round(center.get(2)), (int)Math.round(center.get(1)),
                (int)Math.round(center.get(2)) + (int)Math.round(axisZ.get(2)), (int)Math.round(center.get(1) + (int)Math.round(axisZ.get(1))));

        g2D.drawString("z", (int)Math.round(center.get(2)) + (int)Math.round(axisZ.get(2))+5,
                (int)Math.round(center.get(1) + (int)Math.round(axisZ.get(1))) - 5);
    }

    private Color getColor(double x1, double x2, int r, int g, int b){
        double coeff = ((x1 + x2) - 1) / 2;
        if (coeff > 1){
            return new Color((int)Math.round(0.5 * r), (int)Math.round(0.5 * g), (int)Math.round(0.5 * b));
        }
        if (coeff < -1){
            return new Color(r, g, b);
        }

        coeff = -0.35 * coeff + 0.65;

        return new Color((int)Math.round(coeff * r), (int)Math.round(coeff * g), (int)Math.round(coeff * b));

    }

}
