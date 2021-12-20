package com.example.application.data.entity;

import com.example.application.data.utils.Constants;

public enum LevelName {
    LIGHT(Constants.LEVEL_LIGHT_VALUE),
    MEDIUM(Constants.LEVEL_MEDIUM_VALUE),
    HARD(Constants.LEVEL_HARD_VALUE);

    private final String value;

    LevelName(String value) {
        this.value = value;
    }

    public String gelValue() {
        return this.value;
    }
}
