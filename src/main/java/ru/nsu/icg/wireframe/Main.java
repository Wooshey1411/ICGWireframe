package ru.nsu.icg.wireframe;

import ru.nsu.icg.wireframe.gui.common.Context;
import ru.nsu.icg.wireframe.gui.view.window.WindowViewImpl;

public class Main {
    public static void main(String[] args) {
        Context context = new Context();


        new WindowViewImpl(context);
    }
}

