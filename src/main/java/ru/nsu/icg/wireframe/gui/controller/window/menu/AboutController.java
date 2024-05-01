package ru.nsu.icg.wireframe.gui.controller.window.menu;

import ru.nsu.icg.wireframe.gui.view.window.IWindowView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AboutController implements ActionListener {

    private final IWindowView view;

    public AboutController(IWindowView view){
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        view.showAbout();
    }
}
