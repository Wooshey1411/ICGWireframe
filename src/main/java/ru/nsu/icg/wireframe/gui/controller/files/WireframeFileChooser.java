package ru.nsu.icg.wireframe.gui.controller.files;

import javax.swing.*;

public abstract class WireframeFileChooser extends JFileChooser {
    public WireframeFileChooser(){
        setAcceptAllFileFilterUsed(false);
    }
}
