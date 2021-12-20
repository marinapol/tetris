package com.example.application.views.user;

import com.example.application.data.entity.User;
import com.example.application.data.service.RoleService;
import com.example.application.data.service.UserService;
import com.example.application.data.utils.Constants;
import com.example.application.data.utils.Notifier;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "admin/user", layout = MainLayout.class)
public class UserView extends VerticalLayout {

    UserService userService;
    RoleService roleService;

    Grid<User> userGrid = new Grid<>(User.class);
    Button createUserButton = new Button("Создать");

    UserForm userForm;

    public UserView(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;

        addClassName("user-view");
        setSizeFull();
        initGrid();
        initUserForm();
        add(getToolbar(), getContent());

        updateUsers();
        closeEditor();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(userGrid, userForm);
        content.setFlexGrow(2, userGrid);
        content.setFlexGrow(1, userForm);
        content.addClassName("content");
        content.setSizeFull();
        return content;

    }
    private void initGrid() {
        userGrid.addClassNames("user-grid");
        userGrid.setSizeFull();
        userGrid.setColumns("username", "password", "stringRoles");
        //userGrid.addColumn(user -> user.getRoles().stream().findAny().get().getName()).setHeader("Роль");
        userGrid.getColumnByKey("username").setHeader("Логин");
        userGrid.getColumnByKey("password").setHeader("Пароль");

        userGrid.asSingleSelect().addValueChangeListener(gridUserComponentValueChangeEvent ->
                editUser(gridUserComponentValueChangeEvent.getValue(), false));
    }
    private HorizontalLayout getToolbar() {
        createUserButton.addClickListener(buttonClickEvent -> addUser());
        HorizontalLayout toolbar = new HorizontalLayout(createUserButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateUsers() {
        userGrid.setItems(userService.getAllUsers());
    }

    private void initUserForm() {
        userForm = new UserForm(roleService);
        userForm.setWidth("25em");
        userForm.addListener(UserForm.SaveEvent.class, this::saveUser);
        userForm.addListener(UserForm.DeleteEvent.class, this::deleteUser);
        userForm.addListener(UserForm.CloseEvent.class, closeEvent -> closeEditor());
    }

    private void saveUser(UserForm.SaveEvent event) {
        if(getClassName().contains("editing")) {
            userService.updateUser(event.getUser(), (!event.getSource().password.getValue().equals("")));
            updateUsers();
            closeEditor();
            Notifier.showSuccessNotification(Constants.USER_SAVE_SUCCESS);
        }
        else {
            if (userService.saveUser(event.getUser())) {
                updateUsers();
                closeEditor();
                Notifier.showSuccessNotification(Constants.USER_SAVE_SUCCESS);
            }
            else {
                Notifier.showWarningNotification("Ошибка");
            }
        }
    }

    private void deleteUser(UserForm.DeleteEvent event) {
        userService.deleteUser(event.getUser().getId());
        updateUsers();
        closeEditor();
        Notifier.showSuccessNotification(Constants.USER_DELETE_SUCCESS);
    }

    private void closeEditor() {
        userForm.setUser(null, false);
        userForm.setVisible(false);
        removeClassName("editing");
    }

    public void editUser(User user, boolean isNew) {
        if (user == null) {
            closeEditor();
        }
        else {

            userForm.setUser(user, isNew);
            userForm.setVisible(true);
            if (!isNew) {
                addClassName("editing");
            }
        }
    }

    private void addUser() {
        userGrid.asSingleSelect().clear();
        editUser(new User(), true);
    }

}
