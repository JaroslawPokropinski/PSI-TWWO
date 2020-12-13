package psi.domain.discipline;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import static psi.infrastructure.utils.PersistenceConstants.ID_GENERATOR;

@Entity
public class DisciplineType {

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    private Long id;

    @NotBlank
    private String name;

}
