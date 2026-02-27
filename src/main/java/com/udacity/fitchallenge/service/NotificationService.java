package com.udacity.fitchallenge.service;

import com.udacity.fitchallenge.event.AchievementEarnedEvent;

import com.udacity.fitchallenge.event.ChallengeCompletedEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Listens for CDI events and logs notifications.
 * This class is provided — no changes needed.
 *
 * @Observes tells CDI: "call this method automatically whenever
 * this event type is fired anywhere in the application."
 */
@ApplicationScoped
public class NotificationService {

    private static final Logger LOGGER = Logger.getLogger(NotificationService.class.getName());

    public void onAchievementEarned(@Observes AchievementEarnedEvent event) {
        LOGGER.log(Level.INFO,
                "Achievement Earned! User: {0}, Achievement: {1}",
                new Object[]{event.getUser().getUsername(), event.getAchievement().getName()});
    }

    public void onChallengeCompleted(@Observes ChallengeCompletedEvent event) {
        LOGGER.log(Level.INFO,
                "Challenge Completed! Challenge: {0}",
                event.getChallenge().getTitle());
    }
}