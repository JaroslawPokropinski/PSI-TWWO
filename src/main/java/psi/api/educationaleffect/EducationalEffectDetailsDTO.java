package psi.api.educationaleffect;

import lombok.AllArgsConstructor;
import lombok.Data;
import psi.domain.educationaleffect.EducationalEffectCategory;
import psi.domain.educationaleffect.EducationalEffectType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class EducationalEffectDetailsDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String code;

    @NotNull
    private EducationalEffectType type;

    @NotNull
    private Integer prkLevel;

    @NotNull
    private Boolean isEngineerEffect;

    @NotNull
    private Boolean isLingualEffect;

    @NotNull
    private EducationalEffectCategory category;

    @NotBlank
    private String description;

}
