package com.udacity.fitchallenge.event;

import com.udacity.fitchallenge.entity.Achievement;
import com.udacity.fitchallenge.entity.User;

/**
 * CDI event fired when a user earns an achievement.
 * This class is provided — no changes needed.
 */
public class AchievementEarnedEvent {

    private final User user;
    private final Achievement achievement;

    public AchievementEarnedEvent(User user, Achievement achievement) {
        this.user = user;
        this.achievement = achievement;
    }

    public User getUser() { return user; }
    public Achievement getAchievement() { return achievement; }
}