package ru.nsu.icg.wireframe.gui.controller.window.menu;

import ru.nsu.icg.wireframe.gui.common.Context;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResetAnglesController implements ActionListener {

    private Context context;
    public ResetAnglesController(Context context){
        this.context = context;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        context.resetAngles();
    }
}
