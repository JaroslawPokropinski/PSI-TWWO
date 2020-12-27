package psi.domain.subjectcard.entity;

import lombok.Getter;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Embeddable
public class Literature {

    @NotNull
    @Positive
    private Integer number;

    @NotBlank
    private String name;

    @NotNull
    private Boolean isPrimary;

}
