package com.udacity.fitchallenge.event;

import com.udacity.fitchallenge.entity.Challenge;

/**
 * CDI event fired when a challenge is marked as completed.
 * This class is provided — no changes needed.
 */
public class ChallengeCompletedEvent {

    private final Challenge challenge;

    public ChallengeCompletedEvent(Challenge challenge) {
        this.challenge = challenge;
    }

    public Challenge getChallenge() { return challenge; }
}