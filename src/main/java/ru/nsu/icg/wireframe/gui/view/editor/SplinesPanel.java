package ru.nsu.icg.wireframe.gui.view.editor;

import ru.nsu.icg.wireframe.gui.common.Context;
import ru.nsu.icg.wireframe.gui.common.EditorListener;
import ru.nsu.icg.wireframe.gui.controller.editor.SplinesPanelController;
import ru.nsu.icg.wireframe.model.DoublePoint2D;
import ru.nsu.icg.wireframe.model.TranslationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class SplinesPanel extends JPanel implements EditorListener {

    private final int basicCellSize;
    private final static Color AXES_COLOR = Color.WHITE;
    private final static Color GRID_COLOR = Color.GRAY;

    private final static Color POINT_COLOR = Color.RED;
    private final static int NUMBERS_SHIFT = 5;

    private final Context context;

    public SplinesPanel(Context context){
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        this.context = context;
        this.basicCellSize = Context.CELL_SIZE;
        context.setEditorListener(this);
        SplinesPanelController splinesPanelController = new SplinesPanelController(context);
        splinesPanelController.setPanel(this);
        addMouseListener(splinesPanelController);
        addMouseMotionListener(splinesPanelController);
        addMouseWheelListener(splinesPanelController);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                context.setEditorHeight(getHeight());
                context.setEditorWidth(getWidth());
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_DELETE){
                    int pos = context.getCurrPivotPointPos();
                    if (pos != -1) {
                        context.removeSplinePivotPoint(pos);
                    }
                }
            }
        });
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        DoublePoint2D currentCenter = context.getCenter();
        double zoom = context.getZoom();

        Graphics2D g2D = (Graphics2D)g;
        drawAxesWithGrid(currentCenter, zoom, g2D);
        drawPoints(context.getPivotPoints(), context.getCenter(), context.getZoom(), context.getCurrPivotPointPos(), g2D);
        drawSplines(context.getSplinePoints(), context.getCenter(), context.getZoom(), g2D);
    }

    private void drawAxesWithGrid(DoublePoint2D center, double zoom, Graphics2D g2D){
        int width = getWidth();
        int height = getHeight();

        double cellSizeR = (int)Math.round(basicCellSize / zoom);

        int yAxisPosX = (int)Math.round(width*1.0/2 + center.u * cellSizeR);
        int xAxisPosY = (int)Math.round(height*1.0/2 + center.v * cellSizeR);

        int cellSize = (int)Math.round(cellSizeR);

        g2D.setStroke(new BasicStroke(1));
        g2D.setColor(GRID_COLOR);

        // vertical lines
        int shiftedXAxisPos = yAxisPosX % cellSize;
        int lastPosX = shiftedXAxisPos;
        for (int x = shiftedXAxisPos + cellSize ; x < width; x+= cellSize){
            g2D.drawLine(x, 0, x, getHeight());
            lastPosX = x;
        }
        for (int x = shiftedXAxisPos; x > 0; x-= cellSize){
            g2D.drawLine(x, 0, x, getHeight());
        }

        // horizontal lines
        int shiftedYAxisPos = xAxisPosY % cellSize;
        int lastPosY = shiftedYAxisPos;
        for (int y = shiftedYAxisPos + cellSize ; y < height; y+= cellSize){
            g2D.drawLine(0, y, width, y);
        }
        for (int y = shiftedYAxisPos; y > 0; y-= cellSize){
            g2D.drawLine(0, y, width, y);
            lastPosY = y;
        }

        // draw axes
        g2D.setColor(AXES_COLOR);

        g2D.drawLine(0, xAxisPosY, width, xAxisPosY); // X
        g2D.drawLine(yAxisPosX, 0, yAxisPosX, height); // Y

        // draw axes values
        int tmp = (int)Math.round(width*1.0/2 - center.u * cellSizeR);

        int pos = tmp / cellSize;
        if(tmp % cellSize == 0 || tmp < 0){ // Почему? ХЗ. скорее всего из-за приколом с остатком от отрицательного числа, хотя по вертикалям всё работает
            pos--;
        }
        for (int x = lastPosX; x > 0; x-= cellSize){
            g2D.drawString(Integer.toString(pos), x + NUMBERS_SHIFT, xAxisPosY - NUMBERS_SHIFT);
            pos--;
        }
        tmp = (int)Math.round(height*1.0/2 + center.v * cellSize);
        pos = tmp / cellSize;

        for (int y = lastPosY; y < height; y+=cellSize){
            g2D.drawString(Integer.toString(pos), yAxisPosX + NUMBERS_SHIFT, y - NUMBERS_SHIFT);
            pos--;
        }

    }

    private void drawPoints(List<DoublePoint2D> points, DoublePoint2D center, double zoom, int currPointPos, Graphics2D g2D){
        if(points.isEmpty()){
            return;
        }

        g2D.setColor(POINT_COLOR);

        double cellSize = basicCellSize / zoom;
        int pointPos = 0;

        int prevX = 0;
        int prevY = 0;
        boolean isFirstPoint = true;

        for(DoublePoint2D point2D : points){
            int x = TranslationUtils.uToX(center, getWidth(), point2D.u, cellSize);
            int y = TranslationUtils.vToY(center, getHeight(), point2D.v, cellSize);
            if(pointPos == currPointPos){
                g2D.setColor(Color.GREEN);
                g2D.drawRect(x,y,1,1);
                g2D.drawOval(x - Context.POINT_RADIUS,y - Context.POINT_RADIUS, Context.POINT_RADIUS * 2, Context.POINT_RADIUS * 2);
                g2D.setColor(POINT_COLOR);
            } else {
                g2D.drawRect(x, y, 1, 1);
                g2D.drawOval(x - Context.POINT_RADIUS, y - Context.POINT_RADIUS, Context.POINT_RADIUS * 2, Context.POINT_RADIUS * 2);
            }

            pointPos++;
            if(isFirstPoint){
                isFirstPoint = false;
            } else {
                g2D.setColor(Color.CYAN);
                g2D.drawLine(x, y, prevX, prevY);
                g2D.setColor(POINT_COLOR);
            }
            prevX = x;
            prevY = y;
        }

    }

    private void drawSplines(List<DoublePoint2D> points, DoublePoint2D center, double zoom, Graphics2D g2D){
        if(points == null || points.isEmpty()){
            return;
        }

        g2D.setColor(new Color(context.getSplinesColorR(), context.getSplinesColorG(), context.getSplinesColorB()));

        double cellSize = basicCellSize / zoom;

        int prevX = 0;
        int prevY = 0;
        boolean isFirst = false;

        for (DoublePoint2D point2D : points){
            int x = TranslationUtils.uToX(center, getWidth(), point2D.u, cellSize);
            int y = TranslationUtils.vToY(center, getHeight(), point2D.v, cellSize);
            if (!isFirst){
                isFirst = true;
            } else {
                g2D.drawLine(prevX, prevY, x, y);
            }
            prevX = x;
            prevY = y;
        }

    }
    @Override
    public void onEditorPositionChange() {
        repaint();
    }

    @Override
    public void onPointsChange() {
        repaint();
    }
}
