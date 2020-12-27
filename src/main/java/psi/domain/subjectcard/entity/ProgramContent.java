package psi.domain.subjectcard.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProgramContent {

    @NotNull
    @Positive
    private Integer number;

    @NotBlank
    private String description;

    @NotNull
    @Positive
    private Double numberOfHours;

}
