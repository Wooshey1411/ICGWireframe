package ru.nsu.icg.wireframe.gui.controller.window.menu;

import ru.nsu.icg.wireframe.gui.view.window.IWindowView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenBSplineEditorController implements ActionListener {

    private final IWindowView view;
    public OpenBSplineEditorController(IWindowView view){
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        view.showSplineEditor();
    }
}
