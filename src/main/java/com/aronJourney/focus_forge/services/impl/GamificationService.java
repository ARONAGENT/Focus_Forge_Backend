package com.aronJourney.focus_forge.services.impl;

import org.springframework.stereotype.Service;

@Service
public class GamificationService {

    private static final int POINTS_PER_HOUR = 20;

    public Integer calculateSessionPoints(Integer duration) {
        return (duration * POINTS_PER_HOUR) / 60;
    }
}