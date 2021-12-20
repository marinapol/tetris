package com.example.application.data.utils;

import com.example.application.data.entity.Figure;

public class Tetromino {

    private int [][] fig;
    private int width;
    private int height;
    private int color;

    public Tetromino(Figure figure, int color) {
        createTetromino(figure);
        this.color = color;
    }

    public int[][] getFig() {
        return fig;
    }

    public void setFig(int[][] fig) {
        this.fig = fig;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private void createTetromino(Figure figure) {
        int[][] figureMatrix = figure.getMatrix();
        int iStart = -1, iEnd = -1, jStart = -1, jEnd = -1;
        for (int i = 0; i < figureMatrix.length; i++) {
            boolean isCellPresent = false;
            for (int j = 0; j < figureMatrix[0].length; j++) {
                if (figureMatrix[i][j] == 1) {
                    isCellPresent = true;
                    break;
                }
            }
            if (iStart == -1 && isCellPresent) {
                iStart = i;
                iEnd = iStart;
            } else {
                if (isCellPresent) {
                    iEnd = i;
                }
            }
        }
        for (int j = 0; j < figureMatrix[0].length; j++) {
            boolean isCellPresent = false;
            for (int i = 0; i < figureMatrix.length; i++) {
                if (figureMatrix[i][j] == 1) {
                    isCellPresent = true;
                    break;
                }
            }
            if (jStart == -1 && isCellPresent) {
                jStart = j;
                jEnd = jStart;
            } else {
                if (isCellPresent) {
                    jEnd = j;
                }
            }
        }
        int[][] newTetromino = new int[iEnd - iStart + 1][jEnd - jStart + 1];
        for (int i = 0; i < newTetromino.length; i++) {
            for (int j = 0; j < newTetromino[0].length; j++) {
                newTetromino[i][j] = figureMatrix[iStart+i][jStart+j];
            }
        }
        fig = newTetromino;
        width = fig[0].length;
        height = fig.length;
    }
}
