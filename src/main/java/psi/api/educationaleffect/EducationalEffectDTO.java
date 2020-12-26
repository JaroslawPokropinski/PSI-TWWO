package psi.api.educationaleffect;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import psi.domain.educationaleffect.entity.EducationalEffectCategory;
import psi.domain.educationaleffect.entity.EducationalEffectType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class EducationalEffectDTO {

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
