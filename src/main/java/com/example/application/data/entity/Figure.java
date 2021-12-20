package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table
public class Figure extends AbstractEntity {

    @NotNull
    private String matrix;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Level> levels;

    public int[][] getMatrix() {
        try {
            String[] matrixSplit = matrix.split(",");
            int size = (int) Math.sqrt(matrixSplit.length);
            int[][] matrixArray = new int[size][size];
            int k = 0;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    matrixArray[i][j] = Integer.parseInt(matrixSplit[k]);
                    k++;
                }
            }
            return matrixArray;
        }
        catch (Exception e) {
            return null;
        }
    }

    public void setMatrix(int[][] matrix) {
        StringBuilder matrixString = new StringBuilder();
        for (int[] integers : matrix) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrixString.append(integers[j]).append(",");
            }
        }
        this.matrix = matrixString.substring(0, matrixString.length()-1);
    }

    public Set<Level> getLevels() {
        return levels;
    }

    public void setLevels(Set<Level> levels) {
        this.levels = levels;
    }
}
