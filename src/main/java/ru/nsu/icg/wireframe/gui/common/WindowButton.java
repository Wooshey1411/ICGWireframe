package ru.nsu.icg.wireframe.gui.common;

import ru.nsu.icg.wireframe.gui.controller.window.buttons.ButtonController;

public record WindowButton(ButtonController buttonController, String tip, String iconPath)
{}
