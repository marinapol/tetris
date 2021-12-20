package com.example.application;

import com.vaadin.flow.component.ClickNotifier;
import org.vaadin.pekkam.Canvas;

public class ExtCanvas extends Canvas implements ClickNotifier<ExtCanvas> {

    public ExtCanvas(int width, int height) {
        super(width, height);
    }
}
