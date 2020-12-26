package psi.domain.studiesplan;

import lombok.Getter;
import org.hibernate.annotations.NaturalId;
import psi.domain.auditedobject.entity.AuditedObject;
import psi.domain.studiesprogram.StudiesProgram;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.sql.Date;
import java.util.Objects;

import static psi.infrastructure.jpa.PersistenceConstants.ID_GENERATOR;

@Entity
@Getter
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
