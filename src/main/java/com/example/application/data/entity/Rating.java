package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Null;
import java.sql.Time;

@Entity
@Table
public class Rating extends AbstractEntity {

    @Column
    private Integer score;

    @Column
    private Time time;

    @ManyToOne
    private User user;

    public Rating() {
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
