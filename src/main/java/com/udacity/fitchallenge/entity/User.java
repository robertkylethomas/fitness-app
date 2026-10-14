package com.udacity.fitchallenge.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

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

    // TODO: Add @NotBlank and @Size validation annotations - done
    @NotBlank(message="Username cannot be blank")
    @Min(value = 3, message="Username cannot be shorter than 3 characters")
    @Max(value = 50, message="Username cannot be longer than 50 characters")
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    // TODO: Add @NotBlank and @Size validation annotations
    @NotBlank(message="Password cannot be blank")
    @Min (value = 8, message="Password cannot be shorter than 8 characters")
    @Column(nullable = false)
    @JsonbTransient
    private String password;

    // TODO: Add @NotBlank and @Email validation annotations
    @NotBlank(message="Email Address cannot be blank")
    @Email(message="Invalid email")
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
