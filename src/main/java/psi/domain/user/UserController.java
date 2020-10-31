package psi.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psi.api.common.ResourceDTO;
import psi.api.common.ResponseDTO;
import psi.api.user.CredentialsDTO;
import psi.api.user.RegistrationDTO;
import psi.api.user.TokenDTO;
import psi.api.user.UserDTO;
import psi.infrastructure.security.UserInfo;
import psi.infrastructure.security.annotation.HasAnyRole;
import psi.infrastructure.security.annotation.LoggedUser;
import javax.validation.Valid;
import static psi.infrastructure.utils.ResourcePaths.ID;
import static psi.infrastructure.utils.ResourcePaths.ID_PATH;

@RestController
@RequestMapping(UserController.MAIN_RESOURCE)
@RequiredArgsConstructor
public class UserController {

    public static final String MAIN_RESOURCE = "/api/user";
    private static final String USERNAME_PATH_PARAM = "username";
    private static final String EMAIL_PATH_PARAM = "email";
    private static final String CHECK_USERNAME_RESOURCE = "/check/username/{" + USERNAME_PATH_PARAM + "}";
    private static final String CHECK_EMAIL_RESOURCE = "/check/email/{" + EMAIL_PATH_PARAM + "}";

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/signin")
    public TokenDTO authenticateUser(@Valid @RequestBody CredentialsDTO credentials) {
        String token = userService.signInUser(credentials.getUsername(), credentials.getPassword());
        return new TokenDTO(token);
    }

    @PostMapping("/signup")
    public ResourceDTO registerUser(@Valid @RequestBody RegistrationDTO registrationFormDTO) {
        User userToCreate = userMapper.mapToUser(registrationFormDTO);
        User createdUser = userService.createUser(userToCreate);
        return userMapper.mapToResourceDTO(createdUser);
    }

    @GetMapping(CHECK_USERNAME_RESOURCE)
    public ResponseDTO<Boolean> checkUsernameAvailable(@PathVariable(USERNAME_PATH_PARAM) String username) {
        boolean isAvailable = !userService.userExistsByUsername(username);
        return new ResponseDTO<>(isAvailable, isAvailable ? "Username is available" : "Username is already taken");
    }

    @GetMapping(CHECK_EMAIL_RESOURCE)
    public ResponseDTO<Boolean> checkIfEmailAvailable(@PathVariable(EMAIL_PATH_PARAM) String email) {
        boolean isAvailable = !userService.userExistsByEmail(email);
        return new ResponseDTO<>(isAvailable, isAvailable ? "Email is available" : "Email is already taken");
    }

    @GetMapping("/current")
    @HasAnyRole
    public UserDTO getCurrentUser(@LoggedUser UserInfo currentUserInfo) {
        User user = userService.getExistingUser(currentUserInfo.getId());
        return userMapper.mapToUserDTO(user);
    }

    @GetMapping(ID_PATH)
    @HasAnyRole
    public UserDTO getUser(@PathVariable(ID) Long id) {
        User user = userService.getExistingUser(id);
        return userMapper.mapToUserDTO(user);
    }

}
