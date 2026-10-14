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

    
    public Optional<User> findByUsername(String username) {
       
        try{
          TypedQuery<User> query = entityManager
                  .createNamedQuery("User.findByUsername", User.class);
          return Optional.of(query.getSingleResult());

        } catch(NoResultException nre){
          return Optional.empty();
        }
    }

    
    public User save(User user) {
       

        if(user.getId() == null){
          entityManager.persist(user);
        } else {
          entityManager.merge(user);
        }
        return user;
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
