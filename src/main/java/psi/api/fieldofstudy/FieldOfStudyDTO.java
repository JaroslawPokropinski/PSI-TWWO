package psi.api.fieldofstudy;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class FieldOfStudyDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private Long faculty;

}
