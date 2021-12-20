package com.example.application.views.settings;

import com.example.application.data.entity.*;
import com.example.application.data.repository.UserSettingsRepository;
import com.example.application.data.service.LevelService;
import com.example.application.data.service.UserSettingsService;
import com.example.application.data.utils.Notifier;
import com.example.application.security.SecurityUtils;
import com.example.application.views.MainLayout;
import com.example.application.views.figure.FigureForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.AbstractDataProvider;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;

@Route(value = "settings", layout = MainLayout.class)
public class SettingsView extends VerticalLayout {

    HorizontalLayout settingsLayout = new HorizontalLayout();
    RadioButtonGroup<Level> levels;
    Checkbox isNextFigureVisible;
    Checkbox isGridVisible;
    Checkbox isRatingEnable;
    RadioButtonGroup<RatingType> ratingType;

    Button save;

    UserSettingsService userSettingsService;
    LevelService levelService;
    Binder<UserSettings> binder = new BeanValidationBinder<>(UserSettings.class);

    UserSettings userSettings;
    User user;

    public SettingsView(UserSettingsService userSettingsService, LevelService levelService) {
        this.userSettingsService = userSettingsService;
        this.levelService = levelService;
        user = SecurityUtils.getUser();

        initUserSettings(userSettingsService);
        initLevelNameRadioGroup();
        initShowNextFigureCheckBox();
        initShowGridCheckBox();
        initIsRatingEnableCheckBox();
        initRatingType();
        initSaveButton();

        VerticalLayout levelDifficulty = new VerticalLayout(new Label("Уровень сложности"), levels);
        VerticalLayout additionalSettings = new VerticalLayout(new Label("Дополнительные настройки"), isNextFigureVisible, isGridVisible);
        VerticalLayout rating = new VerticalLayout(new Label("Рейтинг"), isRatingEnable, ratingType);
        additionalSettings.getStyle().set( "border" , "1px solid blue" ) ;
        levelDifficulty.getStyle().set( "border" , "1px solid blue" ) ;
        rating.getStyle().set( "border" , "1px solid blue" ) ;

        settingsLayout.add(levelDifficulty, additionalSettings, rating);
        settingsLayout.setAlignItems(Alignment.CENTER);
        setAlignItems(Alignment.CENTER);
        add(settingsLayout, save);

        binder.forField(levels).bind("level");
        binder.forField(isNextFigureVisible).bind("nextFigureVisible");
        binder.forField(isGridVisible).bind("gridVisible");
        binder.forField(isRatingEnable).bind("ratingEnable");
        binder.forField(ratingType).bind("ratingType");
        binder.readBean(userSettings);

        ratingType.setEnabled(isRatingEnable.getValue());
        isRatingEnable.addValueChangeListener(valueChangeEvent -> ratingType.setEnabled(valueChangeEvent.getValue()));
    }

    private void validateAndSave() {
        try {
            binder.writeBean(userSettings);
            userSettingsService.save(userSettings);
            Notifier.showSuccessNotification("Настройки сохранены");

        } catch (ValidationException validationException) {
            validationException.printStackTrace();
            Notifier.showWarningNotification("Произошла ошибка");
        }
    }

    private void initUserSettings(UserSettingsService userSettingsService) {
        if (user != null) {
            if(userSettingsService.getByUserId(user.getId()) == null) {
                userSettings = createUserSettings();
            }
            else {
                userSettings = userSettingsService.getByUserId(user.getId());
            }
            /*userSettings = userSettingsService.getByUserId(user.getId());
            if (userSettings == null) {
                userSettings = createUserSettings();
            }*/
        }
    }

    private UserSettings createUserSettings() {
        UserSettings userSettings = new UserSettings();
        userSettings.setUser(user);
        userSettings.setLevel(levelService.getLevelByName(LevelName.LIGHT));
        userSettings.setNextFigureVisible(false);
        userSettings.setGridVisible(false);
        userSettings.setRatingEnable(true);
        userSettings.setRatingType(RatingType.BY_SCORE);
        return userSettings;
    }

    private void initLevelNameRadioGroup() {
        levels = new RadioButtonGroup<>();
        levels.setItems(levelService.getAllLevels());
        levels.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        levels.setRenderer(new TextRenderer<>(level -> level.getLevelName().gelValue()));
    }

    private void initShowNextFigureCheckBox() {
        isNextFigureVisible = new Checkbox("Отображать следующую фигуру");
    }

    private void initShowGridCheckBox() {
        isGridVisible = new Checkbox("Отображать сетку игрового поля");
    }

    private void initIsRatingEnableCheckBox() {
        isRatingEnable = new Checkbox("Вести подсчёт результатов");
    }

    private void initRatingType() {
        ratingType = new RadioButtonGroup<>();
        ratingType.setItems(RatingType.values());
        ratingType.setRenderer(new TextRenderer<>(RatingType::gelValue));
    }

    private void initSaveButton() {
        save = new Button("Применить");
        save.addClickListener(buttonClickEvent -> validateAndSave());
    }


}
