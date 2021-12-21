package com.example.application.data.listener;

import com.example.application.data.entity.LevelName;
import com.example.application.data.entity.User;
import com.example.application.data.entity.UserSettings;
import com.example.application.data.service.LevelService;
import com.example.application.data.service.UserSettingsService;
import com.example.application.data.utils.AutowireHelper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

public class UserListener {

    @Autowired
    private UserSettingsService userSettingsService;

    @Autowired
    private LevelService levelService;

    @PrePersist
    public void postPersist(User user) {
        AutowireHelper.autowire(this, this.userSettingsService, this.levelService);

        UserSettings userSettings = new UserSettings();
        userSettings.setRatingEnable(false);
        userSettings.setLevel(levelService.getLevelByName(LevelName.LIGHT));
        userSettings.setNextFigureVisible(false);
        userSettings.setGridVisible(false);
        userSettings.setUser(user);
        user.setUserSettings(userSettings);
    }
}
