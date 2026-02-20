# FitChallenge - STARTER CODE

**Jakarta EE 11 Capstone Project**

This is the **STARTER CODE** for students. You will implement the missing pieces to create a complete fitness challenge platform.

## 🎯 What You Have

✅ **Complete Configuration** - pom.xml, persistence.xml, beans.xml
✅ **Complete Entity Classes** - User, Challenge, ProgressLog, Achievement (WITHOUT validation)
✅ **All Enums** - Role, ChallengeType, ChallengeStatus, AchievementType
✅ **Helper Classes** - Exceptions, Events, Config
✅ **Project Structure** - All packages ready for your code

## 🚧 What You Need to Implement

### Phase 1: Add Bean Validation (20 minutes)
Add validation annotations to all entity classes:
- User: @NotBlank, @Size, @Email
- Challenge: @NotBlank, @NotNull, @Min
- ProgressLog: @NotNull, @Min
- Achievement: @NotNull, @NotBlank

### Phase 2: Implement Repositories (45 minutes)
Create 3 repository classes:
- **UserRepository.java** - CRUD + findByUsername
- **ChallengeRepository.java** - CRUD + findActive, findByType
- **ProgressLogRepository.java** - CRUD + findByChallenge, sumByChallenge

### Phase 3: Implement Services (60 minutes)
Create 3 service classes with CDI:
- **UserService.java** - User management + password hashing
- **ChallengeService.java** - Challenge CRUD + join/leave + events
- **ProgressService.java** - Progress logging + leaderboard calculation

### Phase 4: Implement REST API (75 minutes)
Create JAX-RS resources:
- **ChallengeResource.java** - Complete CRUD endpoints
- **ProgressResource.java** - Progress logging endpoints
- Exception mappers for error handling

### Phase 5: Implement Security (45 minutes)
Create authentication:
- **DatabaseIdentityStore.java** - Validate credentials against database

### Phase 6: Implement Web Interface (60 minutes)
Create servlets:
- **DashboardServlet.java** - Display active challenges
- **AuthenticationFilter.java** - Protect servlet pages

### Phase 7: Testing & Deployment (30 minutes)
- Test with curl
- Deploy to WildFly
- Verify all functionality

**Total Time: ~5.5 hours**

## 🚀 Quick Start

### 1. Configure WildFly Datasource

```bash
cd ~/tools/jakartaee-servers/wildfly-32.0.1.Final/bin
./jboss-cli.sh --connect

data-source add --name=FitChallengeDS \
    --jndi-name=java:jboss/datasources/FitChallengeDS \
    --driver-name=h2 \
    --connection-url=jdbc:h2:mem:fitchallengedb;DB_CLOSE_DELAY=-1 \
    --user-name=sa \
    --password=sa \
    --enabled=true

reload
exit
```

### 2. Build & Deploy

```bash
cd FitChallenge-StarterCode
mvn clean package
mvn wildfly:deploy
```

### 3. Test

```bash
curl http://localhost:8080/fitchallenge/api/challenges
```

## 📋 Implementation Guide

Follow the student instructions document for detailed step-by-step guidance on implementing each component.

## 🎯 Learning Objectives

After completing this project, you will be able to:
- ✅ Create JPA entities with validation
- ✅ Implement repository pattern with EntityManager
- ✅ Use CDI for dependency injection
- ✅ Create event-driven architecture with CDI events
- ✅ Build REST APIs with JAX-RS
- ✅ Implement security with DatabaseIdentityStore
- ✅ Create servlets with filters and listeners
- ✅ Deploy Jakarta EE applications to WildFly

## 📚 Resources

- Jakarta EE 11 Documentation
- WildFly Documentation
- Student Instructions (provided separately)
- Solution Code (for reference after completion)

---

**Good luck with your implementation!** 🚀
