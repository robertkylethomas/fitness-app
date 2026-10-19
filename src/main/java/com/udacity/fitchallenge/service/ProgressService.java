package com.udacity.fitchallenge.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.udacity.fitchallenge.entity.Achievement;
import com.udacity.fitchallenge.entity.AchievementType;
import com.udacity.fitchallenge.entity.Challenge;
import com.udacity.fitchallenge.entity.ProgressLog;
import com.udacity.fitchallenge.entity.User;
import com.udacity.fitchallenge.event.AchievementEarnedEvent;
import com.udacity.fitchallenge.repository.ChallengeRepository;
import com.udacity.fitchallenge.repository.ProgressLogRepository;
import com.udacity.fitchallenge.repository.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ProgressService {

    @Inject
    private ProgressLogRepository progressLogRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private ChallengeRepository challengeRepository;

    @Inject
    private Event<AchievementEarnedEvent> achievementEvent;

    @PersistenceContext(unitName = "FitChallengePU")
    private EntityManager entityManager;

    /**
     * Save a progress log entry, then check if the user has earned an achievement.
     *
     * Steps:
     *   1. Validate that user and challenge IDs are present
     *   2. Find the full User and Challenge objects from their repositories
     *   3. Set them on the progressLog
     *   4. Save the progressLog using progressLogRepository.save()
     *   5. Call checkAndAwardAchievements(user, challenge)
     *   6. Return the saved log
     *
     * HINT: throw IllegalArgumentException if user or challenge is null
     * HINT: use userRepository.findById(...).orElseThrow(...)
     * HINT: use challengeRepository.findById(...).orElseThrow(...)
     */
    @Transactional
    public ProgressLog logProgress(ProgressLog progressLog) {
        // TODO: Validate user and challenge are not null
        // Find the User from userRepository using progressLog.getUser().getId()
        // Find the Challenge from challengeRepository using progressLog.getChallenge().getId()
        // Set both on the progressLog
        // Save using progressLogRepository.save()
        // Call checkAndAwardAchievements(user, challenge)
        // Return the saved log

         User user = userRepository.findById(progressLog.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Challenge challenge = challengeRepository.findById(progressLog.getChallenge().getId())
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found"));

        progressLog.setChallenge(challenge);
        progressLog.setUser(user);

        progressLogRepository.save(progressLog);
        checkAndAwardAchievements(user, challenge);

        return progressLog;
    }

    /**
     * Calculate leaderboard for a challenge — provided, no changes needed
     */
    public Map<Long, Double> calculateLeaderboard(Long challengeId) {
        return progressLogRepository.getTotalProgress(challengeId);
    }

    /**
     * Get progress logs for a challenge — provided, no changes needed
     */
    public List<ProgressLog> getProgressByChallenge(Long challengeId) {
        return progressLogRepository.findByChallenge(challengeId);
    }

    /**
     * Get progress logs for a specific user in a challenge — provided, no changes needed
     */
    public List<ProgressLog> getProgressByUserAndChallenge(Long userId, Long challengeId) {
        return progressLogRepository.findByUserAndChallenge(userId, challengeId);
    }

    /**
     * Check progress and award achievements — provided, no changes needed
     */
    private void checkAndAwardAchievements(User user, Challenge challenge) {
        List<ProgressLog> userProgress = progressLogRepository
                .findByUserAndChallenge(user.getId(), challenge.getId());
        int progressCount = userProgress.size();

        if (progressCount == 7) {
            Achievement achievement = new Achievement(
                    user, AchievementType.STREAK_7,
                    "7 Day Streak", "Logged progress for 7 consecutive days"
            );
            entityManager.persist(achievement);
            achievementEvent.fire(new AchievementEarnedEvent(user, achievement));
        } else if (progressCount == 30) {
            Achievement achievement = new Achievement(
                    user, AchievementType.STREAK_30,
                    "30 Day Streak", "Logged progress for 30 consecutive days"
            );
            entityManager.persist(achievement);
            achievementEvent.fire(new AchievementEarnedEvent(user, achievement));
        }

        Double totalProgress = progressLogRepository.getTotalProgress(user.getId(), challenge.getId());
        if (totalProgress != null && challenge.getGoalValue() != null &&
                totalProgress >= challenge.getGoalValue()) {
            Achievement achievement = new Achievement(
                    user, AchievementType.GOAL_ACHIEVED,
                    "Goal Achieved", "Reached the challenge goal of " + challenge.getGoalValue()
            );
            entityManager.persist(achievement);
            achievementEvent.fire(new AchievementEarnedEvent(user, achievement));
        }
    }

    /**
     * Returns all progress logs for a given user across all challenges.
     * Provided — no changes needed.
     */
    public List<ProgressLog> getProgressByUser(Long userId) {
        return progressLogRepository.findByUser(userId);
    }

    /**
     * Returns leaderboard data enriched with username and rank for display.
     * Calls calculateLeaderboard() and looks up each user by ID.
     * Provided — no changes needed.
     */
    public List<Map<String, Object>> getLeaderboardWithUserDetails(Long challengeId) {
        Map<Long, Double> leaderboard = calculateLeaderboard(challengeId);

        List<Map<String, Object>> result = new ArrayList<>();
        int rank = 1;

        for (Map.Entry<Long, Double> entry : leaderboard.entrySet()) {
            User user = userRepository.findById(entry.getKey()).orElse(null);
            if (user != null) {
                Map<String, Object> userScore = new HashMap<>();
                userScore.put("rank", rank++);
                userScore.put("userId", user.getId());
                userScore.put("username", user.getUsername());
                userScore.put("totalProgress", entry.getValue());
                result.add(userScore);
            }
        }

        return result;
    }

    /**
     * Deletes a progress log entry by ID.
     * Provided — no changes needed.
     */
    @Transactional
    public void deleteProgressLog(Long id) {
        progressLogRepository.deleteById(id);
    }
}
