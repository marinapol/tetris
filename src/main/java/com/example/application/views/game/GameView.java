package com.example.application.views.game;

import com.example.application.ExtCanvas;
import com.example.application.data.entity.Rating;
import com.example.application.data.entity.RatingType;
import com.example.application.data.entity.User;
import com.example.application.data.entity.UserSettings;
import com.example.application.data.service.FigureService;
import com.example.application.data.service.RatingService;
import com.example.application.data.service.UserSettingsService;
import com.example.application.data.utils.Game;
import com.example.application.data.utils.Notifier;
import com.example.application.security.SecurityUtils;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.sql.Time;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Route(value = "game", layout = MainLayout.class)
public class GameView extends VerticalLayout {

    UserSettingsService userSettingsService;
    FigureService figureService;
    RatingService ratingService;

    VerticalLayout gameFieldAndControlsLayout;
    VerticalLayout gameFieldLayout;
    VerticalLayout gridLayout = new VerticalLayout();

    VerticalLayout nextFigureLayout;
    VerticalLayout nextFigure = new VerticalLayout();

    HorizontalLayout controlButtonsLayout = new HorizontalLayout();

    UserSettings userSettings;
    User user;

    Thread stepThread;
    Thread timeThread;

    int cellSize = 0;

    Game currentGame;

    Button startGame;
    Button down;
    Button left;
    Button right;
    Button rotate;
    Label score;
    Label time;
    Button stop;


    @Override
    protected void onDetach(DetachEvent detachEvent) {
        if (stepThread != null && stepThread.isAlive()) {
            stepThread.interrupt();
            stepThread = null;
        }
        if (timeThread != null && timeThread.isAlive()) {
            timeThread.interrupt();
            timeThread = null;
        }
    }

    public GameView(UserSettingsService userSettingsService, FigureService figureService, RatingService ratingService) {
        this.userSettingsService = userSettingsService;
        this.figureService = figureService;
        this.ratingService = ratingService;

        startGame = new Button("Начать игру");
        startGame.addClickListener(buttonClickEvent -> initNewGame());

        down = new Button(new Icon(VaadinIcon.ARROW_DOWN));
        down.addClickListener(buttonClickEvent -> down());
        down.addClickShortcut(Key.ARROW_DOWN);
        left = new Button(new Icon(VaadinIcon.ARROW_LEFT));
        left.addClickListener(buttonClickEvent -> left());
        left.addClickShortcut(Key.ARROW_LEFT);
        right = new Button(new Icon(VaadinIcon.ARROW_RIGHT));
        right.addClickListener(buttonClickEvent -> right());
        right.addClickShortcut(Key.ARROW_RIGHT);
        rotate = new Button(new Icon(VaadinIcon.ROTATE_RIGHT));
        rotate.addClickListener(buttonClickEvent -> rotate());
        rotate.addClickShortcut(Key.SPACE);

        stop = new Button(new Icon(VaadinIcon.STOP));
        stop.setEnabled(false);
        stop.addClickListener(buttonClickEvent -> {
            if (currentGame != null) {
                currentGame.setGameOver(true);
            }
        });

        score = new Label("Очки: 0");
        time = new Label("Время: 00:00");

        user = SecurityUtils.getUser();
        if (user != null) {
            userSettings = userSettingsService.getByUserId(user.getId());
        }
        HorizontalLayout mainLayout = new HorizontalLayout();
        VerticalLayout buttonsAndScoreLayout = new VerticalLayout(new HorizontalLayout(startGame, stop), score, time);
        controlButtonsLayout.add(left, right, down, rotate);

        gameFieldLayout = new VerticalLayout(getEmptyGameField());
        gameFieldAndControlsLayout = new VerticalLayout(gameFieldLayout, controlButtonsLayout);
        gameFieldAndControlsLayout.setAlignItems(Alignment.CENTER);
        gameFieldAndControlsLayout.setHeight("700px");
        Label nextFigureLabel = new Label("Следующая фигура");
        nextFigureLabel.setWidth("250px");
        nextFigureLayout = new VerticalLayout(nextFigureLabel, nextFigure);
        nextFigureLayout.setVisible(userSettings.isNextFigureVisible());
        mainLayout.add(buttonsAndScoreLayout, gameFieldAndControlsLayout, nextFigureLayout);
        add(mainLayout);
        setAlignItems(Alignment.CENTER);
    }

    private void initNewGame() {
        currentGame = new Game(userSettings, figureService.getByLevel(userSettings.getLevel()));
        cellSize = 680 / currentGame.getHeight();
        startGame.setEnabled(false);
        stop.setEnabled(true);

        gameFieldLayout.removeAll();
        nextFigureLayout.remove(nextFigure);
        gridLayout = currentGame.getGameField(cellSize);
        nextFigure = currentGame.getNextFigure(40);
        gameFieldLayout.add(gridLayout);
        nextFigureLayout.add(nextFigure);
        stepThread = new StepTread(getUI().get(), this);
        timeThread = new TimeThread(getUI().get(), this);
        stepThread.start();
        timeThread.start();
    }

    private void left() {
        currentGame.left();
        gameFieldLayout.remove(gridLayout);
        nextFigureLayout.remove(nextFigure);
        gridLayout = currentGame.getGameField(cellSize);
        nextFigure = currentGame.getNextFigure(40);
        gameFieldLayout.add(gridLayout);
        nextFigureLayout.add(nextFigure);
    }

