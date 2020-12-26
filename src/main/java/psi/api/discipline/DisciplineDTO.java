package psi.api.discipline;

import lombok.AllArgsConstructor;
import lombok.Data;
import psi.domain.discipline.entity.DisciplineType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class DisciplineDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private DisciplineType disciplineType;

}
