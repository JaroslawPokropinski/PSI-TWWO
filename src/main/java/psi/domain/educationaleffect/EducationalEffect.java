package psi.domain.educationaleffect;

import lombok.Getter;
import psi.domain.auditedobject.AuditedObject;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static psi.infrastructure.utils.PersistenceConstants.ID_GENERATOR;

@Entity
@Getter
@Table(name = "EDUCATIONAL_EFFFECT")
public class EducationalEffect extends AuditedObject {

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    private Long id;

    @NotBlank
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EducationalEffectType type;

    @NotNull
    private Integer prkLevel;

    private boolean isEngineerEffect;

    private boolean isLingualEffect;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EducationalEffectCategory category;

    @NotBlank
    private String description;

}
