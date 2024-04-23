package ru.nsu.icg.wireframe.gui.view.editor;

import lombok.Getter;
import ru.nsu.icg.wireframe.gui.common.Context;
import ru.nsu.icg.wireframe.model.DoublePoint2D;
import ru.nsu.icg.wireframe.gui.common.EditorParamsListener;
import ru.nsu.icg.wireframe.gui.controller.editor.EditorButtonsController;
import ru.nsu.icg.wireframe.gui.controller.editor.keyControllers.PointXFieldKeyController;
import ru.nsu.icg.wireframe.gui.controller.editor.keyControllers.PointYFieldKeyController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SettingsBar extends JPanel implements EditorParamsListener {

    private final static Font LABEL_FONT = new Font("Go", Font.BOLD, 16);
    private final static Font TEXT_FIELD_FONT = new Font("Times New Roman", Font.PLAIN, 13);
    private final static Font BUTTON_FONT = new Font("Go", Font.BOLD, 11);
    private final static Dimension COMPONENT_SIZE = new Dimension(100, 30);
    private final static Dimension SETTING_BLOCK_SIZE = new Dimension(120, 30);
    private final static Dimension SETTINGS_FIELD_SIZE = new Dimension(Integer.MAX_VALUE, 120);

    private final static Dimension BUTTON_SIZE = new Dimension(100, 30);

    private final static Color BACKGROUD_COLOR = Color.DARK_GRAY;

    private final JSpinner NSpinner = new JSpinner(new SpinnerNumberModel(1,1,50,1));
    private final JSpinner MSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 50, 1));
    private final JSpinner M1Spinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));

    @Getter
    private final JTextField xPosField;

    @Getter
    private final JTextField yPosField;
    private final JSpinner redSpinner = new JSpinner(new SpinnerNumberModel(255, 0, 255, 1));
    private final JSpinner greenSpinner = new JSpinner(new SpinnerNumberModel(255, 0, 255, 1));
    private final JSpinner blueSpinner = new JSpinner(new SpinnerNumberModel(255, 0, 255, 1));
    public SettingsBar(EditorButtonsController editorButtonsController,
                       Context context,
                       PointXFieldKeyController pointXFieldKeyController,
                       PointYFieldKeyController pointYFieldKeyController){
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);
        setPreferredSize(new Dimension(Integer.MAX_VALUE, 160));

        context.setEditorParamsListener(this);

        xPosField = new JTextField();
        xPosField.setFont(TEXT_FIELD_FONT);
        xPosField.setPreferredSize(COMPONENT_SIZE);
        xPosField.setForeground(Color.WHITE);

        yPosField = new JTextField();
        yPosField.setFont(TEXT_FIELD_FONT);
        yPosField.setPreferredSize(COMPONENT_SIZE);
        yPosField.setForeground(Color.WHITE);

        NSpinner.setPreferredSize(COMPONENT_SIZE);
        MSpinner.setPreferredSize(COMPONENT_SIZE);
        M1Spinner.setPreferredSize(COMPONENT_SIZE);
        redSpinner.setPreferredSize(COMPONENT_SIZE);
        greenSpinner.setPreferredSize(COMPONENT_SIZE);
        blueSpinner.setPreferredSize(COMPONENT_SIZE);

        pointXFieldKeyController.setTextField(xPosField);
        xPosField.addKeyListener(pointXFieldKeyController);
        pointYFieldKeyController.setTextField(yPosField);
        yPosField.addKeyListener(pointYFieldKeyController);

        NSpinner.addChangeListener(e -> context.setCountOfPointsInSpline((int)NSpinner.getValue()));
        redSpinner.addChangeListener(e -> context.setSplinesColorR((int)redSpinner.getValue()));
        greenSpinner.addChangeListener(e -> context.setSplinesColorG((int)greenSpinner.getValue()));
        blueSpinner.addChangeListener(e -> context.setSplinesColorB((int)blueSpinner.getValue()));
        MSpinner.addChangeListener(e -> context.setCountOfGenerating((int)MSpinner.getValue()));
        M1Spinner.addChangeListener(e -> context.setCountOfPointsInCircle((int)M1Spinner.getValue()));

        JPanel settingFields = getSettingsFields();
        settingFields.setBorder(BorderFactory.createEmptyBorder(0,0,0,40));
        add(settingFields, BorderLayout.NORTH);
        add(getButtonsPanel(editorButtonsController), BorderLayout.SOUTH);
    }

    private JPanel getSettingsFields() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(SETTINGS_FIELD_SIZE);
        panel.setBackground(BACKGROUD_COLOR);
        panel.setLayout(new GridLayout(3, 3));

        addLabelWithComponent("N", NSpinner, panel);
        addLabelWithComponent("M", MSpinner, panel);
        addLabelWithComponent("M1", M1Spinner, panel);
        addLabelWithComponent("Spline red", redSpinner, panel);
        addLabelWithComponent("Spline green", greenSpinner, panel);
        addLabelWithComponent("Spline blue", blueSpinner, panel);
        addLabelWithComponent("X of point", xPosField, panel);
        addLabelWithComponent("Y of point", yPosField, panel);

        return panel;
    }

    private static JPanel getLabelWithComponent(Label label, Component component){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(BACKGROUD_COLOR);
        panel.setPreferredSize(SettingsBar.SETTING_BLOCK_SIZE);
        panel.add(label);
        panel.add(component);
        return panel;
    }

    private static void addLabelWithComponent(String text, Component component, JPanel panel) {
        Label label = new Label(text);
        label.setFont(SettingsBar.LABEL_FONT);
        JPanel componentsPanel = getLabelWithComponent(label, component);
        panel.add(componentsPanel);
    }

    private JPanel getButtonsPanel(EditorButtonsController editorButtonsController){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(BACKGROUD_COLOR);
        panel.add(getButton("Apply", editorButtonsController.onApplyBtnClicked()));
        panel.add(getButton("Add point", editorButtonsController.onAddPointClicked()));
        panel.add(getButton("Delete point", editorButtonsController.onDeletePointClicked()));
        panel.add(getButton("Zoom +", editorButtonsController.onZoomInClicked()));
        panel.add(getButton("Zoom -", editorButtonsController.onZoomOutClicked()));
        panel.add(getButton("Clear", editorButtonsController.onClearClicked()));
        panel.add(getButton("Normalize", editorButtonsController.onNormalizeClicked()));
        return panel;
    }

    private static JButton getButton(String text, ActionListener actionListener){
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setPreferredSize(BUTTON_SIZE);
        button.addActionListener(actionListener);
        return button;
    }

    @Override
    public void onParamsChange(Context context) {
        NSpinner.setValue(context.getCountOfPointsInSpline());
        redSpinner.setValue(context.getSplinesColorR());
        greenSpinner.setValue(context.getSplinesColorG());
        blueSpinner.setValue(context.getSplinesColorB());
    }

    @Override
    public void onPointPosChange(Context context) {
        int pos = context.getCurrPivotPointPos();
        if (pos == Context.NULL_POS){
            xPosField.setText("");
            yPosField.setText("");
            return;
        }

        DoublePoint2D point2D = context.getPivotPoints().get(pos);
        xPosField.setText(String.format("%.04f", point2D.u));
        yPosField.setText(String.format("%.04f", point2D.v));
    }
}
