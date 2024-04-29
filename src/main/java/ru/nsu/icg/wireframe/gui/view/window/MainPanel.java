package ru.nsu.icg.wireframe.gui.view.window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainPanel extends JFrame {

    public MainPanel(JMenuBar menuBar, ToolsBar toolsBar, JPanel wireframePanel){
        super("Wireframe");
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(640, 480));
        setPreferredSize(new Dimension(800, 800));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setJMenuBar(menuBar);
        add(toolsBar, BorderLayout.NORTH);
        add(wireframePanel, BorderLayout.CENTER);

        setVisible(true);
        pack();
        setLocationRelativeTo(null);
        this.createBufferStrategy(2);
    }

}
