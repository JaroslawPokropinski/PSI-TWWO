package psi.domain.fieldofstudy;

import psi.domain.organisationalunit.OrganisationalUnit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import static psi.infrastructure.utils.PersistenceConstants.ID_GENERATOR;

@Entity
public class FieldOfStudy {

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    private Long id;

    private String name;

    @ManyToOne
    private OrganisationalUnit organisationalUnit;

}
