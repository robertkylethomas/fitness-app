package com.udacity.fitchallenge.filter;

import jakarta.annotation.Priority;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * JAX-RS filter that handles authentication and authorization for the REST API.
 * Provided — no changes needed.
 *
 * Runs at AUTHORIZATION priority (after resource matching), so ResourceInfo
 * is fully populated and @RolesAllowed can be read from the matched method.
 *
 * Step 1 - Role check: If the matched method has no @RolesAllowed, the
 *           endpoint is public and the request passes through immediately.
 *
 * Step 2 - Authentication: Extracts and validates HTTP Basic credentials
 *           via IdentityStoreHandler → DatabaseIdentityStore.validate().
 *           Aborts with 401 if credentials are missing or invalid.
 *
 * Step 3 - Authorization: Checks whether the authenticated user holds one
 *           of the required roles. Aborts with 403 if not.
 */
@Provider
@Priority(Priorities.AUTHORIZATION)
public class RestAuthFilter implements ContainerRequestFilter {

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Step 1: Check if this endpoint requires any roles
        Set<String> requiredRoles = getRequiredRoles();
        if (requiredRoles.isEmpty()) {
            return; // Public endpoint — no auth needed
        }

        // Step 2: Extract and validate Basic Auth credentials
        String authHeader = requestContext.getHeaderString("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            abortUnauthorized(requestContext);
            return;
        }

        String encoded = authHeader.substring("Basic ".length()).trim();
        String decoded;
        try {
            decoded = new String(Base64.getDecoder().decode(encoded));
        } catch (IllegalArgumentException e) {
            abortUnauthorized(requestContext);
            return;
        }

        int colonIndex = decoded.indexOf(':');
        if (colonIndex < 0) {
            abortUnauthorized(requestContext);
            return;
        }
        String username = decoded.substring(0, colonIndex);
        String password = decoded.substring(colonIndex + 1);

        CredentialValidationResult result = identityStoreHandler.validate(
                new UsernamePasswordCredential(username, password)
        );

        if (result == null || result.getStatus() != CredentialValidationResult.Status.VALID) {
            abortUnauthorized(requestContext);
            return;
        }

        Set<String> userRoles = result.getCallerGroups();
        String callerName = result.getCallerPrincipal().getName();
        boolean secure = requestContext.getSecurityContext().isSecure();

        requestContext.setSecurityContext(new SecurityContext() {
            @Override public Principal getUserPrincipal() { return () -> callerName; }
            @Override public boolean isUserInRole(String role) { return userRoles.contains(role); }
            @Override public boolean isSecure() { return secure; }
            @Override public String getAuthenticationScheme() { return SecurityContext.BASIC_AUTH; }
        });

        // Step 3: Enforce role requirement
        boolean hasRole = requiredRoles.stream().anyMatch(userRoles::contains);
        if (!hasRole) {
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"Access denied: insufficient role\"}")
                    .build());
        }
    }

    private Set<String> getRequiredRoles() {
        if (resourceInfo == null) return Collections.emptySet();

        Method method = resourceInfo.getResourceMethod();
        if (method != null && method.isAnnotationPresent(RolesAllowed.class)) {
            return new HashSet<>(Arrays.asList(method.getAnnotation(RolesAllowed.class).value()));
        }

        Class<?> cls = resourceInfo.getResourceClass();
        if (cls != null && cls.isAnnotationPresent(RolesAllowed.class)) {
            return new HashSet<>(Arrays.asList(cls.getAnnotation(RolesAllowed.class).value()));
        }

        return Collections.emptySet();
    }

    private void abortUnauthorized(ContainerRequestContext ctx) {
        ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .header("WWW-Authenticate", "Basic realm=\"FitChallenge\"")
                .type(MediaType.APPLICATION_JSON)
                .entity("{\"error\":\"Authentication required\"}")
                .build());
    }
}