package com.example.application.data.service;

import com.example.application.data.entity.Figure;
import com.example.application.data.entity.Grid;
import com.example.application.data.entity.Level;
import com.example.application.data.repository.FigureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FigureService {

    @Autowired
    FigureRepository figureRepository;

    public List<Figure> getAllFigures() {
        return figureRepository.findByOrderByIdAsc();
    }

    public Figure getById(Integer figureId) {
        return figureRepository.findById(figureId).orElse(null);
    }

    public List<Figure> getByLevel(Level level) {
        return figureRepository.findByLevel(level);
    }

    public boolean save(Figure figure) {
        figureRepository.save(figure);
        return true;
    }


    public boolean delete(Integer figureId) {
        if(figureRepository.findById(figureId).isPresent()) {
            figureRepository.deleteById(figureId);
            return true;
        }
        return false;
    }


}
