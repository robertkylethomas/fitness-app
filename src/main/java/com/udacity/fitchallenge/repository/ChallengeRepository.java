package com.udacity.fitchallenge.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.udacity.fitchallenge.entity.Challenge;
import com.udacity.fitchallenge.entity.ChallengeStatus;
import com.udacity.fitchallenge.entity.ChallengeType;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@ApplicationScoped
public class ChallengeRepository {

    @PersistenceContext(unitName = "FitChallengePU")
    private EntityManager entityManager;

    /**
     * Find a challenge by its ID — provided, no changes needed
     */
    public Optional<Challenge> findById(Long id) {
        Challenge challenge = entityManager.find(Challenge.class, id);
        return Optional.ofNullable(challenge);
    }

    /**
     * Get all challenges — provided, no changes needed
     */
    public List<Challenge> findAll() {
        // TypedQuery<Challenge> query = entityManager
        //         .createNamedQuery("Challenge.findAll", Challenge.class);
        // return query.getResultList();

        List<Challenge> challenges = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

         Challenge c1 = new Challenge();
        c1.setTitle("Daily Step Challenge");
        c1.setDescription("Walk 10,000 steps every day.");
        c1.setType(ChallengeType.CYCLING);
        c1.setStatus(ChallengeStatus.ACTIVE);
        c1.setStartDate(now);
        c1.setEndDate(now.plusDays(30));
        c1.setGoalValue(10000);
        challenges.add(c1);

        Challenge c2 = new Challenge();
        c2.setTitle("Weekly Running Goal");
        c2.setDescription("Run 20 kilometres in one week.");
        c2.setType(ChallengeType.CYCLING);
        c2.setStatus(ChallengeStatus.ACTIVE);
        c2.setStartDate(now);
        c2.setEndDate(now.plusDays(7));
        c2.setGoalValue(20);
        challenges.add(c2);

        Challenge c3 = new Challenge();
        c3.setTitle("Push-Up Challenge");
        c3.setDescription("Complete 100 push-ups throughout the week.");
        c3.setType(ChallengeType.CYCLING);
        c3.setStatus(ChallengeStatus.ACTIVE);
        c3.setStartDate(now);
        c3.setEndDate(now.plusDays(7));
        c3.setGoalValue(100);
        challenges.add(c3);

        Challenge c4 = new Challenge();
        c4.setTitle("Cycling Challenge");
        c4.setDescription("Cycle 50 kilometres this month.");
        c4.setType(ChallengeType.CYCLING);
        c4.setStatus(ChallengeStatus.ACTIVE);
        c4.setStartDate(now);
        c4.setEndDate(now.plusDays(30));
        c4.setGoalValue(50);
        challenges.add(c4);

        Challenge c5 = new Challenge();
        c5.setTitle("Hydration Challenge");
        c5.setDescription("Drink 8 glasses of water every day.");
        c5.setType(ChallengeType.CYCLING);
        c5.setStatus(ChallengeStatus.ACTIVE);
        c5.setStartDate(now);
        c5.setEndDate(now.plusDays(14));
        c5.setGoalValue(8);
        challenges.add(c5);

        Challenge c6 = new Challenge();
        c6.setTitle("Monthly Fitness Goal");
        c6.setDescription("Complete 20 workout sessions this month.");
        c6.setType(ChallengeType.CYCLING);
        c6.setStatus(ChallengeStatus.ACTIVE);
        c6.setStartDate(now);
        c6.setEndDate(now.plusDays(30));
        c6.setGoalValue(20);
        challenges.add(c6);

        Challenge c7 = new Challenge();
        c7.setTitle("Swimming Challenge");
        c7.setDescription("Swim 2 kilometres over the next two weeks.");
        c7.setType(ChallengeType.CYCLING);
        c7.setStatus(ChallengeStatus.ACTIVE);
        c7.setStartDate(now);
        c7.setEndDate(now.plusDays(14));
        c7.setGoalValue(2);
        challenges.add(c7);

        Challenge c8 = new Challenge();
        c8.setTitle("Squat Challenge");
        c8.setDescription("Complete 500 squats over 30 days.");
        c8.setType(ChallengeType.CYCLING);
        c8.setStatus(ChallengeStatus.ACTIVE);
        c8.setStartDate(now);
        c8.setEndDate(now.plusDays(30));
        c8.setGoalValue(500);
        challenges.add(c8);

        Challenge c9 = new Challenge();
        c9.setTitle("Weekend Hiking Goal");
        c9.setDescription("Hike 15 kilometres over the weekend.");
        c9.setType(ChallengeType.CYCLING);
        c9.setStatus(ChallengeStatus.ACTIVE);
        c9.setStartDate(now);
        c9.setEndDate(now.plusDays(2));
        c9.setGoalValue(15);
        challenges.add(c9);

        Challenge c10 = new Challenge();
        c10.setTitle("30-Day Fitness Streak");
        c10.setDescription("Complete at least one workout every day for 30 days.");
        c10.setType(ChallengeType.CYCLING);
        c10.setStatus(ChallengeStatus.ACTIVE);
        c10.setStartDate(now);
        c10.setEndDate(now.plusDays(30));
        c10.setGoalValue(30);
        challenges.add(c10);


        return challenges;
    }

