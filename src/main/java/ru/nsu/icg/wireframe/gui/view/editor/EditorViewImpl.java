package ru.nsu.icg.wireframe.gui.view.editor;

import ru.nsu.icg.wireframe.gui.common.Context;
import ru.nsu.icg.wireframe.gui.controller.editor.EditorButtonsController;
import ru.nsu.icg.wireframe.gui.controller.editor.keyControllers.PointXFieldKeyController;
import ru.nsu.icg.wireframe.gui.controller.editor.keyControllers.PointYFieldKeyController;

import javax.swing.*;
import java.awt.*;

public class EditorViewImpl implements IEditorView {

    private final JFrame mainPanel;
    public EditorViewImpl(Context context){

        EditorButtonsController editorButtonsController = new EditorButtonsController(context);
        PointXFieldKeyController pointXFieldKeyController = new PointXFieldKeyController(this, context);
        PointYFieldKeyController pointYFieldKeyController = new PointYFieldKeyController(this, context);
        SplinesPanel splinesPanel = new SplinesPanel(context);

        SettingsBar settingsBar = new SettingsBar(editorButtonsController, context, pointXFieldKeyController, pointYFieldKeyController);

        mainPanel = new MainPanel(splinesPanel, settingsBar);

    }

    @Override
    public void show() {
        mainPanel.setVisible(true);
    }

    @Override
    public void hide() {
        mainPanel.setVisible(false);
    }

    @Override
    public void showDialogWindow(String name, Component component) {
                JOptionPane.showConfirmDialog(
                mainPanel,
                component,
                name,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE);
    }
}
