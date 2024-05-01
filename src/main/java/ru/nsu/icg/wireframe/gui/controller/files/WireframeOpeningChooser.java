package ru.nsu.icg.wireframe.gui.controller.files;

import javax.swing.filechooser.FileNameExtensionFilter;

public class WireframeOpeningChooser extends WireframeFileChooser{
    public WireframeOpeningChooser(){
        addChoosableFileFilter(new FileNameExtensionFilter("Wireframe model", "wfm"));
    }
}
