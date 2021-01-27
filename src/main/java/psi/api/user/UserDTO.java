package psi.api.user;

import lombok.Builder;
import lombok.Data;
import psi.domain.user.entity.UserRole;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserDTO {

    @NotNull
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 40)
    private String surname;

    @NotBlank
    private String phoneNumber;

    @NotNull
    private UserRole role;

}
