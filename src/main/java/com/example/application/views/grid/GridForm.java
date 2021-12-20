package com.example.application.views.grid;

import com.example.application.data.entity.Grid;
import com.example.application.data.entity.User;
import com.example.application.views.user.UserForm;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class GridForm extends FormLayout {

    private Grid grid;

    Binder<Grid> binder = new BeanValidationBinder<>(Grid.class);

    IntegerField width = new IntegerField("Ширина");
    IntegerField height = new IntegerField("Высота");

    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Закрыть");

    public GridForm() {
        addClassName("grid-form");

        binder.bindInstanceFields(this);

        add(width, height, createButtonsLayout());
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
        binder.readBean(grid);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new GridForm.DeleteEvent(this, grid)));
        close.addClickListener(event -> fireEvent(new GridForm.CloseEvent(this)));

        binder.addStatusChangeListener(statusChangeEvent -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try{
            binder.writeBean(grid);
            fireEvent(new GridForm.SaveEvent(this, grid));
        }
        catch (ValidationException validationException) {
            validationException.printStackTrace();
        }
    }

    public static abstract class GridFormEvent extends ComponentEvent<GridForm> {
        private Grid grid;

        protected GridFormEvent(GridForm source, Grid grid) {
            super(source, false);
            this.grid = grid;
        }

        public Grid getGrid() {
            return grid;
        }
    }

    public static class SaveEvent extends GridForm.GridFormEvent {
        SaveEvent(GridForm source, Grid grid) {
            super(source, grid);
        }
    }

    public static class DeleteEvent extends GridForm.GridFormEvent {
        DeleteEvent(GridForm source, Grid grid) {
            super(source, grid);
        }
    }
    public static class CloseEvent extends GridForm.GridFormEvent {
        CloseEvent(GridForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
