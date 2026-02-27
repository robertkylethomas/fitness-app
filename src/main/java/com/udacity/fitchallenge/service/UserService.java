package com.udacity.fitchallenge.service;

import com.udacity.fitchallenge.entity.User;
import com.udacity.fitchallenge.exception.ResourceNotFoundException;
import com.udacity.fitchallenge.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class UserService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    /**
     * Initialize password hash parameters — provided, no changes needed.
     * This MUST run before any password hashing. @PostConstruct runs
     * automatically once, right after WildFly creates this bean.
     */
    @PostConstruct
    public void init() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
        passwordHash.initialize(parameters);
    }

    /**
     * Create a new user — hash the password before saving.
     *
     * @Transactional wraps this method in a database transaction.
     * If anything fails, all changes are rolled back automatically.
     *
     * HINT: Hash the password like this:
     *   String hashed = passwordHash.generate(user.getPassword().toCharArray());
     *   user.setPassword(hashed);
     * HINT: Then call userRepository.save(user) and return the result
     */
    @Transactional
    public User createUser(User user) {
        // TODO: Hash the user's password using passwordHash.generate()
        // Then save and return the user using userRepository.save()
        return null;
    }

    /**
     * Find a user by ID — provided, no changes needed
     */
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    /**
     * Find a user by username — throw ResourceNotFoundException if not found
     *
     * HINT: userRepository.findByUsername() returns Optional<User>
     * HINT: Use .orElseThrow() like findById() above does
     */
    public User findByUsername(String username) {
        // TODO: Call userRepository.findByUsername(username)
        // Use .orElseThrow() to throw ResourceNotFoundException if not found
        return null;
    }

    /**
     * Get all users — provided, no changes needed
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Delete a user — provided, no changes needed
     */
    @Transactional
    public void deleteUser(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }

    /**
     * Returns the total number of users in the database.
     * Used by ApplicationLifecycleListener to skip seeding if data already exists.
     * Provided — no changes needed.
     */
    public long countUsers() {
        return userRepository.count();
    }
}