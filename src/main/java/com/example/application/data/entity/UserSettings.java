package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;

import javax.persistence.*;

@Entity
@Table
public class UserSettings extends AbstractEntity {

    @OneToOne
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private Level level;

    private boolean gridVisible;

    private boolean nextFigureVisible;

    private boolean ratingEnable;

    @Enumerated(EnumType.STRING)
    private RatingType ratingType;

    public UserSettings() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public boolean isGridVisible() {
        return gridVisible;
    }

    public void setGridVisible(boolean gridVisible) {
        this.gridVisible = gridVisible;
    }

    public boolean isNextFigureVisible() {
        return nextFigureVisible;
    }

    public void setNextFigureVisible(boolean nextFigureVisible) {
        this.nextFigureVisible = nextFigureVisible;
    }

    public boolean isRatingEnable() {
        return ratingEnable;
    }

    public void setRatingEnable(boolean ratingEnable) {
        this.ratingEnable = ratingEnable;
    }

    public RatingType getRatingType() {
        return ratingType;
    }

    public void setRatingType(RatingType ratingType) {
        this.ratingType = ratingType;
    }
}
