package ru.nsu.icg.wireframe.gui.controller.files;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FileSavingFilter extends FileFilter {

    private final String description;

    public FileSavingFilter(String description){
        this.description = description;
    }
    @Override
    public boolean accept(File f) {
        return true;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
