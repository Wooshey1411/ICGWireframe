package ru.nsu.icg.wireframe.gui.controller.window.buttons;

import com.google.gson.Gson;
import ru.nsu.icg.wireframe.gui.common.Context;
import ru.nsu.icg.wireframe.gui.common.SettingsDTO;
import ru.nsu.icg.wireframe.gui.controller.files.WireframeSavingChooser;
import ru.nsu.icg.wireframe.gui.view.window.IWindowView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SaveButtonController extends ButtonController {

    private final JFileChooser fileChooser;
    public SaveButtonController(IWindowView view, Context context) {
        super(view, context);
        fileChooser = new WireframeSavingChooser();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        SettingsDTO settingsDTO = getContext().getSettingsDTO();

        Gson gson = new Gson();
        String json = gson.toJson(settingsDTO);

        int code = fileChooser.showSaveDialog(getView().getFrame());

        if (code == JFileChooser.CANCEL_OPTION){
            return;
        }

        if (code == JFileChooser.ERROR_OPTION) {
            getView().showError("Cannot save file");
            return;
        }

        final String fileWithoutExtension = fileChooser.getSelectedFile().toString();
        final String fileExtension = fileChooser.getFileFilter().getDescription();
        final String filePath = String.format("%s.%s", fileWithoutExtension, fileExtension);

        if (Files.exists(Path.of(filePath))){
            getView().showError("File exists yet");
            return;
        }

        try(FileWriter writer = new FileWriter(filePath)){
            writer.write(json);
        }
        catch (IOException ex) {
            getView().showError(ex.getLocalizedMessage());
        }

    }
}
