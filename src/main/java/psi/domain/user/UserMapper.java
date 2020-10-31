package psi.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import psi.api.common.ResourceDTO;
import psi.api.user.RegistrationDTO;
import psi.api.user.UserDTO;

import java.net.URI;

import static psi.infrastructure.utils.ResourcePaths.ID_PATH;

@Service
@RequiredArgsConstructor
public class UserMapper {

    public UserDTO mapToUserDTO(User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public User mapToUser(RegistrationDTO registrationFormDTO) {
        if (registrationFormDTO == null) {
            return null;
        }
        return User.builder()
                .name(registrationFormDTO.getName())
                .surname(registrationFormDTO.getSurname())
                .username(registrationFormDTO.getUsername())
                .password(registrationFormDTO.getPassword())
                .email(registrationFormDTO.getEmail())
                .phoneNumber(registrationFormDTO.getPhoneNumber())
                .build();
    }

    public ResourceDTO mapToResourceDTO(User user) {
        if (user == null) {
            return null;
        }
        return ResourceDTO.builder()
                .id(user.getId())
                .identifier(user.getUsername())
                .uri(getUserUri(user))
                .build();
    }

    private URI getUserUri(User user) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(UserController.MAIN_RESOURCE)
                .path(ID_PATH)
                .buildAndExpand(user.getId())
                .toUri();
    }

}
