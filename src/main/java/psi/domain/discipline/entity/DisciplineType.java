package psi.domain.discipline.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import static psi.infrastructure.jpa.PersistenceConstants.ID_GENERATOR;

@Entity
public class DisciplineType {

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    private Long id;

    @NotBlank
    private String name;

}
