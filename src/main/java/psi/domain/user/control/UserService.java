package psi.domain.user.control;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psi.domain.auditedobject.ObjectState;
import psi.domain.user.entity.User;
import psi.domain.user.entity.UserRole;
import psi.infrastructure.exception.IllegalArgumentAppException;
import psi.infrastructure.security.TokenHandler;

import java.text.MessageFormat;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private static final String USERNAME_ALREADY_TAKEN_MSG = "User with username {0} already exists!";
    private static final String EMAIL_ALREADY_TAKEN_MSG = "User with email {0} already exists!";
    private static final String NO_SUCH_USER = "There is no user with id {0}";
    private static final String PASSWORD_NOT_PASSED_VALIDATION_RULES = "Password not passed validation rules";
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final Pattern EMAIL_REGEX = Pattern.compile("[^@ ]+@[^@ ]+\\.[^@ ]+");
    private static final String EMAIL_INCORRECT = "Supplied email is incorrect";

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenHandler tokenHandler;

    public User createUser(User userToCreate) {
        validateUserOnCreate(userToCreate);
        prepareUserBeforeCreate(userToCreate);
        return userRepository.save(userToCreate);
    }

    private void validateUserOnCreate(User user) {
        validateUsernameUniqueness(user);
        validateEmail(user.getEmail());
        validateEmailUniqueness(user.getEmail());
        validatePasswordRules(user.getPassword());
    }

    private void validateUsernameUniqueness(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentAppException(MessageFormat.format(USERNAME_ALREADY_TAKEN_MSG, user.getUsername()));
        }
    }

    private void validateEmailUniqueness(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentAppException(MessageFormat.format(EMAIL_ALREADY_TAKEN_MSG, email));
        }
    }

    private void validateEmail(String email) {
        if (!EMAIL_REGEX.matcher(email).matches()) {
            throw new IllegalArgumentAppException(EMAIL_INCORRECT);
        }
    }

    private void validatePasswordRules(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentAppException(PASSWORD_NOT_PASSED_VALIDATION_RULES);
        }
    }

    private void prepareUserBeforeCreate(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.ROLE_USER);
        user.setObjectState(ObjectState.ACTIVE);
    }

    public String signInUser(String usernameOrEmail, String password) {
        Authentication authentication = authenticateUser(usernameOrEmail, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenHandler.generateToken(authentication);
    }

    public Authentication authenticateUser(String usernameOrEmail, String password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(usernameOrEmail, password);
        return authenticationManager.authenticate(token);
    }

    public User getExistingUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentAppException(MessageFormat.format(NO_SUCH_USER, userId)));
    }

    public boolean userExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
