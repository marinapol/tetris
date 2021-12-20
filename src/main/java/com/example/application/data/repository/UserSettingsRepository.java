package com.example.application.data.repository;

import com.example.application.data.entity.User;
import com.example.application.data.entity.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Integer> {

    UserSettings findByUserId(Integer userId);
}
