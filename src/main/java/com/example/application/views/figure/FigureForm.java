package com.example.application.views.figure;

import com.example.application.ExtCanvas;
import com.example.application.data.entity.Figure;
import com.example.application.data.entity.Level;
import com.example.application.data.service.LevelService;
import com.example.application.data.utils.Notifier;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.shared.Registration;

import java.util.Arrays;

public class FigureForm extends VerticalLayout {
    LevelService levelService;
    Figure figure;
    VerticalLayout figureLayout = new VerticalLayout();

    Binder<Figure> binder = new BeanValidationBinder<>(Figure.class);

    Label levelsLabel = new Label("Уровни");
    MultiSelectListBox<Level> levels = new MultiSelectListBox<>();
    Button save = new Button("Сохранить");
    Button close = new Button("Закрыть");

    public FigureForm(LevelService levelService) {
        this.levelService = levelService;
        addClassName("figure-form");
        levels.setItems(levelService.getAllLevels());
        levels.setRenderer(new TextRenderer<>(level -> level.getLevelName().gelValue()));
        binder.forField(levels).bind("levels");
        add(figureLayout, levelsLabel, levels, createButtonsLayout());
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
        initFigure();
        binder.readBean(figure);
    }

    private void initFigure() {
        figureLayout.removeAll();
        if (figure == null) {
            figure = new Figure();
            figure.setMatrix(new int[4][4]);
        }
        int[][] figureMatrix = figure.getMatrix();

        VerticalLayout figureRowsLayout = new VerticalLayout();
        figureRowsLayout.setSpacing(false);

        for (int i = 0; i < figureMatrix.length; i++) {
            HorizontalLayout figureRowLayout = new HorizontalLayout();
            figureRowLayout.setSpacing(false);
            for (int j = 0; j < figureMatrix[0].length; j++) {
                ExtCanvas cell = new ExtCanvas(60, 60);
                cell.setId(String.format("%s-%s", i, j));
                if (figureMatrix[i][j] == 1) {
                    cell.getContext().fillRect(0, 0, 60, 60);
                } else {
                    cell.getContext().setStrokeStyle("black");
                    cell.getContext().rect(0, 0, 60, 60);
                    cell.getContext().stroke();
                }
                cell.addClickListener(createCellListener());
                figureRowLayout.add(cell);
            }
            figureRowsLayout.add(figureRowLayout);
        }
        figureLayout.add(figureRowsLayout);
    }

    private ComponentEventListener<ClickEvent<ExtCanvas>> createCellListener() {
        return extCanvasClickEvent -> {
            String[] split = extCanvasClickEvent.getSource().getId().get().split("-");
            int i = Integer.parseInt(split[0]);
            int j = Integer.parseInt(split[1]);
            int[][] matrix = figure.getMatrix();
            matrix[i][j] = matrix[i][j] == 0 ? 1 : 0;
            figure.setMatrix(matrix);
            initFigure();
        };
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> {
            if (levels.isEmpty()) {
                Notifier.showWarningNotification("Выберите уровень сложности");
            } else {
                validateAndSave();
            }
        });
        close.addClickListener(event -> fireEvent(new FigureForm.CloseEvent(this)));


        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        try {
            if (!isCorrect()) {
                Notifier.showWarningNotification("Нарушена целостность фигуры");
            } else {
                binder.writeBean(figure);
                fireEvent(new FigureForm.SaveEvent(this, figure));
            }
        } catch (Exception validationException) {
            validationException.printStackTrace();
        }
    }

    private boolean isCorrect() {
        int[][] figureMatrix = figure.getMatrix();
        int sum = arraySum(figureMatrix);
        if (sum == 0) {
            return false;
        }
        if (sum == 1) {
            return true;
        }
        for (int i = 0; i < figureMatrix.length; i++) {
            for (int j = 0; j < figureMatrix[0].length; j++) {
                if (figureMatrix[i][j] == 1) {
                    if (!(i < figureMatrix.length - 1 && figureMatrix[i + 1][j] == 1
                            || i > 0 && figureMatrix[i - 1][j] == 1
                            || j < figureMatrix[0].length - 1 && figureMatrix[i][j + 1] == 1
                            || j > 0 && figureMatrix[i][j - 1] == 1)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private int arraySum(int[][] array) {
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j =0; j < array[0].length; j++) {
                sum += array[i][j];
            }
        }
        return sum;
    }
    public static abstract class FigureFormEvent extends ComponentEvent<FigureForm> {
        private Figure figure;

        protected FigureFormEvent(FigureForm source, Figure figure) {
            super(source, false);
            this.figure = figure;
        }

        public Figure getFigure() {
            return figure;
        }
    }

    public static class SaveEvent extends FigureFormEvent {
        SaveEvent(FigureForm source, Figure figure) {
            super(source, figure);
        }
    }

    public static class CloseEvent extends FigureFormEvent {
        CloseEvent(FigureForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
