package ru.nsu.icg.wireframe.gui.controller.window.buttons;

import ru.nsu.icg.wireframe.gui.common.Context;
import ru.nsu.icg.wireframe.gui.view.window.IWindowView;

import java.awt.event.ActionEvent;

public class OpenEditorButtonController extends ButtonController {

    public OpenEditorButtonController(IWindowView view, Context context) {
        super(view, context);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getView().showSplineEditor();
    }
}
