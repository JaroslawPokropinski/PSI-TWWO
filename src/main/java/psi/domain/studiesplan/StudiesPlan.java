package psi.domain.studiesplan;

import psi.domain.auditedobject.AuditedObject;
import psi.domain.studiesprogram.StudiesProgram;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.sql.Date;

import static psi.infrastructure.utils.PersistenceConstants.ID_GENERATOR;

@Entity
public class StudiesPlan extends AuditedObject {

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    private Long id;

    @NotBlank
    private String code;

    @NotNull
    private Date decreeDate;

    @NotNull
    private Date inEffectSince;

    @NotNull
    @ManyToOne
    private StudiesProgram studiesProgram;

}
