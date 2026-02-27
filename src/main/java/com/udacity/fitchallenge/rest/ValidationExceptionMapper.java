package com.udacity.fitchallenge.rest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception mapper for ConstraintViolationException.
 * Provided — no changes needed.
 *
 * WHY THIS EXISTS:
 * When @Valid triggers Bean Validation and a constraint is violated,
 * JAX-RS throws a ConstraintViolationException. Without this mapper,
 * the client receives a generic 500 Internal Server Error.
 * This mapper converts it into a structured 400 Bad Request that lists
 * every violated field and the exact message you defined in Phase 1.
 *
 * THIS IS THE BRIDGE BETWEEN PHASE 1 AND PHASE 4:
 * The @NotBlank(message = "Title is required") annotation you wrote in
 * Challenge.java is what produces the value in "validationErrors" below.
 * The annotation declares the rule — this mapper formats the violation
 * into a response the API client can actually use.
 *
 * HOW IT WORKS:
 * @Provider registers this mapper globally. JAX-RS calls toResponse()
 * automatically whenever Bean Validation fails on any @Valid parameter
 * in any resource class — no per-endpoint handling needed.
 *
 * WHAT TO OBSERVE:
 * Each ConstraintViolation has two useful pieces of information:
 *   - getPropertyPath() — the field path (e.g. "createChallenge.challenge.title")
 *   - getMessage()      — the message= value from your Phase 1 annotation
 * Both are extracted and returned together so the client knows exactly
 * which fields failed and why.
 */
@Provider
public class ValidationExceptionMapper
        implements ExceptionMapper<ConstraintViolationException> {

    /**
     * Converts a ConstraintViolationException into an HTTP 400 response.
     *
     * Example response body:
     * {
     *   "error": "Validation Failed",
     *   "validationErrors": {
     *     "createChallenge.challenge.title":     "Title is required",
     *     "createChallenge.challenge.goalValue": "Goal value must be at least 1"
     *   }
     * }
     */
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Map<String, String> errors = new HashMap<>();

        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            String fieldName    = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }

        Map<String, Object> response = Map.of(
                "error",            "Validation Failed",
                "validationErrors", errors
        );

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .build();
    }
}