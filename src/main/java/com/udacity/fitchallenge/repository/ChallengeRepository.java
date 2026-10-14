package com.udacity.fitchallenge.repository;

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
        TypedQuery<Challenge> query = entityManager
                .createNamedQuery("Challenge.findAll", Challenge.class);
        return query.getResultList();
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

        TypedQuery<Challenge> query =
                              entityManager.createNamedQuery("Challenge.findByType", Challenge.class)
                              .setParameter("type", type);
        return query.getResultList();
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

        if(challenge.getId() == null){
          entityManager.persist(challenge);
        } else {
          entityManager.merge(challenge);
        }
        return challenge;
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
