package com.udacity.fitchallenge.service;

import com.udacity.fitchallenge.entity.Challenge;
import com.udacity.fitchallenge.entity.ChallengeStatus;
import com.udacity.fitchallenge.entity.ChallengeType;
import com.udacity.fitchallenge.event.ChallengeCompletedEvent;
import com.udacity.fitchallenge.exception.ResourceNotFoundException;
import com.udacity.fitchallenge.repository.ChallengeRepository;
import com.udacity.fitchallenge.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ChallengeService {

    @Inject
    private ChallengeRepository challengeRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private Event<ChallengeCompletedEvent> challengeCompletedEvent;

    /**
     * Create a new challenge and link it to its creator.
     *
     * HINT: Find the creator: userRepository.findById(creatorId).orElseThrow(...)
     * HINT: Set creator and status: challenge.setCreator(creator)
     *                               challenge.setStatus(ChallengeStatus.ACTIVE)
     * HINT: Save and return: challengeRepository.save(challenge)
     */
    @Transactional
    public Challenge createChallenge(Challenge challenge, Long creatorId) {
        // TODO: Find the creator using userRepository.findById(creatorId)
        // throw ResourceNotFoundException if not found
        // Set the creator on the challenge using challenge.setCreator()
        // Set status to ACTIVE using challenge.setStatus(ChallengeStatus.ACTIVE)
        // Save and return the challenge using challengeRepository.save()
        return null;
    }

    /**
     * Add a user to a challenge's participant list.
     *
     * HINT: Find the challenge using getChallengeById(challengeId)
     * HINT: Find the user using userRepository.findById(userId).orElseThrow(...)
     * HINT: Check if already a participant:
     *   if (challenge.getParticipants().contains(user)) throw new IllegalStateException(...)
     * HINT: Add user: challenge.getParticipants().add(user)
     * HINT: Save and return: challengeRepository.save(challenge)
     */
    @Transactional
    public Challenge joinChallenge(Long challengeId, Long userId) {
        // TODO: Find the challenge using getChallengeById(challengeId)
        // Find the user using userRepository.findById(userId).orElseThrow()
        // Check if user is already a participant — throw IllegalStateException if yes
        // Add user to challenge.getParticipants()
        // Save and return the updated challenge
        return null;
    }

    /**
     * Get a challenge by ID — provided, no changes needed
     */
    public Challenge getChallengeById(Long id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge not found with id: " + id));
    }

    /**
     * Get all challenges — provided, no changes needed
     */
    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }

    /**
     * Get active challenges — provided, no changes needed
     */
    public List<Challenge> getActiveChallenges() {
        return challengeRepository.findActiveChallenges();
    }

    /**
     * Get challenges by type — provided, no changes needed
     */
    public List<Challenge> getChallengesByType(ChallengeType type) {
        return challengeRepository.findByType(type);
    }

    /**
     * Complete a challenge and fire CDI event — provided, no changes needed
     */
    @Transactional
    public Challenge completeChallenge(Long challengeId) {
        Challenge challenge = getChallengeById(challengeId);
        challenge.setStatus(ChallengeStatus.COMPLETED);
        Challenge updated = challengeRepository.save(challenge);
        challengeCompletedEvent.fire(new ChallengeCompletedEvent(updated));
        return updated;
    }

    /**
     * Returns all challenges with the given status.
     * Used by GET /api/challenges?status= for filtering.
     * Provided — no changes needed.
     */
    public List<Challenge> getChallengesByStatus(ChallengeStatus status) {
        return challengeRepository.findByStatus(status);
    }

    /**
     * Deletes a challenge by ID.
     * Throws ResourceNotFoundException if the challenge does not exist.
     * Provided — no changes needed.
     */
    @Transactional
    public void deleteChallenge(Long id) {
        Challenge challenge = getChallengeById(id);
        challengeRepository.delete(challenge);
    }
}