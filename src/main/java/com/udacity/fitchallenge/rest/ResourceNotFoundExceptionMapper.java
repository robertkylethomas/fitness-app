package com.udacity.fitchallenge.rest;

import com.udacity.fitchallenge.exception.ResourceNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

/**
 * Global exception mapper for ResourceNotFoundException.
 * Provided — no changes needed.
 *
 * WHY THIS EXISTS:
 * Without this class, any unhandled ResourceNotFoundException anywhere in
 * the application would produce a generic 500 Internal Server Error.
 * That's misleading — the server didn't fail, the resource simply doesn't exist.
 * This mapper intercepts the exception globally and converts it to a proper
 * 404 Not Found with a structured JSON error body.
 *
 * HOW IT WORKS:
 * @Provider tells JAX-RS to register this class as a framework extension.
 * Whenever a ResourceNotFoundException is thrown — in any resource class,
 * with or without a try/catch — JAX-RS automatically routes it here.
 * No per-endpoint boilerplate needed.
 *
 * WHAT TO OBSERVE:
 * Notice that ChallengeResource still has explicit try/catch blocks for
 * ResourceNotFoundException in some methods. That's intentional — those
 * catch blocks existed before this mapper was added and are kept as
 * reference patterns. In a production codebase you would rely on the
 * mapper and remove the per-method catch blocks.
 */
@Provider
public class ResourceNotFoundExceptionMapper
        implements ExceptionMapper<ResourceNotFoundException> {

    /**
     * Converts a ResourceNotFoundException into an HTTP 404 response.
     *
     * Example response body:
     * {
     *   "error":   "Resource Not Found",
     *   "message": "Challenge not found with id: 5"
     * }
     */
    @Override
    public Response toResponse(ResourceNotFoundException exception) {
        Map<String, String> errorResponse = Map.of(
                "error",   "Resource Not Found",
                "message", exception.getMessage()
        );

        return Response.status(Response.Status.NOT_FOUND)
                .entity(errorResponse)
                .build();
    }
}