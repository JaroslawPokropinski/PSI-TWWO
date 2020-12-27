package psi.domain.subjectcard.entity;

import lombok.Getter;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Embeddable
@Getter
public class Item {

    @NotNull
    @Positive
    private Integer number;

    @NotBlank
    private String content;

}
