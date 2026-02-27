package com.udacity.fitchallenge.rest;

import com.udacity.fitchallenge.entity.ProgressLog;
import com.udacity.fitchallenge.exception.ResourceNotFoundException;
import com.udacity.fitchallenge.service.ProgressService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * REST resource for progress logging.
 * Provided — no changes needed.
 *
 * Read this class after completing ChallengeResource.
 * Notice how the same patterns repeat:
 *   - @Valid triggers Bean Validation on the request body
 *   - 201 Created for successful POST with a Location header
 *   - 204 No Content for successful DELETE
 *   - catch ResourceNotFoundException → 404
 *   - catch IllegalArgumentException  → 400
 */
@Path("/progress")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProgressResource {

    @Inject
    private ProgressService progressService;

    /**
     * POST /api/progress — logs a progress entry for a user in a challenge.
     * Returns 201 Created with the saved log in the response body.
     * Returns 400 Bad Request if user or challenge IDs are missing.
     * Returns 404 Not Found if the user or challenge does not exist.
     */
    @POST
    @RolesAllowed({"USER", "MANAGER", "ADMIN"})
    public Response logProgress(@Valid ProgressLog progressLog) {
        try {
            ProgressLog created = progressService.logProgress(progressLog);
            URI location = URI.create("/api/progress/" + created.getId());
            return Response.created(location).entity(created).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    /**
     * GET /api/progress/challenge/{challengeId} — returns all progress logs for a challenge.
     */
    @GET
    @Path("/challenge/{challengeId}")
    public Response getProgressByChallenge(@PathParam("challengeId") Long challengeId) {
        List<ProgressLog> logs = progressService.getProgressByChallenge(challengeId);
        return Response.ok(logs).build();
    }

    /**
     * GET /api/progress/challenge/{challengeId}/user/{userId} — returns progress for a
     * specific user within a specific challenge.
     */
    @GET
    @Path("/challenge/{challengeId}/user/{userId}")
    public Response getProgressByUserAndChallenge(@PathParam("challengeId") Long challengeId,
                                                  @PathParam("userId") Long userId) {
        List<ProgressLog> logs = progressService.getProgressByUserAndChallenge(userId, challengeId);
        return Response.ok(logs).build();
    }

    /**
     * GET /api/progress/user/{userId} — returns all progress logs across all challenges for a user.
     */
    @GET
    @Path("/user/{userId}")
    public Response getProgressByUser(@PathParam("userId") Long userId) {
        List<ProgressLog> logs = progressService.getProgressByUser(userId);
        return Response.ok(logs).build();
    }

    /**
     * DELETE /api/progress/{id} — deletes a progress log entry by ID.
     * Returns 204 No Content on success.
     */
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"MANAGER", "ADMIN"})
    public Response deleteProgressLog(@PathParam("id") Long id) {
        progressService.deleteProgressLog(id);
        return Response.noContent().build();
    }
}