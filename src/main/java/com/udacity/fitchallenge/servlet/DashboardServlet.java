package com.udacity.fitchallenge.servlet;

import com.udacity.fitchallenge.entity.Challenge;
import com.udacity.fitchallenge.service.ChallengeService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Dashboard servlet — displays all active challenges in a simple HTML page.
 * Provided — no changes needed.
 *
 * Key concepts to observe in this class:
 *
 * 1. @WebServlet("/dashboard") maps this servlet to the /dashboard URL path —
 *    the Jakarta Servlet equivalent of @Path in JAX-RS.
 *
 * 2. @Inject works here exactly as it does in services and REST resources —
 *    CDI manages the lifecycle of all Jakarta EE components uniformly.
 *
 * 3. doGet() generates an HTML response directly using PrintWriter —
 *    this is the traditional Servlet approach before JSP/JSF/templating engines.
 *
 * 4. escapeHtml() prevents XSS attacks by sanitizing user-supplied content
 *    before rendering it in HTML. Never render raw user data in a web page.
 *
 * Once deployed, visit: http://localhost:8080/fitchallenge/dashboard
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Inject
    private ChallengeService challengeService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Fetch active challenges from the service layer
        List<Challenge> challenges = challengeService.getActiveChallenges();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>FitChallenge Dashboard</title>");
        out.println("<link rel='stylesheet' href='css/style.css'>");
        out.println("</head>");
        out.println("<body>");

        out.println("<div class='container'>");
        out.println("<header>");
        out.println("<h1>🏋️ FitChallenge Dashboard</h1>");
        out.println("<nav>");
        out.println("<a href='dashboard'>Dashboard</a>");
        out.println("<a href='api/challenges'>API</a>");
        out.println("<a href='logout'>Logout</a>");
        out.println("</nav>");
        out.println("</header>");

        out.println("<main>");
        out.println("<h2>Active Challenges</h2>");

        if (challenges.isEmpty()) {
            out.println("<p class='no-challenges'>No active challenges at the moment.</p>");
            out.println("<p>Create a new challenge using the REST API!</p>");
        } else {
            out.println("<div class='challenges-grid'>");

            for (Challenge challenge : challenges) {
                out.println("<div class='challenge-card'>");
                out.println("<div class='challenge-header'>");
                out.println("<h3>" + escapeHtml(challenge.getTitle()) + "</h3>");
                out.println("<span class='challenge-type'>" + challenge.getType() + "</span>");
                out.println("</div>");

                if (challenge.getDescription() != null) {
                    out.println("<p class='challenge-description'>" +
                            escapeHtml(challenge.getDescription()) + "</p>");
                }

                out.println("<div class='challenge-details'>");
                out.println("<p><strong>Goal:</strong> " + challenge.getGoalValue() + "</p>");
                out.println("<p><strong>Start:</strong> " + challenge.getStartDate() + "</p>");
                out.println("<p><strong>End:</strong> " + challenge.getEndDate() + "</p>");
                out.println("<p><strong>Participants:</strong> " +
                        challenge.getParticipants().size() + "</p>");
                out.println("</div>");

                out.println("<div class='challenge-actions'>");
                out.println("<a href='api/challenges/" + challenge.getId() +
                        "/leaderboard' class='btn'>View Leaderboard</a>");
                out.println("</div>");

                out.println("</div>");
            }

            out.println("</div>");
        }

        out.println("</main>");

        out.println("<footer>");
        out.println("<p>&copy; 2026 FitChallenge - Jakarta EE 11 Capstone Project</p>");
        out.println("</footer>");

        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * Escapes special HTML characters to prevent XSS attacks.
     * Always sanitize user-supplied content before rendering it in HTML.
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}