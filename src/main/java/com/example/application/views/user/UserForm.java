package com.example.application.views.user;

import com.example.application.data.entity.Role;
import com.example.application.data.entity.User;
import com.example.application.data.service.RoleService;
import com.example.application.data.utils.Notifier;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class UserForm extends FormLayout {

    private RoleService roleService;

    private User user;

    Binder<User> binder = new BeanValidationBinder<>(User.class);

    TextField username = new TextField("Логин");
    Button changePasswordBtn = new Button("Сменить пароль");
    PasswordField password = new PasswordField("Пароль");
    Label rolesLabel = new Label("Роли");
    //MultiSelectListBox<Role> roles = new MultiSelectListBox<>();

    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Закрыть");


    public UserForm(RoleService roleService) {

        this.roleService = roleService;
        addClassName("user-form");
        binder.forField(username).bind("username");
        //binder.forField(roles).bind("roles");

        changePasswordBtn.addClickListener(buttonClickEvent -> {
            changePasswordBtn.setVisible(false);
            password.setVisible(true);
            binder.forField(password).withValidator(password -> password.length() <= 12 && password.length() >= 4,
                            "размер должен находиться в диапозоне от 4 до 12")
                    .bind("password");

        });
        //roles.setItems(roleService.getRoles());
        //roles.setRenderer(new TextRenderer<>(Role::getName));
        add(username, changePasswordBtn, password, /*rolesLabel, roles*/ createButtonsLayout());
    }

    public void setUser(User user, boolean isNew) {
        this.user = user;
        if (!isNew && binder.getBinding("password").isPresent()) {
            binder.removeBinding("password");
            password.setValue("");
        }
        if (isNew && binder.getBinding("password").isEmpty()) {
            binder.forField(password).withValidator(password -> password.length() <= 12 && password.length() >= 4,
                            "размер должен находиться в диапозоне от 4 до 12")
                    .bind("password");
        }
        changePasswordBtn.setVisible(!isNew);
        password.setVisible(isNew);
        binder.readBean(user);
        //password.se
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> {
            validateAndSave();
           /* if (roles.isEmpty()) {
                Notifier.showWarningNotification("Выберите роли пользователя");
            } else {
                validateAndSave();
            }*/
        });
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, user)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(statusChangeEvent -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(user);

            fireEvent(new SaveEvent(this, user));
        } catch (ValidationException validationException) {
            validationException.printStackTrace();
        }
    }

    public static abstract class UserFormEvent extends ComponentEvent<UserForm> {
        private User user;

        protected UserFormEvent(UserForm source, User user) {
            super(source, false);
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }

    public static class SaveEvent extends UserFormEvent {
        SaveEvent(UserForm source, User user) {
            super(source, user);
        }
    }

    public static class DeleteEvent extends UserFormEvent {
        DeleteEvent(UserForm source, User user) {
            super(source, user);
        }
    }

    public static class CloseEvent extends UserFormEvent {
        CloseEvent(UserForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
