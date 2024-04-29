package ru.nsu.icg.wireframe.gui.controller.window;

import ru.nsu.icg.wireframe.gui.common.Context;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class WireframePanelController extends MouseAdapter {

    private int prevX;
    private int prevY;
    private boolean isPressed = false;
    private final Context context;
    private final static double SENSIBILITY = 0.4;

    public WireframePanelController(Context context){
        this.context = context;
    }
    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        isPressed = true;
        prevX = e.getX();
        prevY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        isPressed = false;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        super.mouseWheelMoved(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        context.setAngleY(context.getAngleY() + (prevX - e.getX())*SENSIBILITY);
        context.setAngleX(context.getAngleX() + (prevY - e.getY())*SENSIBILITY);
        prevX = e.getX();
        prevY = e.getY();
        context.getWireframeListener().onPointsChange();
    }
}
