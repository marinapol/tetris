package com.example.application.views.level;

import com.example.application.data.entity.Grid;
import com.example.application.data.entity.Level;
import com.example.application.data.entity.LevelName;
import com.example.application.data.service.GridService;
import com.example.application.data.service.LevelService;
import com.example.application.data.utils.Constants;
import com.example.application.data.utils.Notifier;
import com.example.application.views.MainLayout;
import com.example.application.views.PaperSlider;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;

@Route(value = "admin/level", layout = MainLayout.class)
public class LevelView extends VerticalLayout {
    LevelService levelService;
    GridService gridService;

    RadioButtonGroup<LevelName> levelName;

    Level editedLevel;

    PaperSlider speed;
    Button applyButton;
    ComboBox<Grid> grid;

    public LevelView(GridService gridService, LevelService levelService) {
        this.gridService = gridService;
        this.levelService = levelService;

        add(getLevelSettingsLayout());
        initLevelNameChangeListener();
        initButtonClickListener();
        levelName.setValue(LevelName.LIGHT);

        setAlignItems(Alignment.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        setWidth("100%");
        setHeight("100%");
    }

    private VerticalLayout getLevelSettingsLayout() {
        VerticalLayout levelSettingsLayout = new VerticalLayout();
        levelSettingsLayout.setAlignItems(Alignment.CENTER);
        levelSettingsLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        levelSettingsLayout.setSizeFull();

        levelSettingsLayout.add(new Label(Constants.SELECT_LEVEL), getRadioGroup());
        levelSettingsLayout.add(new Label(Constants.FIGURE_SPEED), getSpeedSlider());
        levelSettingsLayout.add(new Label(Constants.GRID_SIZE), getGrid());
        levelSettingsLayout.add(getApplyButton());

        return levelSettingsLayout;
    }

    private RadioButtonGroup<LevelName> getRadioGroup() {
        levelName = new RadioButtonGroup<>();
        levelName.setItems(LevelName.values());
        levelName.setRenderer(new TextRenderer<>(LevelName::gelValue));

        return levelName;
    }

    private void initLevelNameChangeListener() {
        levelName.addValueChangeListener(event -> {
            editedLevel = levelService.getLevelByName(event.getValue());
            speed.setValue(editedLevel.getSpeed() != null ? editedLevel.getSpeed() : 1);
            grid.setValue(editedLevel.getGrid() != null ? editedLevel.getGrid() : null);

        });
    }

    private PaperSlider getSpeedSlider() {
        speed = new PaperSlider();
        speed.setMax(10);
        speed.setMin(1);
        speed.setPin(true);
        return speed;
    }

    private ComboBox<Grid> getGrid() {
        grid = new ComboBox<>();
        grid.setItems(gridService.getAllGrids());
        grid.setItemLabelGenerator(Grid::getGridSizeString);
        grid.setAllowCustomValue(false);
        return grid;
    }

    private Button getApplyButton() {
        applyButton = new Button();
        applyButton.setText(Constants.APPLY);
        return applyButton;
    }

    private void initButtonClickListener() {
        applyButton.addClickListener(buttonClickEvent -> {
            if(grid.isEmpty()) {
                Notifier.showWarningNotification(Constants.GRID_FIELD_IS_REQUIRED);
            }
            else {
                editedLevel.setSpeed(speed.getValue());
                editedLevel.setGrid(grid.getValue());
                levelService.saveLevel(editedLevel);
                Notifier.showSuccessNotification(Constants.LEVEL_EDITED_SUCCESS);
            }
        });
    }
}
