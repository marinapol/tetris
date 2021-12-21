package com.example.application.data.repository;

import com.example.application.data.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

    @Query(value = "select * from Rating where score is not null order by score desc limit 10", nativeQuery = true)
    List<Rating> getTop10RatingsByScore();

    @Query(value = "select * from Rating where time is not null order by time desc limit 10", nativeQuery = true)
    List<Rating> getTop10RatingsByTime();
}
