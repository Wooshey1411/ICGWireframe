package ru.nsu.icg.wireframe.gui.controller.window.buttons;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ru.nsu.icg.wireframe.gui.common.Context;
import ru.nsu.icg.wireframe.gui.common.SettingsDTO;
import ru.nsu.icg.wireframe.gui.controller.files.WireframeOpeningChooser;
import ru.nsu.icg.wireframe.gui.view.window.IWindowView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OpenButtonController extends ButtonController {

    private final JFileChooser fileChooser;

    public OpenButtonController(IWindowView view, Context context) {
        super(view, context);
        fileChooser = new WireframeOpeningChooser();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final int code = fileChooser.showOpenDialog(getView().getFrame());

        if (code == JFileChooser.CANCEL_OPTION) {
            return;
        }

        if (code == JFileChooser.ERROR_OPTION) {
            getView().showError("Cannot open file");
            return;
        }

        String readJson;
        try(BufferedReader fileReader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))){
            readJson = fileReader.readLine();
        } catch (IOException ex){
            getView().showError("Cannot open file");
            return;
        }

        SettingsDTO settingsDTO;

        try {
            Gson gson = new Gson();
            settingsDTO = gson.fromJson(readJson, SettingsDTO.class);
            getContext().setSettings(settingsDTO);
        } catch (JsonSyntaxException ex){
            System.out.println(ex.getLocalizedMessage());
            getView().showError("Unsupportable format");
        }

    }
}
