package psi.api.educationaleffect;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import psi.api.user.UserDTO;
import psi.domain.auditedobject.entity.ObjectState;
import psi.domain.educationaleffect.entity.EducationalEffectCategory;
import psi.domain.educationaleffect.entity.EducationalEffectType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Builder
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

    @NotNull
    private ObjectState objectState;

    @NotNull
    private UserDTO createdBy;

    @NotNull
    private Instant createdAt;

    @NotNull
    private UserDTO lastUpdatedBy;

    @NotNull
    private Instant lastUpdatedAt;

}
