package com.udacity.fitchallenge.repository;

import com.udacity.fitchallenge.entity.ProgressLog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class ProgressLogRepository {

    @PersistenceContext(unitName = "FitChallengePU")
    private EntityManager entityManager;

    /**
     * Find a progress log entry by its ID — provided, no changes needed
     */
    public Optional<ProgressLog> findById(Long id) {
        ProgressLog log = entityManager.find(ProgressLog.class, id);
        return Optional.ofNullable(log);
    }

    /**
     * Get all progress logs for a challenge — provided, no changes needed
     */
    public List<ProgressLog> findByChallenge(Long challengeId) {
        TypedQuery<ProgressLog> query = entityManager
                .createNamedQuery("ProgressLog.findByChallenge", ProgressLog.class);
        query.setParameter("challengeId", challengeId);
        return query.getResultList();
    }

    /**
     * Get all progress logs for a user in a challenge — provided, no changes needed
     */
    public List<ProgressLog> findByUserAndChallenge(Long userId, Long challengeId) {
        TypedQuery<ProgressLog> query = entityManager
                .createNamedQuery("ProgressLog.findByUserAndChallenge", ProgressLog.class);
        query.setParameter("userId", userId);
        query.setParameter("challengeId", challengeId);
        return query.getResultList();
    }

    /**
     * Get all progress logs for a user across all challenges — provided, no changes needed
     */
    public List<ProgressLog> findByUser(Long userId) {
        TypedQuery<ProgressLog> query = entityManager.createQuery(
                "SELECT p FROM ProgressLog p WHERE p.user.id = :userId ORDER BY p.logDate DESC",
                ProgressLog.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    /**
     * Save a progress log — either insert or update
     *
     * HINT: Same pattern as save() in UserRepository and ChallengeRepository
     */
    public ProgressLog save(ProgressLog progressLog) {
        // TODO: Check if progressLog.getId() == null
        // If yes: call entityManager.persist(progressLog) and return progressLog
        // If no:  call entityManager.merge(progressLog) and return the result
        return null;
    }

    /**
     * Get total progress for a specific user in a specific challenge
     * Returns 0.0 if no progress logged yet
     */
    public Double getTotalProgress(Long userId, Long challengeId) {
        TypedQuery<Double> query = entityManager.createQuery(
                "SELECT SUM(p.value) FROM ProgressLog p " +
                        "WHERE p.user.id = :userId AND p.challenge.id = :challengeId",
                Double.class);
        query.setParameter("userId", userId);
        query.setParameter("challengeId", challengeId);
        Double result = query.getSingleResult();
        return result != null ? result : 0.0;
    }

    /**
     * Calculate total progress per user for a challenge — leaderboard order
     * Returns Map of userId → totalProgress, sorted by total descending
     *
     * IMPORTANT: This query returns Object[] rows, not ProgressLog entities.
     *   row[0] = userId (Long)
     *   row[1] = total progress — cast using ((Number) row[1]).doubleValue()
     * Use LinkedHashMap to preserve the sorted order from the query.
     */
    public Map<Long, Double> getTotalProgress(Long challengeId) {
        // TODO: Create a query using createNamedQuery("ProgressLog.sumByChallenge", Object[].class)
        // Set the :challengeId parameter
        // Create a new LinkedHashMap<Long, Double>
        // Loop over results — each row is Object[] where row[0]=userId, row[1]=total
        // Put each entry into the map and return it
        return null;
    }

    /**
     * Delete a progress log — provided, no changes needed
     */
    public void delete(ProgressLog progressLog) {
        if (entityManager.contains(progressLog)) {
            entityManager.remove(progressLog);
        } else {
            entityManager.remove(entityManager.merge(progressLog));
        }
    }

    /**
     * Delete progress log by ID — provided, no changes needed
     */
    public void deleteById(Long id) {
        findById(id).ifPresent(this::delete);
    }

    /**
     * Count progress logs for a challenge — provided, no changes needed
     */
    public long countByChallenge(Long challengeId) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(p) FROM ProgressLog p WHERE p.challenge.id = :challengeId",
                Long.class);
        query.setParameter("challengeId", challengeId);
        return query.getSingleResult();
    }
}