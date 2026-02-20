# FitChallenge - Student Implementation Guide

## Overview

You will implement a complete Jakarta EE 11 fitness challenge platform by following these 7 phases.


## Phase 1: Add Bean Validation 

### Task: Add validation annotations to entity classes

**Files to modify:**
- `src/main/java/com/udacity/fitchallenge/entity/User.java`
- `src/main/java/com/udacity/fitchallenge/entity/Challenge.java`
- `src/main/java/com/udacity/fitchallenge/entity/ProgressLog.java`
- `src/main/java/com/udacity/fitchallenge/entity/Achievement.java`

**What to do:**
1. Add import: `import jakarta.validation.constraints.*;`
2. Follow the TODO comments in each entity
3. Add appropriate validation annotations

**Example for User.java:**
```java
@NotBlank(message = "Username is required")
@Size(min = 3, max = 50, message = "Username must be 3-50 characters")
@Column(unique = true, nullable = false, length = 50)
private String username;
```

---

## Phase 2: Implement Repository Layer 

### Task: Create 3 repository classes

Create these files in `src/main/java/com/udacity/fitchallenge/repository/`:

### 2.1: UserRepository.java

```java
package com.udacity.fitchallenge.repository;

import com.udacity.fitchallenge.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepository {

    @PersistenceContext(unitName = "FitChallengePU")
    private EntityManager entityManager;

    public Optional<User> findById(Long id) {
        // TODO: Implement
    }

    public Optional<User> findByUsername(String username) {
        // TODO: Use named query "User.findByUsername"
    }

    public List<User> findAll() {
        // TODO: Use named query "User.findAll"
    }

    public User save(User user) {
        // TODO: persist if new, merge if existing
    }

    public void delete(User user) {
        // TODO: Implement
    }
}
```

**Hints:**
- Use `entityManager.find()` for findById
- Use `entityManager.createNamedQuery()` for named queries
- Check if `user.getId() == null` to determine persist vs merge

### 2.2: ChallengeRepository.java

Follow the same pattern. Implement:
- `findById(Long id)`
- `findAll()`
- `findActiveChallenges()` - use named query
- `findByType(ChallengeType type)`
- `save(Challenge challenge)`
- `delete(Challenge challenge)`

### 2.3: ProgressLogRepository.java

Implement:
- `findById(Long id)`
- `findByChallenge(Long challengeId)`
- `findByUserAndChallenge(Long userId, Long challengeId)`
- `save(ProgressLog progressLog)`
- `getTotalProgress(Long userId, Long challengeId)` - use SUM query

---

## Phase 3: Implement Service Layer 

### Task: Create 3 service classes with CDI

### 3.1: UserService.java

```java
package com.udacity.fitchallenge.service;

import com.udacity.fitchallenge.entity.User;
import com.udacity.fitchallenge.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    @Transactional
    public User createUser(User user) {
        // TODO: Hash password before saving
        // String hashedPassword = passwordHash.generate(user.getPassword().toCharArray());
    }

    public User findById(Long id) {
        // TODO: Implement
    }

    public User findByUsername(String username) {
        // TODO: Implement
    }
}
```

### 3.2: ChallengeService.java

Key methods to implement:
- `createChallenge(Challenge challenge, Long creatorId)`
- `joinChallenge(Long challengeId, Long userId)`
- `completeChallenge(Long challengeId)` - fire ChallengeCompletedEvent

### 3.3: ProgressService.java

Key methods:
- `logProgress(ProgressLog progressLog)` - check for achievements
- `calculateLeaderboard(Long challengeId)` - return Map<UserId, TotalProgress>

---

## Phase 4: Implement REST API 

### Task: Create JAX-RS resources

### 4.1: ChallengeResource.java

```java
package com.udacity.fitchallenge.rest;

import com.udacity.fitchallenge.service.ChallengeService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/challenges")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChallengeResource {

    @Inject
    private ChallengeService challengeService;

    @GET
    public Response getAllChallenges() {
        // TODO: Return all challenges
    }

    @GET
    @Path("/{id}")
    public Response getChallengeById(@PathParam("id") Long id) {
        // TODO: Return challenge by ID
    }

    @POST
    public Response createChallenge(Challenge challenge, @QueryParam("creatorId") Long creatorId) {
        // TODO: Create and return created challenge with 201 status
    }
}
```

### 4.2: ProgressResource.java

Implement:
- POST /progress - log progress
- GET /progress/challenge/{id} - get all progress for challenge

---

## Phase 5: Implement Security 

### Task: Create DatabaseIdentityStore

```java
package com.udacity.fitchallenge.security;

import com.udacity.fitchallenge.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

@ApplicationScoped
public class DatabaseIdentityStore implements IdentityStore {

    @Inject
    private UserRepository userRepository;

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        // TODO: 
        // 1. Cast to UsernamePasswordCredential
        // 2. Find user by username
        // 3. Verify password using passwordHash.verify()
        // 4. Return CredentialValidationResult with username and roles
    }
}
```

---

## Phase 6: Implement Web Interface 

### Task: Create servlets

### 6.1: DashboardServlet.java

```java
package com.udacity.fitchallenge.servlet;

import com.udacity.fitchallenge.service.ChallengeService;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Inject
    private ChallengeService challengeService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // TODO:
        // 1. Get active challenges
        // 2. Generate HTML output
        // 3. Display challenges in cards
    }
}
```

---

## Phase 7: Testing & Deployment

### Build and Deploy

```bash
mvn clean package
mvn wildfly:deploy
```

### Test REST API

```bash
# Get all challenges
curl http://localhost:8080/fitchallenge/api/challenges

# Create a challenge
curl -X POST 'http://localhost:8080/fitchallenge/api/challenges?creatorId=1' \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Challenge",
    "type": "RUNNING",
    "goalValue": 50,
    "startDate": "2026-03-01T00:00:00",
    "endDate": "2026-03-15T23:59:59"
  }'
```

---

## Verification Checklist

-  All entities have validation annotations
-  All 3 repositories implemented
-  All 3 services implemented with CDI
-  REST API returns data
-  Security validates credentials
-  Dashboard displays challenges
-  Application deploys without errors

