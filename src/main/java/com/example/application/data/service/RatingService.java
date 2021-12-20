package com.example.application.data.service;

import com.example.application.data.entity.Rating;
import com.example.application.data.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    public void save(Rating rating) {
        ratingRepository.save(rating);
    }
}
