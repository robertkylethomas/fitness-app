package com.udacity.fitchallenge.listener;

import com.udacity.fitchallenge.entity.*;
import com.udacity.fitchallenge.service.ChallengeService;
import com.udacity.fitchallenge.service.ProgressService;
import com.udacity.fitchallenge.service.UserService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.time.LocalDateTime;
import java.util.logging.Logger;

/**
 * Application lifecycle listener — seeds sample data on startup.
 * Provided — no changes needed.
 *
 * WHY THIS EXISTS:
 * @WebListener + ServletContextListener lets you run code at two points
 * in the application lifecycle:
 *   - contextInitialized() — runs when the application starts
 *   - contextDestroyed()   — runs when the application shuts down
 *
 * This class uses contextInitialized() to seed the database with sample
 * users, challenges, and progress logs so every checkpoint curl command
 * has data to work with immediately after deployment.
 *
 * WHAT IT CREATES:
 * Users:      admin / admin123 (ADMIN)
 *             manager / manager123 (MANAGER)
 *             john / john123 (USER)
 *             jane / jane123 (USER)
 *
 * Challenges: 30 Day Running Challenge (goal: 100 km)
 *             Weekly Cycling Goal (goal: 50 km)
 *             Push-up Challenge (goal: 1000 reps)
 *
 * Progress:   john → 5.0 km running, 15.0 km cycling
 *             jane → 7.5 km running
 *
 * NOTE: Data is only seeded if the database is empty. Redeploying
 * will not create duplicates.
 */
@WebListener
public class ApplicationLifecycleListener implements ServletContextListener {

    private static final Logger LOGGER =
            Logger.getLogger(ApplicationLifecycleListener.class.getName());

    @Inject
    private UserService userService;

    @Inject
    private ChallengeService challengeService;

    @Inject
    private ProgressService progressService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("🚀 FitChallenge Application Starting...");

        try {
            initializeSampleData();
            LOGGER.info("✅ FitChallenge Application Started!");
        } catch (Exception e) {
            LOGGER.severe("❌ Error initializing sample data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("👋 FitChallenge Application Shutting Down...");
    }

    private void initializeSampleData() {
        if (userService.countUsers() > 0) {
            LOGGER.info("Sample data already exists — skipping initialization.");
            return;
        }

        // Create users
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin123");
        admin.setEmail("admin@fitchallenge.com");
        admin.setRole(Role.ADMIN);
        admin = userService.createUser(admin);

        User manager = new User();
        manager.setUsername("manager");
        manager.setPassword("manager123");
        manager.setEmail("manager@fitchallenge.com");
        manager.setRole(Role.MANAGER);
        manager = userService.createUser(manager);

        User john = new User();
        john.setUsername("john");
        john.setPassword("john123");
        john.setEmail("john@example.com");
        john.setRole(Role.USER);
        john = userService.createUser(john);

        User jane = new User();
        jane.setUsername("jane");
        jane.setPassword("jane123");
        jane.setEmail("jane@example.com");
        jane.setRole(Role.USER);
        jane = userService.createUser(jane);

        // Create challenges
        Challenge running = new Challenge();
        running.setTitle("30 Day Running Challenge");
        running.setDescription("Run 100 kilometers in 30 days");
        running.setType(ChallengeType.RUNNING);
        running.setGoalValue(100);
        running.setStartDate(LocalDateTime.now());
        running.setEndDate(LocalDateTime.now().plusDays(30));
        running = challengeService.createChallenge(running, admin.getId());

        Challenge cycling = new Challenge();
        cycling.setTitle("Weekly Cycling Goal");
        cycling.setDescription("Cycle 50 kilometers this week");
        cycling.setType(ChallengeType.CYCLING);
        cycling.setGoalValue(50);
        cycling.setStartDate(LocalDateTime.now());
        cycling.setEndDate(LocalDateTime.now().plusDays(7));
        cycling = challengeService.createChallenge(cycling, manager.getId());

        Challenge strength = new Challenge();
        strength.setTitle("Push-up Challenge");
        strength.setDescription("Complete 1000 push-ups in 30 days");
        strength.setType(ChallengeType.STRENGTH);
        strength.setGoalValue(1000);
        strength.setStartDate(LocalDateTime.now());
        strength.setEndDate(LocalDateTime.now().plusDays(30));
        strength = challengeService.createChallenge(strength, admin.getId());

        // Join challenges
        challengeService.joinChallenge(running.getId(), john.getId());
        challengeService.joinChallenge(running.getId(), jane.getId());
        challengeService.joinChallenge(cycling.getId(), john.getId());
        challengeService.joinChallenge(strength.getId(), jane.getId());

        // Add progress logs
        ProgressLog p1 = new ProgressLog();
        p1.setUser(john);
        p1.setChallenge(running);
        p1.setValue(5.0);
        p1.setNotes("Morning run");
        progressService.logProgress(p1);

        ProgressLog p2 = new ProgressLog();
        p2.setUser(jane);
        p2.setChallenge(running);
        p2.setValue(7.5);
        p2.setNotes("Evening run");
        progressService.logProgress(p2);

        ProgressLog p3 = new ProgressLog();
        p3.setUser(john);
        p3.setChallenge(cycling);
        p3.setValue(15.0);
        p3.setNotes("Bike to work");
        progressService.logProgress(p3);

        LOGGER.info("📊 Sample data created: 4 users, 3 challenges, 3 progress logs");
        LOGGER.info("   Credentials: admin/admin123 · manager/manager123 · john/john123 · jane/jane123");
    }
}