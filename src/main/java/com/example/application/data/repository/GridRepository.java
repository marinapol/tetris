package com.example.application.data.repository;

import com.example.application.data.entity.Grid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GridRepository extends JpaRepository<Grid, Integer> {

    Grid findByHeightAndWidth(Integer height, Integer width);
}
