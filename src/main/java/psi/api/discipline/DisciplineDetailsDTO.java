package psi.api.discipline;

import lombok.AllArgsConstructor;
import lombok.Data;
import psi.domain.disciplinetype.entity.DisciplineType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class DisciplineDetailsDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private DisciplineType disciplineType;

}
