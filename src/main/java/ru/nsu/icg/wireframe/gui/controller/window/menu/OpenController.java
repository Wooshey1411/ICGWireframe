package ru.nsu.icg.wireframe.gui.controller.window.menu;

import ru.nsu.icg.wireframe.gui.common.Context;
import ru.nsu.icg.wireframe.gui.controller.window.buttons.OpenButtonController;
import ru.nsu.icg.wireframe.gui.view.window.IWindowView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenController implements ActionListener {

    private final Context context;
    private final IWindowView view;

    public OpenController(Context context, IWindowView view){
        this.context = context;
        this.view = view;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        new OpenButtonController(view, context).actionPerformed(e);
    }
}
