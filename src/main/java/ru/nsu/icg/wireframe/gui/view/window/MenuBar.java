package ru.nsu.icg.wireframe.gui.view.window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuBar extends JMenuBar {
    private final Font font;
    private final Color menuBackgroundColor;
    private final Color buttonsColor;

    public MenuBar(ActionListener openListener,
                   ActionListener saveListener,
                   ActionListener openBSplineListener,
                   ActionListener helpListener,
                   ActionListener aboutListener,
                   ActionListener resetAngles){


        setLayout(new FlowLayout(FlowLayout.LEFT));
        menuBackgroundColor = Color.decode("#D9D9D9");
        setBackground(menuBackgroundColor);
        font = new Font("Go", Font.BOLD, 14);
        buttonsColor = Color.decode("#242121");
        setBackground(menuBackgroundColor);
        add(createFileMenu(openListener, saveListener, menuBackgroundColor, buttonsColor, font));
        add(createToolsMenu(openBSplineListener, resetAngles, menuBackgroundColor, buttonsColor, font));
        add(createInfoMenu(helpListener, aboutListener, menuBackgroundColor, buttonsColor, font));
    }

    private static JMenu createMenu(String name, Color backgroundColor, Color buttonColor, Font font){
        JMenu menu = new JMenu(name);
        menu.setBackground(backgroundColor);
        menu.setForeground(buttonColor);
        menu.setFont(font);
        return menu;
    }

    private static JMenuItem createMenuItem(String name, ActionListener action, Font font, Color backgroundColor){
        JMenuItem menuItem = new JMenuItem(name);
        menuItem.setBorderPainted(false);
        menuItem.setFont(font);
        menuItem.setForeground(backgroundColor);
        menuItem.addActionListener(action);
        return menuItem;
    }
    private static JMenu createFileMenu(ActionListener openListener,
                                        ActionListener saveListener,
                                        Color menuBackgroundColor,
                                        Color buttonsColor,
                                        Font font) {
        JMenu fileMenu = createMenu("File", menuBackgroundColor, buttonsColor, font);
        fileMenu.add(createMenuItem("Open", openListener, font, menuBackgroundColor));
        fileMenu.add(createMenuItem("Save", saveListener, font, menuBackgroundColor));
        return fileMenu;
    }

    private static JMenu createToolsMenu(ActionListener openEditor,
                                          ActionListener resetAngles,
                                          Color menuBackgroundColor,
                                          Color buttonsColor,
                                          Font font) {
        JMenu toolsMenu = createMenu("Tools", menuBackgroundColor, buttonsColor, font);
        toolsMenu.add(createMenuItem("Open editor", openEditor, font, menuBackgroundColor));
        toolsMenu.add(createMenuItem("Reset angles", resetAngles, font, menuBackgroundColor));
        return toolsMenu;
    }

    private static JMenu createInfoMenu(ActionListener openHelp,
                                        ActionListener openAbout,
                                        Color menuBackgroundColor,
                                        Color buttonsColor,
                                        Font font) {

        JMenu infoMenu = createMenu("Info", menuBackgroundColor, buttonsColor, font);
        infoMenu.add(createMenuItem("Help", openHelp, font, menuBackgroundColor));
        infoMenu.add(createMenuItem("About", openAbout, font, menuBackgroundColor));
        return infoMenu;
    }

}
