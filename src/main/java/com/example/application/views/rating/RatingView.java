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
        VerticalLayout gridLayout = new VerticalLayout();
        gridLayout.setWidth("AUTO");
        gridLayout.setHeight("100%");
        gridLayout.add(ratingGrid);
        add(ratingTypeRadioButtonGroup, gridLayout);
        setAlignItems(Alignment.CENTER);
        ratingGrid.setWidth("600px");
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

    }

    private void initRadioButtonGroupListener() {
        ratingTypeRadioButtonGroup.addValueChangeListener(event -> {
            if (event.getValue().equals(RatingType.BY_SCORE)) {
                ratingGrid.getColumnByKey("time").setVisible(false);
                ratingGrid.getColumnByKey("score").setVisible(true);
                ratingGrid.setItems(ratingService.getTop10RatingsByScore());
            }
            else {
                ratingGrid.getColumnByKey("score").setVisible(false);
                ratingGrid.getColumnByKey("time").setVisible(true);
                ratingGrid.setItems(ratingService.getTop10RatingsByTime());
            }
        });
    }
}
