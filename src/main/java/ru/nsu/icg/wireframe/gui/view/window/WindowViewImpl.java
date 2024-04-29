package ru.nsu.icg.wireframe.gui.view.window;

import com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme;
import ru.nsu.icg.wireframe.gui.common.Context;
import ru.nsu.icg.wireframe.gui.common.WindowButton;
import ru.nsu.icg.wireframe.gui.common.WireframeListener;
import ru.nsu.icg.wireframe.gui.controller.window.WireframePanelController;
import ru.nsu.icg.wireframe.gui.controller.window.buttons.*;
import ru.nsu.icg.wireframe.gui.controller.window.menu.*;
import ru.nsu.icg.wireframe.gui.view.editor.EditorViewImpl;
import ru.nsu.icg.wireframe.gui.view.editor.IEditorView;

import javax.swing.*;
import java.util.List;

public class WindowViewImpl implements IWindowView, WireframeListener {

    private final IEditorView editorView;
    public final WireframePanel wireframePanel;
    public WindowViewImpl(Context context){
        FlatArcDarkOrangeIJTheme.setup();

        AboutController aboutController = new AboutController();
        HelpController helpController = new HelpController();
        OpenBSplineEditorController openBSplineEditorController = new OpenBSplineEditorController(this);
        OpenController openController = new OpenController();
        SaveController saveController = new SaveController();
        ResetAnglesController resetAnglesController = new ResetAnglesController(context);
        WireframePanelController wireframePanelController = new WireframePanelController(context);

        List<WindowButton> windowButtons = List.of(
                new WindowButton(new OpenButtonController(this, context), "Open", "open-icon.png"),
                new WindowButton(new SaveButtonButtonController(this, context), "Save", "save-icon.png"),
                new WindowButton(new OpenEditorButtonController(this, context), "Open B-spline editor", "editor-icon.png"),
                new WindowButton(new ResetAnglesButtonController(this, context), "Reset angles", "reset-icon.png"),
                new WindowButton(new HelpButtonController(this, context), "Help", "help-icon.png")
        );

        MenuBar menuBar = new MenuBar(openController,
                                      saveController,
                                      openBSplineEditorController,
                                      helpController,
                                      aboutController,
                                      resetAnglesController);
        ToolsBar toolsBar = new ToolsBar(windowButtons);
        wireframePanel = new WireframePanel(context, wireframePanelController);
        context.setWireframeListener(this);

        new MainPanel(menuBar, toolsBar, wireframePanel);

        editorView = new EditorViewImpl(context);

    }

    @Override
    public void showSplineEditor() {
        editorView.show();
    }

    @Override
    public void onPointsChange() {
        wireframePanel.repaint();
    }
}
