package psi.domain.semester;

import lombok.Getter;
import psi.domain.studiesplan.StudiesPlan;
import psi.domain.subjectcard.SubjectCard;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import java.util.LinkedHashSet;
import java.util.Set;

import static psi.infrastructure.utils.PersistenceConstants.ID_GENERATOR;

@Entity
@Getter
public class Semester {

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    private Long id;

    @NotNull
    @Positive
    private Integer number;

    @NotNull
    @PositiveOrZero
    private Integer allowedEctsDeficit;

    @NotNull
    @ManyToOne
    private StudiesPlan studiesPlan;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "SEMESTER_X_SUBJECT_CARD",
            joinColumns = @JoinColumn(name = "SEMESTER_ID"),
            inverseJoinColumns = @JoinColumn(name = "SUBJECT_CARD_ID"))
    private Set<SubjectCard> subjectCards = new LinkedHashSet<>();

}
