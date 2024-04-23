package ru.nsu.icg.wireframe.gui.view.editor;

import java.awt.*;

public interface IEditorView {
    public void show();
    public void hide();

    void showDialogWindow(String name, Component component);
}
