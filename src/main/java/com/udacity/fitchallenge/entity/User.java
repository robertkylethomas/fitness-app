package com.udacity.fitchallenge.entity;

import jakarta.persistence.*;
import jakarta.json.bind.annotation.JsonbTransient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User entity
 * 
 * TODO (Phase 1): Add Bean Validation annotations
 * - @NotBlank on username, password, email
 * - @Size on username (min=3, max=50)
 * - @Size on password (min=8)
 * - @Email on email
 */
@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(
        name = "User.findByUsername",
        query = "SELECT u FROM User u WHERE u.username = :username"
    ),
    @NamedQuery(
        name = "User.findAll",
        query = "SELECT u FROM User u ORDER BY u.createdDate DESC"
    )
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: Add @NotBlank and @Size validation annotations
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    // TODO: Add @NotBlank and @Size validation annotations
    @Column(nullable = false)
    @JsonbTransient
    private String password;

    // TODO: Add @NotBlank and @Email validation annotations
    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @ManyToMany(mappedBy = "participants")
    @JsonbTransient
    private List<Challenge> joinedChallenges = new ArrayList<>();

    public User() {
    }

    public User(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<Challenge> getJoinedChallenges() {
        return joinedChallenges;
    }

    public void setJoinedChallenges(List<Challenge> joinedChallenges) {
        this.joinedChallenges = joinedChallenges;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', role=" + role + '}';
    }
}
