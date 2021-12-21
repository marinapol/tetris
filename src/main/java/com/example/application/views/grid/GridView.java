package com.example.application.views.grid;

import com.example.application.data.service.GridService;
import com.example.application.data.service.LevelService;
import com.example.application.data.utils.Constants;
import com.example.application.data.utils.Notifier;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@Route(value = "admin/grid", layout = MainLayout.class)
public class GridView extends VerticalLayout {

    GridService gridService;
    LevelService levelService;

    Grid<com.example.application.data.entity.Grid> grid = new Grid<>(com.example.application.data.entity.Grid.class);
    Button createGridButton = new Button("Создать");

    GridForm gridForm;

    public GridView(GridService gridService, LevelService levelService) {
        this.gridService = gridService;
        this.levelService = levelService;

        addClassName("grid-view");
        setSizeFull();
        initGrid();
        initGridForm();
        add(getToolbar(), getContent());

        updateGrids();
    }

    private void initGrid() {
        grid.setSizeFull();
        grid.setColumns("width", "height");
        grid.getColumnByKey("width").setHeader("Ширина");
        grid.getColumnByKey("height").setHeader("Высота");

        grid.asSingleSelect().addValueChangeListener(event ->
                editGrid(event.getValue()));
    }

    private void initGridForm() {
        gridForm = new GridForm();
        gridForm.setWidth("25em");
        gridForm.addListener(GridForm.SaveEvent.class, this::saveGrid);
        gridForm.addListener(GridForm.DeleteEvent.class, this::deleteGrid);
        gridForm.addListener(GridForm.CloseEvent.class, closeEvent -> closeEditor());
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, gridForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, gridForm);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private HorizontalLayout getToolbar() {
        createGridButton.addClickListener(buttonClickEvent -> addGrid());
        HorizontalLayout toolbar = new HorizontalLayout(createGridButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateGrids() {
        grid.setItems(gridService.getAllGrids());
    }

    private void saveGrid(GridForm.SaveEvent event) {
        gridService.saveGrid(event.getGrid());
        updateGrids();
        closeEditor();
        Notifier.showSuccessNotification(Constants.GRID_SAVE_SUCCESS);
    }

    private void deleteGrid(GridForm.DeleteEvent event) {
        if (!gridService.deleteGrid(event.getGrid().getId())) {
            if (levelService.existsLevelByGrid(event.getGrid())) {
                Notifier.showWarningNotification("Удаление невозможно, т.к. сущетсвует зависимый уровень сложности");
                return;
            }
        }
        updateGrids();
        closeEditor();
        Notifier.showSuccessNotification(Constants.GRID_DELETE_SUCCESS);
    }

    private void closeEditor() {
        gridForm.setGrid(null);
        gridForm.setVisible(false);
        removeClassName("editing");
    }

    public void editGrid(com.example.application.data.entity.Grid grid) {
        if (grid == null) {
            closeEditor();
        }
        else {
            gridForm.setGrid(grid);
            gridForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void addGrid() {
        grid.asSingleSelect().clear();
        editGrid(new com.example.application.data.entity.Grid());
    }

}
