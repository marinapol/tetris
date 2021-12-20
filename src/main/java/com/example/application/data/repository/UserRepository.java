package com.example.application.data.repository;

import com.example.application.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);
}
