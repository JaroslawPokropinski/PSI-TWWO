package psi.domain.studiesplan;

import psi.domain.auditedobject.AuditedObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static psi.infrastructure.utils.PersistenceConstants.ID_GENERATOR;

@Entity
public class StudiesPlan extends AuditedObject {

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    private Long id;

}
