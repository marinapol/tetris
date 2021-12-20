package com.example.application.views.figure;

import com.example.application.ExtCanvas;
import com.example.application.data.entity.Figure;
import com.example.application.data.service.FigureService;
import com.example.application.data.service.LevelService;
import com.example.application.data.utils.Constants;
import com.example.application.data.utils.Notifier;
import com.example.application.views.MainLayout;
import com.example.application.views.grid.GridForm;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "admin/figure", layout = MainLayout.class)
public class FigureView extends VerticalLayout {

    FigureService figureService;
    LevelService levelService;
    FlexLayout figuresLayout;

    FigureForm figureForm;

    Button addFigure;

    public FigureView(FigureService figureService, LevelService levelService) {
        this.figureService = figureService;
        this.levelService = levelService;
        figuresLayout = new FlexLayout();
        figuresLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        addFigure = new Button("Добавить");
        addFigure.addClickListener(buttonClickEvent -> {
            Figure figure = new Figure();
            figure.setMatrix(new int[4][4]);
            editFigure(figure);
        });
        setSpacing(false);

        initFigures();
        initFigureForm();
        add(addFigure, getContent());
    }

    private void initFigureForm() {
        figureForm = new FigureForm(levelService);
        figureForm.setWidth("25em");
        figureForm.addListener(FigureForm.SaveEvent.class, this::saveFigure);
        figureForm.addListener(FigureForm.CloseEvent.class, closeEvent -> closeEditor());
    }

    private void initFigures() {
        figuresLayout.removeAll();
        List<Figure> figures = figureService.getAllFigures();

        for (Figure figure : figures) {
            int[][] figureMatrix = figure.getMatrix();

            VerticalLayout figureRowsLayout = new VerticalLayout();
            figureRowsLayout.setWidth("AUTO");
            figureRowsLayout.setSpacing(false);

            for (int i = 0; i < figureMatrix.length; i++) {
                HorizontalLayout figureRowLayout = new HorizontalLayout();
                figureRowLayout.setSpacing(false);
                for (int j = 0; j < figureMatrix[0].length; j++) {
                    ExtCanvas cell = new ExtCanvas(40, 40);
                    cell.setId(String.format("%s-%s-%s", figure.getId(), i, j));
                    if (figureMatrix[i][j] == 1) {
                        cell.getContext().fillRect(0, 0, 40, 40);
                    } else {
                        cell.getContext().setStrokeStyle("black");
                        cell.getContext().rect(0, 0, 40, 40);
                        cell.getContext().stroke();
                    }
                    figureRowLayout.add(cell);
                }
                figureRowsLayout.add(figureRowLayout);
            }
            HorizontalLayout buttonsLayout = new HorizontalLayout();
            buttonsLayout.setMargin(true);
            Button editBtn = new Button(new Icon(VaadinIcon.EDIT));
            editBtn.addThemeVariants(ButtonVariant.LUMO_ICON);
            editBtn.setId(String.format("EDIT-%s", figure.getId()));
            editBtn.addClickListener(createFigureButtonListener());
            buttonsLayout.add(editBtn);

            Button removeBtn = new Button(new Icon(VaadinIcon.TRASH));
            removeBtn.addThemeVariants(ButtonVariant.LUMO_ICON);
            removeBtn.setId(String.format("REMOVE-%s", figure.getId()));
            removeBtn.addClickListener(createFigureButtonListener());
            buttonsLayout.add(removeBtn);
            figureRowsLayout.add(buttonsLayout);
            figuresLayout.add(figureRowsLayout);
        }


    }

    private ComponentEventListener<ClickEvent<Button>> createFigureButtonListener() {
        return buttonClickEvent -> {
            String[] split = buttonClickEvent.getSource().getId().get().split("-");
            switch (split[0]) {
                case "EDIT":
                    editFigure(figureService.getById(Integer.valueOf(split[1])));
                    break;
                case "REMOVE":
                    figureService.delete(Integer.valueOf(split[1]));
                    Notifier.showSuccessNotification(Constants.FIGURE_DELETE_SUCCESS);
                    initFigures();
                    break;
            }
        };
    }

    private void saveFigure(FigureForm.SaveEvent event) {
        figureService.save(event.getFigure());
        initFigures();
        closeEditor();
        Notifier.showSuccessNotification(Constants.FIGURE_SAVE_SUCCESS);
    }

    private void closeEditor() {
        figureForm.setFigure(null);
        figureForm.setVisible(false);
        removeClassName("editing");
    }

    private Component getContent() {
        SplitLayout content = new SplitLayout(figuresLayout, figureForm);
        content.setSizeFull();
        content.addClassName("content");
        figureForm.setVisible(false);
        return content;
    }

    public void editFigure(Figure figure) {
        if (figure == null) {
            closeEditor();
        }
        else {
            figureForm.setFigure(figure);
            figureForm.setVisible(true);
            addClassName("editing");
        }
    }
}
