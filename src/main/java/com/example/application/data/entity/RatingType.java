package com.example.application.data.entity;

public enum RatingType {

    BY_TIME("По времени"),
    BY_SCORE("По количеству очков");

    private final String value;

    RatingType(String value) {
        this.value = value;
    }

    public String gelValue() {
        return this.value;
    }
}
