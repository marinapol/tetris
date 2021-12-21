package com.example.application.data.service;

import com.example.application.data.entity.Role;
import com.example.application.data.entity.User;
import com.example.application.data.entity.UserSettings;
import com.example.application.data.repository.LevelRepository;
import com.example.application.data.repository.RoleRepository;
import com.example.application.data.repository.UserRepository;
import com.example.application.data.repository.UserSettingsRepository;
import com.example.application.data.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserSettingsRepository userSettingsRepository;
    @Autowired
    LevelRepository levelRepository;
    @Autowired
    EntityManager entityManager;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(login);

        if (user == null) {
            throw new UsernameNotFoundException(Constants.USERNAME_NOT_FOUND);
        }

        return user;
    }

    public User getUserById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(new User());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllPlayers() {
        return userRepository.findAllPlayers("ROLE_USER");
    }

    @Transactional
    public boolean saveUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return false;
        }
        user.setRoles(Collections.singleton(new Role(1, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        /*if (userSettingsRepository.findByUserId(user.getId()) == null) {
            //entityManager.persist(user);
            UserSettings userSettings = new UserSettings();
            entityManager.merge(user);
            userSettings.setRatingEnable(false);
            userSettings.setLevel(levelRepository.findById(1).get());
            userSettings.setNextFigureVisible(false);
            userSettings.setGridVisible(false);
            userSettings.setUser(user);
            user.setUserSettings(userSettings);
            userRepository.save(user);
        }*/
        //userSettingsRepository.save(userSettings);


        return true;
    }

    public void updateUser(User user, boolean isPassChanged) {
        if (isPassChanged) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    public boolean deleteUser(Integer userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }
}
