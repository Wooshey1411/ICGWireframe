package ru.nsu.icg.wireframe.gui.view.editor;

import ru.nsu.icg.wireframe.gui.view.window.ToolsBar;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JFrame {

    public MainPanel(JPanel splinesPanel, JPanel settingsBar){
        super("Spline editor");
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(640, 480));
        setPreferredSize(new Dimension(800, 800));
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        add(splinesPanel, BorderLayout.CENTER);
        add(settingsBar, BorderLayout.SOUTH);

        setVisible(false);
        pack();
        setLocationRelativeTo(null);
    }

}
