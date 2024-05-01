package ru.nsu.icg.wireframe.gui.view.window;

import com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme;
import ru.nsu.icg.wireframe.gui.common.Context;
import ru.nsu.icg.wireframe.gui.common.WindowButton;
import ru.nsu.icg.wireframe.gui.common.WireframeListener;
import ru.nsu.icg.wireframe.gui.controller.window.WireframePanelController;
import ru.nsu.icg.wireframe.gui.controller.window.buttons.*;
import ru.nsu.icg.wireframe.gui.controller.window.menu.*;
import ru.nsu.icg.wireframe.gui.view.editor.EditorViewImpl;
import ru.nsu.icg.wireframe.gui.view.editor.IEditorView;

import javax.swing.*;
import java.util.List;

public class WindowViewImpl implements IWindowView, WireframeListener {

    private final IEditorView editorView;
    public final WireframePanel wireframePanel;

    private final JTextPane aboutTextPane;

    private final JTextPane helpTextPane;

    public final JFrame mainWindow;
    public WindowViewImpl(Context context){
        FlatArcDarkOrangeIJTheme.setup();

        AboutController aboutController = new AboutController(this);
        HelpController helpController = new HelpController(this);
        OpenBSplineEditorController openBSplineEditorController = new OpenBSplineEditorController(this);
        OpenController openController = new OpenController(context, this);
        SaveController saveController = new SaveController(context, this);
        ResetAnglesController resetAnglesController = new ResetAnglesController(context);
        WireframePanelController wireframePanelController = new WireframePanelController(context);

        List<WindowButton> windowButtons = List.of(
                new WindowButton(new OpenButtonController(this, context), "Open", "open-icon.png"),
                new WindowButton(new SaveButtonController(this, context), "Save", "save-icon.png"),
                new WindowButton(new OpenEditorButtonController(this, context), "Open B-spline editor", "editor-icon.png"),
                new WindowButton(new ResetAnglesButtonController(this, context), "Reset angles", "reset-icon.png"),
                new WindowButton(new HelpButtonController(this, context), "Help", "help-icon.png")
        );

        MenuBar menuBar = new MenuBar(openController,
                                      saveController,
                                      openBSplineEditorController,
                                      helpController,
                                      aboutController,
                                      resetAnglesController);
        ToolsBar toolsBar = new ToolsBar(windowButtons);
        wireframePanel = new WireframePanel(context, wireframePanelController);
        context.setWireframeListener(this);

        mainWindow = new MainPanel(menuBar, toolsBar, wireframePanel);

        editorView = new EditorViewImpl(context);

        aboutTextPane = createHTMLTextPane();
        aboutTextPane.setText(getAboutText());
        aboutTextPane.setEditable(false);

        helpTextPane = createHTMLTextPane();
        helpTextPane.setText(getHelpText());
        aboutTextPane.setEditable(false);
    }

    @Override
    public void showSplineEditor() {
        editorView.show();
    }

    @Override
    public JFrame getFrame() {
        return mainWindow;
    }

    @Override
    public void showError(String msg) {
        JOptionPane.showMessageDialog(
                mainWindow,
                msg,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    @Override
    public void showAbout() {
        JOptionPane.showMessageDialog(
                null,
                aboutTextPane,
                "About",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public void showHelp() {
        JOptionPane.showMessageDialog(
                null,
                helpTextPane,
                "Help",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public void onPointsChange() {
        wireframePanel.repaint();
    }

    private static JTextPane createHTMLTextPane() {
        final JTextPane result = new JTextPane();
        result.setEditable(false);
        result.setBackground(null);
        result.setContentType("text/html");
        return result;
    }

    private static String getAboutText() {
        return """
               <html>
                    <p><b><i><center>Wireframe</center></i></b></p>
                    <p>Программа для создания проволочных моделей, используя редактор B-сплайнов, и просмотра их.</p>
                    
                    <b>Создал:</b>
                    <ul>
                        <li>Воробьев Андрей</li>
                    </ul>
                    <p>Студент 21203 группы в рамках курса "Компьютерная и инжинерная графика" марта 2024 года </p>
                    <p>Ссылка на проект в гитхабе <u>https://github.com/Wooshey1411/ICGWireframe</u>
               </html>
               """;
    }

    private static String getHelpText(){
        return """
               <html>
                    <p><b><i><center>Рерадтор B-сплайнов</center></i></b></p>
                    <p>Используется для рисования непрерывной гладкой кривой, которая задаёт фигуру вращения</p>
                    <p>Зажав <b>ЛКМ</b> можно перемещаться по полю, а с помощью <b>колесика мыши</b> менять масштабирование</p>
                    <p>Чтобы создать точку кликните <b>ПКМ</b> по полю - появится точка в красном круге.</p>
                    <p>Если вы нажмете <b>ПКМ</b> внутри этого круга - точка удалится.</p>
                    <p>Если нажать на этот круг <b>ЛКМ</b> - он подсветится зеленым, так вы выберете точку.</p>
                    <p>Выбранную точку можно удалить, нажав на кнопку <i>Delete point</i> на панели настроек снизу.</p>
                    <p>У выбранной точки можно настроить её положение в полях <i>X of point</i> и <i>Y of point</i>.</p>
                    <p>Точки можно перемещать, зажав <b>ЛКМ</b> в области круга и двигая её мышью</p>
                    <p>Если точек больше 3, появится кривая, выбранного цвета, который настраивается на панели снизу</p>
                    <p>Для применения настроек, которые вы вводите с клавиатуры нажмите <b>Enter</b></p>
                    <p></p>
                    <b>Панель настроек:</b>
                    <ul>
                        <li><b>N</b> - количество прямых в одном сплайне</li>
                        <li><b>M</b> - количество образующих фигуры вращения</li>
                        <li><b>M1</b> - количество прямых между образующими, которые образуют окружности</li>
                        <li><b>Spline Red</b> - Интенсивность красного цвета кривой</li>
                        <li><b>Spline Green</b> - Интенсивность зеленого цвета кривой</li>
                        <li><b>Spline Blue</b> - Интенсивность синего цвета кривой</li>
                    </ul>
                    <b>Кнопки:</b>
                    <ul>
                        <li><b>Apply</b> - Строит проволочную модель</li>
                        <li><b>Add point</b> - Создаёт новую точка в координатах (0,0)</li>
                        <li><b>Delete point</b> - Удаляет выбранную точку</li>
                        <li><b>Clear</b> - Очищает поле</li>
                        <li><b>Zoom +</b> - Изменяет масштабирование(приближение)</li>
                        <li><b>Zoom -</b> - Изменяет масштабирование(отдаление)</li>
                        <li><b>Normalize</b> - Вписывает точки в квадрат около (0,0)</li>
                    </ul>
                    
                    <p><b><i><center>Проволочная модель</center></i></b></p>
                    <p>Можно вращать, зажав <b>ПКМ</b> и передвигая мышью, так же можно приближать/отдалять, используя <b>колёсико мыши</b></p>
                    <p>После поворотов можно вернуть модель в исходное состояние, нажав на кнопку <i>Reset angles</i></p>
                    <p>Модель имеет такой же цвет как и кривая из редактора, причем ближние прямые светлее дальних</p>
                    
                    <p><b><i><center>Общие функции</center></i></b></p>
                    <p>Модель можно сохранить, нажав на кнопку <i>Save</i> в особый формат <b>.wfm</b></p>
                    <p>Модель можно открыть из сохраненного файла, нажав на кнопку <i>Open</i></p>
               </html>
               """;
    }



}
