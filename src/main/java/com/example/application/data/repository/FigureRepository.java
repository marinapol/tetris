package com.example.application.data.repository;

import com.example.application.data.entity.Figure;
import com.example.application.data.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FigureRepository extends JpaRepository<Figure, Integer> {

    List<Figure> findByOrderByIdAsc();

    @Query("select f from Figure f left join f.levels lvls where :level in lvls")
    List<Figure> findByLevel(@Param("level") Level level);
}
