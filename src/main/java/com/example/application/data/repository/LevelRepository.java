package com.example.application.data.repository;

import com.example.application.data.entity.Grid;
import com.example.application.data.entity.Level;
import com.example.application.data.entity.LevelName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelRepository extends JpaRepository<Level, Integer> {
    Level findLevelByLevelName(LevelName levelName);

    boolean existsLevelByGrid(Grid grid);
}
