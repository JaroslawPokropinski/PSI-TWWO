package psi.domain.disciplinetype.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import psi.domain.auditedobject.entity.AuditedObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import static psi.infrastructure.jpa.PersistenceConstants.ID_GENERATOR;

@Entity
@Getter
@Setter
@Audited
public class DisciplineType extends AuditedObject {

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    private Long id;

    @NotBlank
    private String name;

}
