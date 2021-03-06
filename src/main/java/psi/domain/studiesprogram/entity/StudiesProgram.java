package psi.domain.studiesprogram.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import psi.domain.auditedobject.entity.AuditedObject;
import psi.domain.discipline.entity.Discipline;
import psi.domain.educationaleffect.entity.EducationalEffect;
import psi.domain.fieldofstudy.entity.FieldOfStudy;
import psi.domain.simpleattribute.entity.SimpleAttribute;
import psi.domain.simpleattribute.entity.SimpleAttribute_;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static psi.infrastructure.jpa.PersistenceConstants.ID_GENERATOR;

@Entity
@Getter
@Setter
@Audited
@SuperBuilder
@NoArgsConstructor
@Loader(namedQuery = "findStudiesProgramById")
@NamedQuery(name = "findStudiesProgramById", query = "SELECT s FROM StudiesProgram s WHERE s.id = ?1 AND s.objectState <> psi.domain.auditedobject.entity.ObjectState.REMOVED")
@Where(clause = AuditedObject.IS_NOT_REMOVED_OBJECT)
@Table( name = "STUDIES_PROGRAM")
public class StudiesProgram extends AuditedObject {

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    private Long id;

    @NaturalId
    @Column(unique = true, nullable = false)
    private String code;

    @NotNull
    @ManyToOne
    private FieldOfStudy fieldOfStudy;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StudiesLevel studiesLevel;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StudiesForm studiesForm;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StudiesProfile studiesProfile;

    @NotNull
    @Positive
    private Integer numberOfSemesters;

    @NotNull
    @Positive
    private Integer totalNumberOfEctsPoints;

    @NotNull
    @Positive
    private Integer totalNumberOfHours;

    @ElementCollection
    @CollectionTable(name = "STUDIES_PROGRAM_PREREQUISITE")
    @OrderBy(SimpleAttribute_.NUMBER)
    private Set<SimpleAttribute> prerequisites = new LinkedHashSet<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    private DegreeTitle degreeTitle;

    @NotBlank
    private String languageOfStudies;

    @NotBlank
    @Column(length = 1000)
    private String graduateProfile;

    @NotBlank
    private String possibilityOfContinuingStudies;

    @NotBlank
    @Column(length = 1000)
    private String connectionWithMissionAndDevelopmentStrategy;

    @NotNull
    private LocalDate inEffectSince;

    @NotNull
    @ManyToOne
    private Discipline mainDiscipline;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "STUDIES_PROGRAM_X_DISCIPLINE",
            joinColumns = @JoinColumn(name = "STUDIES_PROGRAM_ID"),
            inverseJoinColumns = @JoinColumn(name = "DISCIPLINE_ID"))
    private Set<Discipline> disciplines = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "STUDIES_PROGRAM_X_EDUCATIONAL_EFFECT",
            joinColumns = @JoinColumn(name = "STUDIES_PROGRAM_ID"),
            inverseJoinColumns = @JoinColumn(name = "EDUCATIONAL_EFFECT_ID"))
    private Set<EducationalEffect> educationalEffects = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudiesProgram)) return false;

        StudiesProgram that = (StudiesProgram) o;

        return Objects.equals(getCode(), that.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }

}
