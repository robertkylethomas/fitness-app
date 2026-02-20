package com.udacity.fitchallenge.repository;

import com.udacity.fitchallenge.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@ApplicationScoped  // ← ONE instance for entire app
public class UserRepository {

    @PersistenceContext(unitName = "FitChallengePU")
    private EntityManager entityManager;  // ← Your database remote!

    // We'll add methods here...

    /**
     * Find user by ID
     * Example: findById(5L) returns the user with id=5
     */
    public Optional<User> findById(Long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    /**
     * Get all users in the system
     */
    public List<User> findAll() {
        TypedQuery<User> query = entityManager.createNamedQuery("User.findAll", User.class);
        return query.getResultList();
    }





}