package psi.domain.studiesprogram;

import lombok.Getter;
import org.hibernate.annotations.NaturalId;
import psi.domain.auditedobject.AuditedObject;
import psi.domain.discipline.Discipline;
import psi.domain.fieldofstudy.entity.FieldOfStudy;
import psi.domain.subjectcard.Item;
import psi.domain.subjectcard.Item_;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static psi.infrastructure.jpa.PersistenceConstants.ID_GENERATOR;

@Entity
@Getter
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
    @CollectionTable(name = "PREREQUISITE")
    @OrderBy(Item_.NUMBER)
    private Set<Item> prerequisites = new LinkedHashSet<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    private DegreeTitle degreeTitle;

    @NotBlank
    private String languageOfStudies;

    @NotBlank
    @Column(length = 10000)
    private String graduateProfile;

    @NotBlank
    private String possibilityOfContinuingStudies;

    @NotBlank
    @Column(length = 10000)
    private String connectionWithMissionAndDevelopmentStrategy;

    @NotNull
    private LocalDate inEffectSince;

    @NotNull
    @ManyToOne
    private Discipline mainDiscipline;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "STUDIES_PROGRAM_X_EDUCATIONAL_EFFECT",
            joinColumns = @JoinColumn(name = "STUDIES_PROGRAM_ID"),
            inverseJoinColumns = @JoinColumn(name = "DISCIPLINE_ID"))
    private Set<Discipline> disciplines = new LinkedHashSet<>();

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
