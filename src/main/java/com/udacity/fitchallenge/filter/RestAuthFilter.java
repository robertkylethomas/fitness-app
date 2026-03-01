package com.udacity.fitchallenge.filter;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.Set;

/**
 * JAX-RS authentication filter for the REST API.
 *
 * Intercepts every request to /api/* before it reaches a resource method.
 * Extracts the HTTP Basic Auth header, decodes the credentials, and delegates
 * validation to IdentityStoreHandler — which in turn calls
 * DatabaseIdentityStore.validate(). If validation succeeds, a SecurityContext
 * is set on the request so that role-based access control can be enforced.
 * If validation fails, the request is aborted with 401 Unauthorized.
 *
 * TODO (Phase 5 - Security):
 * Complete the filter() method:
 * 1. Extract the Authorization header and check it starts with "Basic "
 * 2. Base64-decode the credentials and split on ':'
 * 3. Call identityStoreHandler.validate() with a UsernamePasswordCredential
 * 4. If INVALID → abort with 401 using abortUnauthorized()
 * 5. If VALID → build and set a SecurityContext on requestContext
 *    using result.getCallerPrincipal() and result.getCallerGroups()
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class RestAuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // TODO: Implement Basic Auth validation
        // Hint: retrieve IdentityStoreHandler via CDI.current().select(IdentityStoreHandler.class).get()

        // Determine required roles for this endpoint
        Set<String> requiredRoles = requiredRoles(
                requestContext.getMethod(),
                requestContext.getUriInfo().getPath().replaceFirst("^/", "")
        );

        // Public endpoint — no authentication needed
        if (requiredRoles == null) {
            return;
        }

        // TODO: Extract Basic Auth header
        // TODO: Decode Base64 credentials
        // TODO: Validate via IdentityStoreHandler
        // TODO: Set SecurityContext on requestContext
        // TODO: Check role and abort with 403 if insufficient
    }

    /**
     * Returns the set of roles required for the given HTTP method + path.
     * Returns null if the endpoint is public (no auth required).
     *
     * This mirrors the @RolesAllowed annotations on each resource method.
     */
    private Set<String> requiredRoles(String method, String path) {
        Set<String> ALL_ROLES  = Set.of("USER", "MANAGER", "ADMIN");
        Set<String> MGR_ADMIN  = Set.of("MANAGER", "ADMIN");
        Set<String> ADMIN_ONLY = Set.of("ADMIN");

        if (path.matches("challenges/?")) {
            if ("GET".equals(method))  return null;
            if ("POST".equals(method)) return ALL_ROLES;
        }
        if (path.matches("challenges/\\d+")) {
            if ("GET".equals(method))    return null;
            if ("PUT".equals(method))    return MGR_ADMIN;
            if ("DELETE".equals(method)) return ADMIN_ONLY;
        }
        if (path.matches("challenges/\\d+/(join|leave)")) return ALL_ROLES;
        if (path.matches("challenges/\\d+/complete"))     return MGR_ADMIN;
        if (path.matches("challenges/(active|stats)") ||
                path.matches("challenges/\\d+/leaderboard"))  return null;
        if (path.matches("progress/?")) {
            if ("POST".equals(method)) return ALL_ROLES;
        }
        if (path.matches("progress/\\d+")) {
            if ("DELETE".equals(method)) return MGR_ADMIN;
        }
        if (path.startsWith("progress/")) return null;

        return ALL_ROLES;
    }

    private void abortUnauthorized(ContainerRequestContext ctx) {
        ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .header("WWW-Authenticate", "Basic realm=\"FitChallenge\"")
                .type(MediaType.APPLICATION_JSON)
                .entity("{\"error\":\"Authentication required\"}")
                .build());
    }
}