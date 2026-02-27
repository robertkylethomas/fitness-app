package com.udacity.fitchallenge.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * JAX-RS Application Configuration.
 * Provided — no changes needed.
 *
 * WHY THIS EXISTS:
 * This class activates the JAX-RS framework and sets the base path for
 * all REST endpoints. Without it, WildFly will not route any requests
 * to your resource classes and every /api/* URL will return 404.
 *
 * @ApplicationPath("/api") means all endpoints are prefixed with /api:
 *   GET  /api/challenges
 *   POST /api/challenges
 *   GET  /api/progress/challenge/{id}
 *   etc.
 *
 * No additional configuration is needed — WildFly automatically
 * discovers all classes annotated with @Path in the application.
 */
@ApplicationPath("/api")
public class ApplicationConfig extends Application {
}