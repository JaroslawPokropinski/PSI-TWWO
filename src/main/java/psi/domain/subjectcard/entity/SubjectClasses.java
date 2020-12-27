package psi.domain.subjectcard.entity;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.LinkedHashSet;
import java.util.Set;

import static psi.infrastructure.jpa.PersistenceConstants.ID_GENERATOR;

@Entity
public class SubjectClasses {

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SubjectClassesType subjectClassesType;

    @NotNull
    @Positive
    private Integer zzuHours;

    @NotNull
    @Positive
    private Integer cnpsHours;

    @NotNull
    private Boolean isFinalCourse;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CreditingForm creditingForm;

    @NotNull
    @PositiveOrZero
    private Integer ectsPoints;

    @NotNull
    @PositiveOrZero
    private Integer practicalEctsPoints;

    @NotNull
    @PositiveOrZero
    private Integer buEctsPoints;

    @ElementCollection
    @CollectionTable(name = "SUBJECT_CLASSES_PROGRAM")
    @OrderBy(ProgramContent_.NUMBER)
    private Set<ProgramContent> program = new LinkedHashSet<>();

}
