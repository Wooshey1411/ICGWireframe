package ru.nsu.icg.wireframe.gui.controller.editor.keyControllers;

import lombok.Setter;
import ru.nsu.icg.wireframe.gui.common.Context;
import ru.nsu.icg.wireframe.gui.common.DoublePoint2D;
import ru.nsu.icg.wireframe.gui.view.editor.IEditorView;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PointYFieldKeyController extends KeyAdapter {

    @Setter
    private JTextField textField;
    private final IEditorView editorView;
    private final Context context;

    public PointYFieldKeyController(IEditorView editorView, Context context){
        this.editorView = editorView;
        this.context = context;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(context.getCurrPointPos() == Context.NULL_POS){
                editorView.showDialogWindow("Некорректный ввод", new JLabel("Не выбрана точка"));
                textField.setText("");
                return;
            }

            double newValue;
            try{
                String string = textField.getText();
                string = string.replace(',', '.');
                newValue = Double.parseDouble(string);
                DoublePoint2D point = context.getPivotPoints().get(context.getCurrPointPos());
                point.v = newValue;
                context.changeSplinePoint(context.getCurrPointPos(), point);
            } catch (NumberFormatException ex){
                DoublePoint2D point = context.getPivotPoints().get(context.getCurrPointPos());
                textField.setText(String.format("%.04f", point.v));
                editorView.showDialogWindow("Некорректный ввод", new JLabel("Вы неправильно ввели число"));
            }
        }
    }
}
