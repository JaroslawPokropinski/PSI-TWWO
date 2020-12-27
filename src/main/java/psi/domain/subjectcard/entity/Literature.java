package psi.domain.subjectcard.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Literature {

    @NotNull
    @Positive
    private Integer number;

    @NotBlank
    private String name;

    @NotNull
    private Boolean isPrimary;

}
