package com.udacity.fitchallenge.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * ProgressLog entity
 *
 * TODO (Phase 1): Add Bean Validation annotations
 * - @NotNull on user, challenge, value, logDate
 * - @Min(0) on value
 * - @Size on notes (max=500)
 */
@Entity
@Table(name = "progress_logs")
@NamedQueries({
        @NamedQuery(
                name = "ProgressLog.findByChallenge",
                query = "SELECT p FROM ProgressLog p WHERE p.challenge.id = :challengeId ORDER BY p.logDate DESC"
        ),
        @NamedQuery(
                name = "ProgressLog.findByUserAndChallenge",
                query = "SELECT p FROM ProgressLog p WHERE p.user.id = :userId AND p.challenge.id = :challengeId ORDER BY p.logDate DESC"
        ),
        @NamedQuery(
                name = "ProgressLog.sumByChallenge",
                query = "SELECT p.user.id, SUM(p.value) FROM ProgressLog p WHERE p.challenge.id = :challengeId GROUP BY p.user.id ORDER BY SUM(p.value) DESC"
        )
})
public class ProgressLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // TODO: Add @NotNull validation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    // TODO: Add @NotNull and @Min(0) validation
    @Column(name = "progress_value", nullable = false)
    private Double value;

    // TODO: Add @NotNull validation
    @Column(name = "log_date", nullable = false)
    private LocalDateTime logDate;

    // TODO: Add @Size validation
    @Column(length = 500)
    private String notes;

    public ProgressLog() {
    }

    public ProgressLog(User user, Challenge challenge, Double value) {
        this.user = user;
        this.challenge = challenge;
        this.value = value;
        this.logDate = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (logDate == null) {
            logDate = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Challenge getChallenge() { return challenge; }
    public void setChallenge(Challenge challenge) { this.challenge = challenge; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }

    public LocalDateTime getLogDate() { return logDate; }
    public void setLogDate(LocalDateTime logDate) { this.logDate = logDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "ProgressLog{id=" + id + ", value=" + value + ", logDate=" + logDate + '}';
    }
}
