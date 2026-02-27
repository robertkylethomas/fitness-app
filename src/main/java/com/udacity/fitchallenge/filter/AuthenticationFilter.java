package com.udacity.fitchallenge.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Authentication filter — protects servlet pages from unauthenticated access.
 * Provided — no changes needed.
 *
 * HOW IT WORKS:
 * @WebFilter(urlPatterns = {"/dashboard", "/progress"}) tells the container
 * to intercept every request to those paths and run doFilter() first.
 * If the user has an active session with a "username" attribute, the request
 * proceeds. If not, they are redirected to login.html.
 *
 * This filter protects the SERVLET layer (/dashboard).
 * The @RolesAllowed annotations in Phase 4 protect the REST API layer (/api/*).
 * Both mechanisms work independently — servlet pages need this filter,
 * JAX-RS endpoints use the Jakarta Security role-based access control.
 *
 * WHAT TO OBSERVE:
 * - implements Filter — the standard Jakarta Servlet filter interface
 * - doFilter() is the interception point — call chain.doFilter() to continue,
 *   or send a redirect to abort the request
 * - getSession(false) — the false argument means "don't create a new session
 *   if one doesn't exist". Always use false for authentication checks.
 */
@WebFilter(urlPatterns = {"/dashboard", "/progress"})
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest   = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // getSession(false) — never create a session just to check authentication
        HttpSession session = httpRequest.getSession(false);
        boolean isAuthenticated = (session != null && session.getAttribute("username") != null);

        if (isAuthenticated) {
            // User has an active session — continue to the requested resource
            chain.doFilter(request, response);
        } else {
            // No active session — redirect to login page
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.html");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialisation needed
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}