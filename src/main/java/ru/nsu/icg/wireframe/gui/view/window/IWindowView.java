package ru.nsu.icg.wireframe.gui.view.window;

import javax.swing.*;

public interface IWindowView {
    void showSplineEditor();
    JFrame getFrame();

    void showError(String msg);
}
