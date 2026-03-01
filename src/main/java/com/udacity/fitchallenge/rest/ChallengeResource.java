package com.udacity.fitchallenge.rest;

import com.udacity.fitchallenge.entity.Challenge;
import com.udacity.fitchallenge.entity.ChallengeStatus;
import com.udacity.fitchallenge.entity.ChallengeType;
import com.udacity.fitchallenge.exception.ResourceNotFoundException;
import com.udacity.fitchallenge.service.ChallengeService;
import com.udacity.fitchallenge.service.ProgressService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;


import java.net.URI;
import java.util.List;
import java.util.Map;

@Path("/challenges")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChallengeResource {

    @Inject
    private ChallengeService challengeService;

    @Inject
    private ProgressService progressService;

    @Context
    private UriInfo uriInfo;

    /**
     * GET /api/challenges — returns all challenges.
     * Supports optional filtering by ?status= or ?type= query parameters.
     * Provided — no changes needed.
     */
    @GET
    public Response getAllChallenges(@QueryParam("status") ChallengeStatus status,
                                     @QueryParam("type") ChallengeType type) {
        List<Challenge> challenges;

        if (status != null) {
            challenges = challengeService.getChallengesByStatus(status);
        } else if (type != null) {
            challenges = challengeService.getChallengesByType(type);
        } else {
            challenges = challengeService.getAllChallenges();
        }

        return Response.ok(challenges).build();
    }

    /**
     * GET /api/challenges/active — returns only active challenges.
     * Provided — no changes needed.
     */
    @GET
    @Path("/active")
    public Response getActiveChallenges() {
        List<Challenge> challenges = challengeService.getActiveChallenges();
        return Response.ok(challenges).build();
    }

    /**
     * GET /api/challenges/{id} — returns a single challenge by ID.
     * Returns 404 Not Found if the challenge does not exist.
     * Provided — no changes needed.
     *
     * Study this method before implementing createChallenge() and joinChallenge().
     * It shows the standard try/catch pattern used throughout this class:
     *   - catch ResourceNotFoundException → return 404 with a JSON error body
     *   - wrap error messages in Map.of("error", e.getMessage())
     */
    @GET
    @Path("/{id}")
    public Response getChallengeById(@PathParam("id") Long id) {
        try {
            Challenge challenge = challengeService.getChallengeById(id);
            return Response.ok(challenge).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    /**
     * POST /api/challenges — creates a new challenge.
     * Requires a creatorId query parameter identifying the challenge creator.
     * Returns 201 Created with the new challenge in the response body.
     * Returns 400 Bad Request if creatorId is missing.
     * Returns 404 Not Found if the creator user does not exist.
     *
     * HINT: @Valid on the Challenge parameter triggers Bean Validation automatically —
     *       invalid input is rejected before your method body runs
     * HINT: Check if creatorId == null first and return 400 if missing:
     *       Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "...")).build()
     // HINT: Use Response.created(uriInfo.getBaseUriBuilder()
     //           .path("challenges").path(String.valueOf(created.getId())).build())
     //       .entity(created).build() for the 201 Created response
     * HINT: Follow the same try/catch pattern as getChallengeById() above
     */
    @POST
    @RolesAllowed({"USER", "MANAGER", "ADMIN"})
    public Response createChallenge(@Valid Challenge challenge,
                                    @QueryParam("creatorId") Long creatorId) {
        // TODO: Return 400 if creatorId is null
        // Call challengeService.createChallenge(challenge, creatorId)
        // Return 201 Created with a Location header and the created challenge body
        // Catch ResourceNotFoundException → 404
        return null;
    }

    /**
     * POST /api/challenges/{id}/join — adds a user to a challenge's participant list.
     * Requires a userId query parameter.
     * Returns 200 OK with the updated challenge on success.
     * Returns 400 Bad Request if userId is missing.
     * Returns 404 Not Found if the challenge or user does not exist.
     * Returns 409 Conflict if the user is already a participant.
     *
     * HINT: Check if userId == null first — return 400 if missing
     * HINT: Call challengeService.joinChallenge(challengeId, userId)
     * HINT: You need TWO catch blocks this time:
     *       catch ResourceNotFoundException → 404 (resource missing)
     *       catch IllegalStateException    → 409 Conflict (already a participant)
     *       Response.status(Response.Status.CONFLICT).entity(Map.of("error", e.getMessage())).build()
     */
    @POST
    @Path("/{id}/join")
    @RolesAllowed({"USER", "MANAGER", "ADMIN"})
    public Response joinChallenge(@PathParam("id") Long challengeId,
                                  @QueryParam("userId") Long userId) {
        // TODO: Return 400 if userId is null
        // Call challengeService.joinChallenge(challengeId, userId)
        // Return 200 OK with updated challenge on success
        // Catch ResourceNotFoundException → 404
        // Catch IllegalStateException → 409 Conflict
        return null;
    }

    /**
     * GET /api/challenges/{id}/leaderboard — returns ranked progress for a challenge.
     * Returns 404 if the challenge does not exist.
     * Provided — no changes needed.
     */
    @GET
    @Path("/{id}/leaderboard")
    public Response getLeaderboard(@PathParam("id") Long challengeId) {
        try {
            challengeService.getChallengeById(challengeId);
            List<Map<String, Object>> leaderboard =
                    progressService.getLeaderboardWithUserDetails(challengeId);
            return Response.ok(leaderboard).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    /**
     * PUT /api/challenges/{id}/complete — marks a challenge as COMPLETED and fires a CDI event.
     * Provided — no changes needed.
     */
    @PUT
    @Path("/{id}/complete")
    @RolesAllowed({"MANAGER", "ADMIN"})
    public Response completeChallenge(@PathParam("id") Long id) {
        try {
            Challenge completed = challengeService.completeChallenge(id);
            return Response.ok(completed).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    /**
     * DELETE /api/challenges/{id} — deletes a challenge by ID.
     * Returns 204 No Content on success — no response body since the resource is gone.
     * Returns 404 Not Found if the challenge does not exist.
     *
     * HINT: Call challengeService.deleteChallenge(id)
     * HINT: Use Response.noContent().build() for 204 — not Response.ok()
     * HINT: Follow the same try/catch pattern as getChallengeById()
     */
    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response deleteChallenge(@PathParam("id") Long id) {
        // TODO: Call challengeService.deleteChallenge(id)
        // Return 204 No Content on success
        // Catch ResourceNotFoundException → 404
        return null;
    }
}