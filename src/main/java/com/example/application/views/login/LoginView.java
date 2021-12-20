package com.example.application.views.login;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("Login | Tetris")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm loginForm = new LoginForm();

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        loginForm.setForgotPasswordButtonVisible(false);
        loginForm.setAction("login");

        initLoginForm();

        add(new H1("Тетрис"), loginForm);

    }

    private void initLoginForm() {
        LoginI18n i18n = LoginI18n.createDefault();
         LoginI18n.Form i18nForm = i18n.getForm();
         i18nForm.setTitle("Вход");
         i18nForm.setUsername("Логин");
         i18nForm.setPassword("Пароль");
         i18nForm.setSubmit("Войти");
         i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("Неверные логин и/или пароль");
        i18nErrorMessage.setMessage("Проверьте корректность введенных учетных данных и попробуйте снова.");
        i18n.setErrorMessage(i18nErrorMessage);

        loginForm.setI18n(i18n);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        loginForm.setError(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error"));
    }
}
