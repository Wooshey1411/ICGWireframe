package ru.nsu.icg.wireframe.gui.controller.editor.keyControllers;

import lombok.Setter;
import ru.nsu.icg.wireframe.gui.common.Context;
import ru.nsu.icg.wireframe.model.DoublePoint2D;
import ru.nsu.icg.wireframe.gui.view.editor.IEditorView;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PointXFieldKeyController extends KeyAdapter {

    @Setter
    private JTextField textField;
    private final IEditorView editorView;
    private final Context context;

    public PointXFieldKeyController(IEditorView editorView, Context context){
        this.editorView = editorView;
        this.context = context;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            double newValue;
            if(context.getCurrPivotPointPos() == Context.NULL_POS){
                editorView.showDialogWindow("Некорректный ввод", new JLabel("Не выбрана точка"));
                textField.setText("");
                return;
            }
            try{
                String string = textField.getText();
                string = string.replace(',', '.');
                newValue = Double.parseDouble(string);
                DoublePoint2D point = context.getPivotPoints().get(context.getCurrPivotPointPos());
                point.u = newValue;
                context.changeSplinePoint(context.getCurrPivotPointPos(), point);
            } catch (NumberFormatException ex){
                DoublePoint2D point = context.getPivotPoints().get(context.getCurrPivotPointPos());
                textField.setText(String.format("%.04f", point.u));
                editorView.showDialogWindow("Некорректный ввод", new JLabel("Вы неправильно ввели число"));
            }
        }
    }
}
