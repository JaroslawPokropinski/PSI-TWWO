package psi.infrastructure.security;

import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import psi.infrastructure.exception.IllegalArgumentAppException;

import java.util.Optional;

@UtilityClass
public class UserInfoProvider {

    public static UserInfo requireAuthenticatedUser() {
        return getAuthenticatedUser()
                .orElseThrow(() -> new IllegalArgumentAppException("Cannot retrieve authenticated user from security context."));
    }

    public static Optional<UserInfo> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        return  Optional.ofNullable((UserInfo) authentication.getPrincipal());
    }

}