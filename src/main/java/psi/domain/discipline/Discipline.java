package psi.domain.discipline;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import static psi.infrastructure.utils.PersistenceConstants.ID_GENERATOR;

@Entity
public class Discipline {

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    private Long id;

    private String name;

    @ManyToOne
    private DisciplineType disciplineType;

}
