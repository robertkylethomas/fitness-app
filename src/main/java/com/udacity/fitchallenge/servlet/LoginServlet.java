package com.udacity.fitchallenge.servlet;

import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Login servlet — handles form-based authentication for the web interface.
 * Provided — no changes needed.
 *
 * Key concepts to observe in this class:
 *
 * 1. @WebServlet("/login") maps this servlet to the /login URL path —
 *    matching the form action="login" in login.html.
 *
 * 2. doPost() processes the submitted username and password from the login form.
 *    The form fields name="username" and name="password" map directly to
 *    request.getParameter("username") and request.getParameter("password").
 *
 * 3. IdentityStoreHandler.validate() delegates to DatabaseIdentityStore.validate()
 *    — the same identity store used by RestAuthFilter for the REST API.
 *    One identity store, two consumers: servlet login and REST Basic Auth.
 *
 * 4. On success: a session is created and the username is stored as an attribute.
 *    AuthenticationFilter checks for this attribute on every request to /dashboard.
 *    session.invalidate() on logout removes it — no manual cookie management needed.
 *
 * 5. On failure: redirect back to login.html with ?error=true so the page can
 *    display a message. Never reveal whether username or password was wrong.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validate credentials against the database via DatabaseIdentityStore
        CredentialValidationResult result = identityStoreHandler.validate(
                new UsernamePasswordCredential(username, password)
        );

        if (result.getStatus() == CredentialValidationResult.Status.VALID) {
            // Credentials valid — create a session and store the username
            // AuthenticationFilter checks for this attribute on every request to /dashboard
            HttpSession session = request.getSession(true);
            session.setAttribute("username", result.getCallerPrincipal().getName());
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            // Credentials invalid — redirect back to login with error flag
            // Never reveal whether username or password was the problem
            response.sendRedirect(request.getContextPath() + "/login.html?error=true");
        }
    }
}