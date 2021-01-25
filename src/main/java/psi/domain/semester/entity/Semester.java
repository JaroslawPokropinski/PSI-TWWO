package psi.domain.semester.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import psi.domain.studiesplan.entity.StudiesPlan;
import psi.domain.subjectcard.entity.SubjectCard;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import java.util.LinkedHashSet;
import java.util.Set;

import static psi.infrastructure.jpa.PersistenceConstants.ID_GENERATOR;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "SEMESTER")
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
