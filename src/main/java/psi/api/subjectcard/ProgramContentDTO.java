package psi.api.subjectcard;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
public class ProgramContentDTO {

    @NotBlank
    private String description;

    @NotNull
    @Positive
    private Double numberOfHours;

}