    /**
     * Get all active challenges — provided, no changes needed
     */
    public List<Challenge> findActiveChallenges() {
        TypedQuery<Challenge> query = entityManager
                .createNamedQuery("Challenge.findActive", Challenge.class);
        return query.getResultList();
    }

    /**
     * Find challenges by type
     * Uses the named query Challenge.findByType defined on the Challenge entity
     *
     * HINT: Same pattern as findActiveChallenges() above
     * HINT: Use query.setParameter("type", type) to pass the parameter
     */
    public List<Challenge> findByType(ChallengeType type) {
        // TODO: Create a TypedQuery using createNamedQuery("Challenge.findByType", Challenge.class)
        // Set the :type parameter using query.setParameter("type", type)
        // Return the result list
        return null;
    }

    /**
     * Save a challenge — either insert a new one or update an existing one
     * Rule: if challenge.getId() == null → new challenge → use persist()
     *       if challenge.getId() != null → existing challenge → use merge()
     *
     * HINT: Same pattern as save() in UserRepository
     */
    public Challenge save(Challenge challenge) {
        // TODO: Check if challenge.getId() == null
        // If yes: call entityManager.persist(challenge) and return challenge
        // If no:  call entityManager.merge(challenge) and return the result
        return null;
    }

    /**
     * Find challenges by status — provided, no changes needed
     */
    public List<Challenge> findByStatus(ChallengeStatus status) {
        TypedQuery<Challenge> query = entityManager.createQuery(
                "SELECT c FROM Challenge c WHERE c.status = :status ORDER BY c.startDate DESC",
                Challenge.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    /**
     * Find challenges by participant — provided, no changes needed
     */
    public List<Challenge> findByParticipant(Long userId) {
        TypedQuery<Challenge> query = entityManager.createQuery(
                "SELECT c FROM Challenge c JOIN c.participants p WHERE p.id = :userId ORDER BY c.startDate DESC",
                Challenge.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    /**
     * Find challenges created by a user — provided, no changes needed
     */
    public List<Challenge> findByCreator(Long creatorId) {
        TypedQuery<Challenge> query = entityManager.createQuery(
                "SELECT c FROM Challenge c WHERE c.creator.id = :creatorId ORDER BY c.startDate DESC",
                Challenge.class);
        query.setParameter("creatorId", creatorId);
        return query.getResultList();
    }

    /**
     * Delete a challenge — provided, no changes needed
     */
    public void delete(Challenge challenge) {
        if (entityManager.contains(challenge)) {
            entityManager.remove(challenge);
        } else {
            entityManager.remove(entityManager.merge(challenge));
        }
    }

    /**
     * Delete challenge by ID — provided, no changes needed
     */
    public void deleteById(Long id) {
        findById(id).ifPresent(this::delete);
    }

    /**
     * Count total challenges — provided, no changes needed
     */
    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Challenge c", Long.class);
        return query.getSingleResult();
    }

    /**
     * Count challenges by status — provided, no changes needed
     */
    public long countByStatus(ChallengeStatus status) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Challenge c WHERE c.status = :status", Long.class);
        query.setParameter("status", status);
        return query.getSingleResult();
    }
}
