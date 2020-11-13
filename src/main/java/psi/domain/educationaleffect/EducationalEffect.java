package psi.domain.educationaleffect;

import psi.domain.auditedobject.AuditedObject;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static psi.infrastructure.utils.PersistenceConstants.ID_GENERATOR;

@Entity
@Table(name = "EDUCATIONAL_EFFFECT")
public class EducationalEffect extends AuditedObject {

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    private Long id;

    private String code;

    @Enumerated(EnumType.STRING)
    private EducationalEffectType type;

    private Integer prkLevel;

    private boolean isEngineerEffect;

    private boolean isLingualEffect;

    @Enumerated(EnumType.STRING)
    private EducationalEffectCategory category;

    private String description;

}
