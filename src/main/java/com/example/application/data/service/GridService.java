package com.example.application.data.service;

import com.example.application.data.entity.Grid;

import com.example.application.data.repository.GridRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GridService {

    @Autowired
    GridRepository gridRepository;

    public List<Grid> getAllGrids() {
        return gridRepository.findAll();
    }

    public boolean saveGrid(Grid grid) {
        if (gridRepository.findByHeightAndWidth(grid.getHeight(), grid.getWidth()) != null) {
            return false;
        }
        gridRepository.save(grid);
        return true;
    }

    public boolean deleteGrid(Integer gridId) {
        if(gridRepository.findById(gridId).isPresent()) {
            gridRepository.deleteById(gridId);
            return true;
        }
        return false;
    }
}
