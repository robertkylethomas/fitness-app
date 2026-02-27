package com.udacity.fitchallenge.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

/**
 * Achievement entity
 * 
 * TODO (Phase 1): Add Bean Validation annotations
 * - @NotNull on user, type
 * - @NotBlank on name
 */
@Entity
@Table(name = "achievements")
@NamedQueries({
    @NamedQuery(
        name = "Achievement.findByUser",
        query = "SELECT a FROM Achievement a WHERE a.user.id = :userId ORDER BY a.earnedDate DESC"
    ),
    @NamedQuery(
        name = "Achievement.findByType",
        query = "SELECT a FROM Achievement a WHERE a.type = :type"
    )
})
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: Add @NotNull validation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // TODO: Add @NotNull validation
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AchievementType type;

    // TODO: Add @NotBlank validation
    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "earned_date", nullable = false)
    private LocalDateTime earnedDate;

    public Achievement() {
    }

    public Achievement(User user, AchievementType type, String name, String description) {
        this.user = user;
        this.type = type;
        this.name = name;
        this.description = description;
        this.earnedDate = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (earnedDate == null) {
            earnedDate = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public AchievementType getType() { return type; }
    public void setType(AchievementType type) { this.type = type; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDateTime getEarnedDate() { return earnedDate; }
    public void setEarnedDate(LocalDateTime earnedDate) { this.earnedDate = earnedDate; }

    @Override
    public String toString() {
        return "Achievement{id=" + id + ", type=" + type + ", name='" + name + '\'' + '}';
    }
}
