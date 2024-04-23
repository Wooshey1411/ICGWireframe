package ru.nsu.icg.wireframe.gui.view.window;

import ru.nsu.icg.wireframe.gui.common.WindowButton;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class ToolsBar extends JPanel {

    private final static Color BACKGROUND_COLOR = Color.decode("#D9D9D9");
    private final static Color BUTTON_COLOR = Color.decode("#B7B7B5");
    private final static int BUTTON_SIZE = 32;
    public ToolsBar(List<WindowButton> buttonList) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(BACKGROUND_COLOR);
        for (WindowButton button : buttonList){
            add(createButton(button, BUTTON_SIZE, BUTTON_COLOR));
        }
    }

    private static ImageIcon loadIcon(String iconPath) {
        URL url = ToolsBar.class.getResource(iconPath);
        if (url == null){
            throw new IllegalArgumentException("Icon not found: " + iconPath);
        }
        return new ImageIcon(url);
    }

    private static JButton createButton(WindowButton windowButton, int size, Color buttonColor) {
        JButton button = new JButton(loadIcon(windowButton.iconPath()));
        button.setFocusPainted(false);
        button.setToolTipText(windowButton.tip());
        button.addActionListener(windowButton.buttonController());
        button.setPreferredSize(new Dimension(size, size));
        button.setMinimumSize(new Dimension(size, size));
        button.setBackground(buttonColor);
        button.setBorderPainted(false);
        return button;
    }

}
