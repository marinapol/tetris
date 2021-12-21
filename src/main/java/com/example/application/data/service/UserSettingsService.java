package com.example.application.data.service;

import com.example.application.data.entity.User;
import com.example.application.data.entity.UserSettings;
import com.example.application.data.repository.LevelRepository;
import com.example.application.data.repository.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSettingsService {

    @Autowired
    private UserSettingsRepository userSettingsRepository;
    @Autowired
    private LevelRepository levelRepository;

    public UserSettings getByUserId(Integer userId) {
        return userSettingsRepository.findByUserId(userId);
    }

    public void save(UserSettings userSettings) {
        userSettingsRepository.save(userSettings);
    }

}
