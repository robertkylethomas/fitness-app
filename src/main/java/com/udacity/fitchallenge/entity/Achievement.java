package com.udacity.fitchallenge.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


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

    @NotNull(message ="User cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message ="Achievement Type cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AchievementType type;

    @NotBlank(message ="Name cannot be empty")
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
