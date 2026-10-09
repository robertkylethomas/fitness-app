package com.udacity.fitchallenge.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Challenge entity
 *
 * TODO (Phase 1): Add Bean Validation annotations
 * - @NotBlank and @Size on title
 * - @NotNull on type, status, startDate, endDate, goalValue
 * - @Min(1) on goalValue
 */
@Entity
@Table(name = "challenges")
@NamedQueries({
    @NamedQuery(
        name = "Challenge.findAll",
        query = "SELECT c FROM Challenge c ORDER BY c.startDate DESC"
    ),
    @NamedQuery(
        name = "Challenge.findActive",
        query = "SELECT c FROM Challenge c WHERE c.status = 'ACTIVE'"
    ),
    @NamedQuery(
        name = "Challenge.findByType",
        query = "SELECT c FROM Challenge c WHERE c.type = :type"
    )
})
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank
    @Size
    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 1000)
    private String description;


    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChallengeType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChallengeStatus status = ChallengeStatus.ACTIVE;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;


    @NotNull
    @Min(1)
    @Column(name = "goal_value")
    private Integer goalValue;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinTable(
        name = "challenge_participants",
        joinColumns = @JoinColumn(name = "challenge_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonbTransient
    private List<User> participants = new ArrayList<>();

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    @JsonbTransient
    private List<ProgressLog> progressLogs = new ArrayList<>();

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    public Challenge() {
    }

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Challenge{id=" + id + ", title='" + title + "', type=" + type + ", status=" + status + '}';
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ChallengeType getType() {
        return type;
    }

    public void setType(ChallengeType type) {
        this.type = type;
    }

    public ChallengeStatus getStatus() {
        return status;
    }

    public void setStatus(ChallengeStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getGoalValue() {
        return goalValue;
    }

    public void setGoalValue(Integer goalValue) {
        this.goalValue = goalValue;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public List<ProgressLog> getProgressLogs() {
        return progressLogs;
    }

    public void setProgressLogs(List<ProgressLog> progressLogs) {
        this.progressLogs = progressLogs;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
