package ru.nsu.icg.wireframe.gui.controller.files;

public class WireframeSavingChooser extends WireframeFileChooser{
    public WireframeSavingChooser(){
        addChoosableFileFilter(new FileSavingFilter("wfm"));
    }
}
