package com.udacity.fitchallenge.repository;

import com.udacity.fitchallenge.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepository {

    @PersistenceContext(unitName = "FitChallengePU")
    private EntityManager entityManager;

    /**
     * Find user by ID
     */
    public Optional<User> findById(Long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    /**
     * Find all users
     */
    public List<User> findAll() {
        TypedQuery<User> query = entityManager
                .createNamedQuery("User.findAll", User.class);
        return query.getResultList();
    }

    /**
     * Find a user by their username
     * Returns Optional.empty() if no user found with that username
     *
     * HINT: Use createNamedQuery("User.findByUsername", User.class)
     * HINT: Use query.setParameter("username", username)
     * HINT: Use getSingleResult() wrapped in try/catch for NoResultException
     */
    public Optional<User> findByUsername(String username) {
        // TODO: Create a TypedQuery using createNamedQuery("User.findByUsername", User.class)
        // Set the :username parameter using query.setParameter("username", username)
        // Use getSingleResult() — return Optional.of(result) if found, Optional.empty() if NoResultException
        return null;
    }

    /**
     * Save a user — either insert a new one or update an existing one
     * Rule: if user.getId() == null → new user → use persist()
     *       if user.getId() != null → existing user → use merge()
     */
    public User save(User user) {
        // TODO: Check if user.getId() == null
        // If yes: call entityManager.persist(user) and return user
        // If no:  call entityManager.merge(user) and return the result
        return null;
    }

    /**
     * Delete a user — provided, no changes needed
     */
    public void delete(User user) {
        if (entityManager.contains(user)) {
            entityManager.remove(user);
        } else {
            entityManager.remove(entityManager.merge(user));
        }
    }

    /**
     * Delete user by ID — provided, no changes needed
     */
    public void deleteById(Long id) {
        findById(id).ifPresent(this::delete);
    }

    /**
     * Check if username exists — provided, no changes needed
     */
    public boolean existsByUsername(String username) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class);
        query.setParameter("username", username);
        return query.getSingleResult() > 0;
    }

    /**
     * Check if email exists — provided, no changes needed
     */
    public boolean existsByEmail(String email) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class);
        query.setParameter("email", email);
        return query.getSingleResult() > 0;
    }

    /**
     * Count total users — provided, no changes needed
     */
    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u", Long.class);
        return query.getSingleResult();
    }
}