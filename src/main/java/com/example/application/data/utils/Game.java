package com.example.application.data.utils;

import com.example.application.ExtCanvas;
import com.example.application.data.entity.Figure;
import com.example.application.data.entity.UserSettings;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Game {

    int width;
    int height;
    int[][] grid;
    long time;
    Integer score;
    boolean isGameOver;
    List<Figure> availableFigures;
    Tetromino activeTetromino;
    Tetromino nextTetromino;
    UserSettings userSettings;

    private static final Map<Integer, String> COLORS = Map.of(
            1, "#0ff",
            2, "#00f",
            3, "#ffa500",
            4, "#ff0",
            5, "#0f0",
            6, "#800080",
            7, "#f00"
    );
    int pos_x, pos_y;

    Random rnd = new Random();

    public Game(UserSettings userSettings, List<Figure> availableFigures) {
        this.userSettings = userSettings;
        width = userSettings.getLevel().getGrid().getWidth();
        height = userSettings.getLevel().getGrid().getHeight();
        this.grid = new int[height][width];
        this.availableFigures = availableFigures;
        time = 0;
        score = 0;
        isGameOver = false;
        activeTetromino = new Tetromino(availableFigures.get(rnd.nextInt(availableFigures.size())), rnd.nextInt(7)+1);
        nextTetromino = new Tetromino(availableFigures.get(rnd.nextInt(availableFigures.size())), rnd.nextInt(7)+1);


        pos_x = (width - activeTetromino.getWidth()) / 2;
        pos_y = 0;
    }

    public Integer getScore() {
        return score;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public int getHeight() {
        return height;
    }

    public void step() {
        if (isTetrominoDone()) {
            for (int i = 0; i < activeTetromino.getHeight(); i++) {
                for (int j = 0; j < activeTetromino.getWidth(); j++) {
                    if (activeTetromino.getFig()[i][j] > 0) {
                        grid[i+pos_y][j+pos_x] = activeTetromino.getColor();
                    }
                }
            }
            activeTetromino = nextTetromino;
            nextTetromino = new Tetromino(availableFigures.get(rnd.nextInt(availableFigures.size())), rnd.nextInt(7)+1);
            pos_x = (width - activeTetromino.getWidth()) / 2;
            pos_y = 0;
            checkGameOver();
        }
        else {
            pos_y++;
        }
        removeFullLines();
    }

    public void left() {
        int newPosX = pos_x - 1;
        if (newPosX >= 0 && !isFigureOverlay(activeTetromino.getFig(), pos_y, newPosX)) {
            pos_x = newPosX;
        }
    }

    public void right() {
        int newPosX = pos_x + 1;
        if (newPosX + activeTetromino.getWidth() < width + 1 && !isFigureOverlay(activeTetromino.getFig(), pos_y, newPosX)) {
            pos_x = newPosX;
        }

    }

    private void removeFullLines() {
        for (int i = 0; i < height; i++) {
            boolean isLineFull = true;
            for (int j = 0; j < width; j++) {
                if (grid[i][j] == 0) {
                    isLineFull = false;
                    break;
                }
            }
            if (isLineFull) {
                removeLine(i);
                score = score+10;
            }
        }
    }

    private void removeLine(int lineIndex) {
        for(int i = lineIndex - 1; i >= 0; i--) {
            for (int j = 0; j < width; j++) {
                grid[i+1][j] = grid[i][j];
            }
        }
        for (int j = 0; j < width; j++) {
            grid[0][j] = 0;
        }
    }

    private void checkGameOver() {
        if (isFigureOverlay(activeTetromino.getFig(), pos_y, pos_x)) {
            isGameOver = true;
        }
    }

    public void pullTetrominoDown() {
        while (!isTetrominoDone()) {
            step();
        }
    }

    private boolean isTetrominoDone() {

        for (int j = 0; j < activeTetromino.getWidth(); j++) {
            List<Integer> yIndexes = new ArrayList<>();
            for (int i = 0; i < activeTetromino.getHeight(); i++) {
                if (activeTetromino.getFig()[i][j] > 0 && i == activeTetromino.getHeight() - 1
                        || activeTetromino.getFig()[i][j] > 0 && activeTetromino.getFig()[i+1][j] == 0) {
                    yIndexes.add(i);
                }
            }

            for(Integer yIndex : yIndexes) {
                if (pos_y + yIndex == height - 1 || grid[pos_y + yIndex+1][pos_x + j] > 0) {
                    return true;
                }
            }
        }
        return false;
    }


    public VerticalLayout getGameField(int cellSize) {
        int[][] tmpGrid = copyGrid(grid);
        for (int i = 0; i < activeTetromino.getHeight(); i++) {
            for (int j = 0; j < activeTetromino.getWidth(); j++) {
                if (activeTetromino.getFig()[i][j] > 0) {
                    tmpGrid[i+pos_y][j+pos_x] = activeTetromino.getColor();
                }
            }
        }
        VerticalLayout figureRowsLayout = new VerticalLayout();
        figureRowsLayout.setWidth("AUTO");
        figureRowsLayout.setSpacing(false);

        for (int i = 0; i < height; i++) {
            HorizontalLayout figureRowLayout = new HorizontalLayout();
            figureRowLayout.setSpacing(false);
            for (int j = 0; j < width; j++) {
                ExtCanvas cell = new ExtCanvas(cellSize, cellSize);
                if (tmpGrid[i][j] != 0) {
                    cell.getContext().setFillStyle(COLORS.get(tmpGrid[i][j]));
                    cell.getContext().fillRect(0, 0, cellSize, cellSize);
                } else if (userSettings.isGridVisible()) {
                    cell.getContext().setStrokeStyle("black");
                    cell.getContext().rect(0, 0, cellSize, cellSize);
                    cell.getContext().stroke();
                }
                figureRowLayout.add(cell);
            }
            figureRowsLayout.add(figureRowLayout);
        }
        if (!userSettings.isGridVisible()) {
            figureRowsLayout.getStyle().set( "border" , "1px solid black");
            figureRowsLayout.setPadding(false);
        }
        return figureRowsLayout;
    }

    public VerticalLayout getNextFigure(int cellSize) {
        int[][] figureMatrix = nextTetromino.getFig();

        VerticalLayout figureRowsLayout = new VerticalLayout();
        figureRowsLayout.setWidth("AUTO");
        figureRowsLayout.setSpacing(false);

        for (int i = 0; i < figureMatrix.length; i++) {
            HorizontalLayout figureRowLayout = new HorizontalLayout();
            figureRowLayout.setSpacing(false);
            for (int j = 0; j < figureMatrix[0].length; j++) {
                ExtCanvas cell = new ExtCanvas(cellSize, cellSize);
                if (figureMatrix[i][j] != 0) {
                    cell.getContext().setFillStyle(COLORS.get(nextTetromino.getColor()));
                    cell.getContext().fillRect(0, 0, cellSize, cellSize);
                }
                figureRowLayout.add(cell);
            }
            figureRowsLayout.add(figureRowLayout);
        }
        return figureRowsLayout;
    }

    public void rotateCW() {

        int[][] rotatedFigure = rotateFigureCW(activeTetromino.getFig());
        int new_pos_x = pos_x;
        int new_pos_y = pos_y;
        if (pos_x + rotatedFigure[0].length > width) {
            new_pos_x = pos_x - (pos_x + rotatedFigure[0].length - width);
        }

        if (pos_y + rotatedFigure.length > height) {
            new_pos_y = pos_y - (pos_y + rotatedFigure.length - height);
        }

        if (!isFigureOverlay(rotatedFigure, new_pos_y, new_pos_x)) {
            activeTetromino.setFig(rotatedFigure);
            activeTetromino.setHeight(activeTetromino.getFig().length);
            activeTetromino.setWidth(activeTetromino.getFig()[0].length);
            pos_x = new_pos_x;
            pos_y = new_pos_y;
        }
    }

    private boolean isFigureOverlay(int[][] figure, int new_pos_y, int new_pos_x) {
        for (int i = 0; i < figure.length; i++) {
            for (int j = 0; j < figure[0].length; j++) {
                if (figure[i][j] != 0 && grid[new_pos_y + i][new_pos_x + j] != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private int[][] copyGrid(int[][] grid) {
        int[][] newGrid = new int[grid.length][grid[0].length];
        for (int i = 0; i < newGrid.length; i++) {
            for (int j = 0; j < newGrid[0].length; j++) {
                newGrid[i][j] = grid[i][j];
            }
        }
        return newGrid;
    }

    private int[][] rotateFigureCW(int[][] figure) {
        int[][] newFigure = new int[figure[0].length][figure.length];
        for (int i = 0; i < figure.length; i++) {
            for (int j = 0; j < figure[0].length; j++) {
                newFigure[j][figure.length - 1 - i] = figure[i][j];
            }
        }
        return newFigure;
    }


}
