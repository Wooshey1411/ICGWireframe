package ru.nsu.icg.wireframe.gui.controller.window.buttons;

import lombok.Getter;
import ru.nsu.icg.wireframe.gui.common.Context;
import ru.nsu.icg.wireframe.gui.view.window.IWindowView;

import java.awt.event.ActionListener;

@Getter
public abstract class ButtonController implements ActionListener {

    private final IWindowView view;
    private final Context context;

    protected ButtonController(IWindowView view, Context context){
        this.view = view;
        this.context = context;
    }
}
