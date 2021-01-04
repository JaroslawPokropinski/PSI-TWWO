package psi.domain.studiesplan;

import lombok.Getter;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Where;
import psi.domain.auditedobject.entity.AuditedObject;
import psi.domain.studiesprogram.entity.StudiesProgram;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.sql.Date;
import java.util.Objects;

import static psi.infrastructure.jpa.PersistenceConstants.ID_GENERATOR;

@Entity
@Getter
@Loader(namedQuery = "findStudiesPlanById")
@NamedQuery(name = "findStudiesPlanById", query = "SELECT s FROM StudiesPlan s WHERE s.id = ?1 AND s.objectState <> psi.domain.auditedobject.entity.ObjectState.REMOVED")
@Where(clause = AuditedObject.IS_NOT_REMOVED_OBJECT)
public class StudiesPlan extends AuditedObject {

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    private Long id;

    @NaturalId
    @NotBlank
    @Column(unique = true, nullable = false)
    private String code;

    @NotNull
    private Date decreeDate;

    @NotNull
    private Date inEffectSince;

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
