package ru.nsu.icg.wireframe.gui.controller.editor;

import ru.nsu.icg.wireframe.gui.common.Context;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditorButtonsController {

    private final Context context;

    public EditorButtonsController(Context context){
        this.context = context;
    }

    public ActionListener onApplyBtnClicked(){
        return e -> {
            System.out.println("APPLY CLICKED");
        };
    }

    public ActionListener onZoomInClicked(){
        return e -> {
            System.out.println("Zoom + clicked");
        };
    }

    public ActionListener onZoomOutClicked(){
        return e -> {
            System.out.println("Zoom - clicked");
        };
    }

    public ActionListener onClearClicked(){
        return e -> {
            System.out.println("Clear clicked");
        };
    }

    public ActionListener onNormalizeClicked(){
        return e -> {
            System.out.println("Normalize clicked");
        };
    }

    public ActionListener onAddPointClicked(){
        return e -> {
            System.out.println("Add point clicked");
        };
    }

    public ActionListener onDeletePointClicked(){
        return e -> {
            System.out.println("Delete point clicked");
        };
    }
}
