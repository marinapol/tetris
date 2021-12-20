package com.example.application.views.rating;

import com.example.application.data.entity.Rating;
import com.example.application.data.entity.RatingType;
import com.example.application.data.service.RatingService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;

@Route(value = "rating", layout = MainLayout.class)
public class RatingView extends VerticalLayout {

    RatingService ratingService;

    Grid<Rating> ratingGrid = new Grid<>(Rating.class);
    RadioButtonGroup<RatingType> ratingTypeRadioButtonGroup = new RadioButtonGroup<>();

    public RatingView(RatingService ratingService) {
        this.ratingService = ratingService;
        setSizeFull();

        initRadioButtonGroup();
        initRatingGrid();
        add(ratingTypeRadioButtonGroup, ratingGrid);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setAlignItems(Alignment.CENTER);
        setHorizontalComponentAlignment(Alignment.CENTER);
        ratingTypeRadioButtonGroup.setValue(RatingType.BY_SCORE);

    }

    private void initRadioButtonGroup() {
        ratingTypeRadioButtonGroup.setItems(RatingType.values());
        ratingTypeRadioButtonGroup.setRenderer(new TextRenderer<>(RatingType::gelValue));
        initRadioButtonGroupListener();
    }

    private void initRatingGrid() {
        //ratingGrid.setSizeFull();

        ratingGrid.removeAllColumns();
        ratingGrid.addColumn(new TextRenderer<>(rating -> rating.getUser().getUsername())).setHeader("Пользователь");
        ratingGrid.addColumn("score").setHeader("Очки");
        ratingGrid.addColumn("time").setHeader("Время");
        ratingGrid.setItems(ratingService.getAllRatings());

    }

    private void initRadioButtonGroupListener() {
        ratingTypeRadioButtonGroup.addValueChangeListener(event -> {
            if (event.getValue().equals(RatingType.BY_SCORE)) {
                ratingGrid.getColumnByKey("time").setVisible(false);
                ratingGrid.getColumnByKey("score").setVisible(true);
            }
            else {
                ratingGrid.getColumnByKey("score").setVisible(false);
                ratingGrid.getColumnByKey("time").setVisible(true);
            }
        });
    }
}
