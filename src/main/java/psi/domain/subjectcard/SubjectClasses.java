package psi.domain.subjectcard;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import java.util.LinkedHashSet;
import java.util.Set;

import static psi.infrastructure.utils.PersistenceConstants.ID_GENERATOR;

@Entity
public class SubjectClasses {

    @Id
    @GeneratedValue(generator = ID_GENERATOR)

    @Enumerated(EnumType.STRING)
    private SubjectClassesType subjectClassesType;

    private int zzuHours;

    private int cnpsHours;

    private boolean isFinalCourse;

    @Enumerated(EnumType.STRING)
    private CreditingForm creditingForm;

    private int ectsPoints;

    private int practicalEctsPoints;

    private int buEctsPoints;

    @ElementCollection
    @CollectionTable(name = "SUBJECT_CLASSES_PROGRAM")
    @OrderBy(ProgramContent_.NUMBER)
    private Set<ProgramContent> program = new LinkedHashSet<>();

}
