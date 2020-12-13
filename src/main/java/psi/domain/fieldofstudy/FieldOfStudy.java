package psi.domain.fieldofstudy;

import psi.domain.educationaleffect.EducationalEffect;
import psi.domain.organisationalunit.OrganisationalUnit;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;

import static psi.infrastructure.utils.PersistenceConstants.ID_GENERATOR;

@Entity
public class FieldOfStudy {

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @ManyToOne
    private OrganisationalUnit organisationalUnit;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "FIELD_OF_STUDY_X_EDUCATIONAL_EFFECT",
            joinColumns = @JoinColumn(name = "FIELD_OF_STUDY_ID"),
            inverseJoinColumns = @JoinColumn(name = "EDUCATIONAL_EFFECT_ID"))
    private Set<EducationalEffect> educationalEffects = new LinkedHashSet<>();

}
