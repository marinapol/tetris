package com.example.application.views.login;

import com.example.application.data.entity.User;
import com.example.application.data.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("register")
@PageTitle("Register | Tetris")
public class RegisterView extends VerticalLayout {

    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Button registerButton = new Button("Зарегаться");

    @Autowired
    UserService userService;

    public RegisterView() {
        addClassName("register-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        usernameField.setTitle("Логин");
        passwordField.setTitle("Пароль");
        registerButton.addClickListener(buttonClickEvent -> {
            User user = new User();
            user.setUsername(usernameField.getValue());
            user.setPassword(passwordField.getValue());
            userService.saveUser(user);
        });
        add(new H1("Регистрация"));
        add(usernameField, passwordField, registerButton);

    }
}