    private void right() {
        currentGame.right();
        gameFieldLayout.remove(gridLayout);
        nextFigureLayout.remove(nextFigure);
        gridLayout = currentGame.getGameField(cellSize);
        nextFigure = currentGame.getNextFigure(40);
        gameFieldLayout.add(gridLayout);
        nextFigureLayout.add(nextFigure);
    }

    private void rotate() {
        currentGame.rotateCW();
        gameFieldLayout.remove(gridLayout);
        nextFigureLayout.remove(nextFigure);
        gridLayout = currentGame.getGameField(cellSize);
        nextFigure = currentGame.getNextFigure(40);
        gameFieldLayout.add(gridLayout);
        nextFigureLayout.add(nextFigure);
    }

    private void down() {
        currentGame.pullTetrominoDown();
        gameFieldLayout.remove(gridLayout);
        nextFigureLayout.remove(nextFigure);
        gridLayout = currentGame.getGameField(cellSize);
        nextFigure = currentGame.getNextFigure(40);
        gameFieldLayout.add(gridLayout);
        nextFigureLayout.add(nextFigure);
    }

    private VerticalLayout getEmptyGameField() {
        int width = userSettings.getLevel().getGrid().getWidth();
        int height = userSettings.getLevel().getGrid().getHeight();
        int cellSize = 680 / height;

        VerticalLayout figureRowsLayout = new VerticalLayout();
        figureRowsLayout.setWidth("AUTO");
        figureRowsLayout.setSpacing(false);

        if (userSettings.isGridVisible()) {
            for (int i = 0; i < height; i++) {
                HorizontalLayout figureRowLayout = new HorizontalLayout();
                figureRowLayout.setSpacing(false);
                for (int j = 0; j < width; j++) {
                    ExtCanvas cell = new ExtCanvas(cellSize, cellSize);
                    cell.getContext().setStrokeStyle("black");
                    cell.getContext().rect(0, 0, cellSize, cellSize);
                    cell.getContext().stroke();
                    figureRowLayout.add(cell);
                }
                figureRowsLayout.add(figureRowLayout);
            }
        }
        if (!userSettings.isGridVisible()) {
            figureRowsLayout.setWidth(width*cellSize, Unit.PIXELS);
            figureRowsLayout.setHeight(height*cellSize, Unit.PIXELS);
            figureRowsLayout.getStyle().set( "border" , "1px solid black");
            figureRowsLayout.setPadding(false);
        }
        return figureRowsLayout;
    }

    private static class StepTread extends  Thread {
        private final UI ui;
        private final GameView view;

        public StepTread(UI ui,  GameView view) {
            this.ui = ui;
            this.view = view;
        }

        @Override
        public void run() {
            int stepTime = 2300 - 200 * view.userSettings.getLevel().getSpeed();
            while (!interrupted()) {
                try {
                    Thread.sleep(stepTime);
                    ui.access(() -> {
                        view.currentGame.step();
                        view.gameFieldLayout.remove(view.gridLayout);
                        view.nextFigureLayout.remove(view.nextFigure);
                        view.gridLayout = view.currentGame.getGameField(view.cellSize);
                        view.nextFigure = view.currentGame.getNextFigure(40);
                        view.gameFieldLayout.add(view.gridLayout);
                        view.nextFigureLayout.add(view.nextFigure);
                        view.score.setText(String.format("Очки: %d", view.currentGame.getScore()));
                        if(view.currentGame.isGameOver()) {
                            Thread.currentThread().interrupt();
                        }
                    });
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            ui.access(() -> {
                Notifier.showWarningNotification("Игра окончена");
                view.startGame.setEnabled(true);
                view.stop.setEnabled(false);
            });
            Thread.currentThread().interrupt();
        }
    }

    private static class TimeThread extends  Thread {
        private final UI ui;
        private final GameView view;

        public TimeThread(UI ui,  GameView view) {
            this.ui = ui;
            this.view = view;
        }

        @Override
        public void run() {
            while (!interrupted()) {
                try {
                    Thread.sleep(1000);
                    ui.access(() -> {
                        view.currentGame.setTime(view.currentGame.getTime() + 1000);
                        view.time.setText(String.format("Время: %02d:%02d", TimeUnit.MILLISECONDS.toMinutes(view.currentGame.getTime()),
                                TimeUnit.MILLISECONDS.toSeconds(view.currentGame.getTime()) - TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(view.currentGame.getTime()))));
                        if(view.currentGame.isGameOver()) {
                            Thread.currentThread().interrupt();
                        }
                    });

                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            ui.access(() -> {
                if (view.userSettings.isRatingEnable()) {
                    Rating rating = new Rating();
                    rating.setUser(view.user);
                    if (view.userSettings.getRatingType() == RatingType.BY_SCORE) {
                        rating.setScore(view.currentGame.getScore());
                        rating.setTime(null);
                    } else if (view.userSettings.getRatingType() == RatingType.BY_TIME) {
                        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
                        rating.setTime(new Time(view.currentGame.getTime()));
                        rating.setScore(null);
                    }
                    view.ratingService.save(rating);
                }
            });
            Thread.currentThread().interrupt();
        }
    }
}
