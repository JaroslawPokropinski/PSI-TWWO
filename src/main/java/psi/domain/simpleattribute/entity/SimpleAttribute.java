package psi.domain.simpleattribute.entity;

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
public class SimpleAttribute {

    @NotNull
    @Positive
    private Integer number;

    @NotBlank
    private String content;

}
