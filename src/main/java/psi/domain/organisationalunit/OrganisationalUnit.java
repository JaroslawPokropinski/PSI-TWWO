package psi.domain.organisationalunit;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static psi.infrastructure.utils.PersistenceConstants.ID_GENERATOR;

@Entity
public class OrganisationalUnit {

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private OrganisationalUnitType type;

}
