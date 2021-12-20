package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "level")
public class Level extends AbstractEntity {

    @NotNull
    private Integer speed;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "grid_id")
    private Grid grid;

    @NotNull
    @Enumerated(EnumType.STRING)
    private LevelName levelName;

    public Level() {
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public LevelName getLevelName() {
        return levelName;
    }

    public void setLevelName(LevelName levelName) {
        this.levelName = levelName;
    }
}
