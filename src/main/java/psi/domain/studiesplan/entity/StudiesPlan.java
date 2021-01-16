package psi.domain.studiesplan.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.bytebuddy.implementation.bind.annotation.Super;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import psi.domain.auditedobject.entity.AuditedObject;
import psi.domain.studiesprogram.entity.StudiesProgram;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

import static psi.infrastructure.jpa.PersistenceConstants.ID_GENERATOR;

@Entity
@Getter
@Setter
@Audited
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Loader(namedQuery = "findStudiesPlanById")
@NamedQuery(name = "findStudiesPlanById", query = "SELECT s FROM StudiesPlan s WHERE s.id = ?1 AND s.objectState <> psi.domain.auditedobject.entity.ObjectState.REMOVED")
@Where(clause = AuditedObject.IS_NOT_REMOVED_OBJECT)
@Table(name = "STUDIES_PLAN")
public class StudiesPlan extends AuditedObject {

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    private Long id;

    @NaturalId
    @NotBlank
    @Column(unique = true, nullable = false)
    private String code;

    @NotNull
    private LocalDate decreeDate;

    @NotNull
    private LocalDate inEffectSince;

    @NotNull
    @ManyToOne
    private StudiesProgram studiesProgram;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudiesPlan)) return false;
        if (!super.equals(o)) return false;

        StudiesPlan that = (StudiesPlan) o;

        return Objects.equals(getCode(), that.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }
}
