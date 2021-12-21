package com.example.application.data.service;

import com.example.application.data.entity.Grid;
import com.example.application.data.entity.Level;
import com.example.application.data.entity.LevelName;
import com.example.application.data.repository.LevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelService {

    @Autowired
    LevelRepository levelRepository;

    public List<Level> getAllLevels() {
        return levelRepository.findAll();
    }

    public Level getLevelByName(LevelName name) {
        return levelRepository.findLevelByLevelName(name);
    }
    public void saveLevel(Level level) {
        levelRepository.save(level);
    }

    public boolean existsLevelByGrid(Grid grid) {
        return levelRepository.existsLevelByGrid(grid);
    }
}
