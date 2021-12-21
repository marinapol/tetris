package com.example.application.views;

import com.example.application.security.SecurityService;
import com.example.application.security.SecurityUtils;
import com.example.application.views.figure.FigureView;
import com.example.application.views.game.GameView;
import com.example.application.views.grid.GridView;
import com.example.application.views.level.LevelView;
import com.example.application.views.rating.RatingView;
import com.example.application.views.settings.SettingsView;
import com.example.application.views.user.UserView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.Theme;

@Push
@Theme(themeFolder = "flowcrmtutorial")
public class MainLayout extends AppLayout {

    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
    }

    private void createHeader() {
        H1 logo = new H1("Тетрис");
        logo.addClassNames("text-l", "m-m");

        Button logoutButton = new Button("Выйти", buttonClickEvent -> securityService.logout());
        Tabs menuTabs = getMenuTabs();

        Label currentUserLabel = new Label(String.format("[%s]", SecurityUtils.getUser().getUsername()));
        HorizontalLayout header = new HorizontalLayout(logo, menuTabs, currentUserLabel, logoutButton);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassNames("py-0, px-m");
        header.expand(menuTabs);

        addToNavbar(header);
    }


    private Tabs getMenuTabs() {
        RouterLink userLink = new RouterLink("Пользователи", UserView.class);
        Tab usersTab = new Tab(userLink);

        RouterLink levelLink = new RouterLink("Настройка уровней сложности", LevelView.class);
        Tab levelsTab = new Tab(levelLink);

        RouterLink figureLink = new RouterLink("Фигуры", FigureView.class);
        Tab figuresTab = new Tab(figureLink);

        RouterLink gridLink = new RouterLink("Стаканы", GridView.class);
        Tab gridsTab = new Tab(gridLink);

        RouterLink ratingLink = new RouterLink("Рейтинг", RatingView.class);
        Tab ratingTab = new Tab(ratingLink);

        RouterLink gameLink = new RouterLink("Игра", GameView.class);
        Tab gameTab = new Tab(gameLink);

        RouterLink settingsLink = new RouterLink("Профиль", SettingsView.class);
        Tab settingsTab = new Tab(settingsLink);


        Tabs menuTabs = new Tabs();
        if (SecurityUtils.isUserHasRole("ROLE_ADMIN")) {
            menuTabs.add(usersTab, levelsTab, figuresTab, gridsTab);
        }
        if (SecurityUtils.isUserHasRole("ROLE_USER")) {
            menuTabs.add(settingsTab, gameTab);
        }
        menuTabs.add(ratingTab);

        menuTabs.addThemeVariants(TabsVariant.LUMO_CENTERED);
        return menuTabs;
    }


}
