package psi.domain.educationaleffect.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.NaturalId;
import org.hibernate.envers.Audited;
import psi.domain.auditedobject.entity.AuditedObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Objects;

import static psi.infrastructure.jpa.PersistenceConstants.ID_GENERATOR;

@Entity
@Getter
@Setter
@Audited
@SuperBuilder
@NoArgsConstructor
@Table(name = "EDUCATIONAL_EFFFECT")
public class EducationalEffect extends AuditedObject {

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    private Long id;

    @NaturalId
    @NotBlank
    @Column(unique = true, nullable = false)
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EducationalEffectType type;

    @NotNull
    private Integer prkLevel;

    @NotNull
    private Boolean isEngineerEffect;

    @NotNull
    private Boolean isLingualEffect;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EducationalEffectCategory category;

    @NotBlank
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EducationalEffect)) return false;

        EducationalEffect that = (EducationalEffect) o;

        return Objects.equals(getCode(), that.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

}
