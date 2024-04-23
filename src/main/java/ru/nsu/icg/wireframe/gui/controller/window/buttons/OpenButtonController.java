package ru.nsu.icg.wireframe.gui.controller.window.buttons;

import ru.nsu.icg.wireframe.gui.common.Context;
import ru.nsu.icg.wireframe.gui.view.window.IWindowView;

import java.awt.event.ActionEvent;

public class OpenButtonController extends ButtonController {
    public OpenButtonController(IWindowView view, Context context) {
        super(view, context);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("OPEN");
    }
}
