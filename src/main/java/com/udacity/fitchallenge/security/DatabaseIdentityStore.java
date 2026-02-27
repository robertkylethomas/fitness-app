package com.udacity.fitchallenge.security;

import com.udacity.fitchallenge.entity.User;
import com.udacity.fitchallenge.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Database-backed identity store for user authentication.
 *
 * This class implements Jakarta Security's IdentityStore interface.
 * The container calls validate() automatically whenever authentication
 * is triggered — you never call it directly.
 *
 * HOW IT CONNECTS TO THE REST OF THE APPLICATION:
 * - UserRepository (Phase 2) provides findByUsername() to look up the user
 * - Pbkdf2PasswordHash verifies the incoming password against the hash
 *   stored by UserService.createUser() in Phase 3
 * - The role returned here is what @RolesAllowed checks in Phase 4
 *
 * THREE POSSIBLE OUTCOMES from validate():
 * - NOT_VALIDATED_RESULT — wrong credential type, skip this store
 * - INVALID_RESULT       — user not found or wrong password
 * - CredentialValidationResult(username, roles) — success
 */
@ApplicationScoped
public class DatabaseIdentityStore implements IdentityStore {

    @Inject
    private UserRepository userRepository;

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    /**
     * Configures the password hasher with the same parameters used in UserService.
     * Must match exactly — different parameters produce different hashes.
     * Provided — no changes needed.
     */
    @PostConstruct
    public void init() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
        passwordHash.initialize(parameters);
    }

    /**
     * Validates a user's credentials against the database.
     * Called automatically by the container during authentication.
     *
     * Steps 1 and the credential extraction are provided.
     * Implement Steps 2, 3, and 4.
     *
     * HINT Step 2: userRepository.findByUsername(username) returns Optional<User>
     *              Return CredentialValidationResult.INVALID_RESULT if empty
     * HINT Step 3: passwordHash.verify(password.toCharArray(), user.getPassword())
     *              Returns true if the plaintext password matches the stored hash
     * HINT Step 4: Return new CredentialValidationResult(username, Set.of(user.getRole().name()))
     *              Note: .name() converts Role.ADMIN → "ADMIN" to match @RolesAllowed strings
     */
    @Override
    public CredentialValidationResult validate(Credential credential) {

        // Step 1 — verify credential type and extract username/password
        // Provided — no changes needed
        if (!(credential instanceof UsernamePasswordCredential)) {
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }
        UsernamePasswordCredential upc = (UsernamePasswordCredential) credential;
        String username = upc.getCaller();
        String password = upc.getPasswordAsString();

        // TODO Step 2 — find the user by username using userRepository
        // Return CredentialValidationResult.INVALID_RESULT if not found

        // TODO Step 3 — verify the password using passwordHash.verify()
        // Return CredentialValidationResult.INVALID_RESULT if password does not match

        // TODO Step 4 — return a successful CredentialValidationResult
        // Pass username and Set.of(user.getRole().name()) as the roles
        return null;
    }
}