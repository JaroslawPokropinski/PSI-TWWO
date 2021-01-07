package psi.domain.block;

import lombok.Getter;
import psi.domain.studiesprogram.entity.StudiesProgram;
import psi.domain.subjectcard.entity.SubjectCard;

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

import static psi.infrastructure.jpa.PersistenceConstants.ID_GENERATOR;

@Entity
@Getter
public class Block {

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String mainType;

    @NotBlank
    private String type;

    @NotNull
    @ManyToOne
    private StudiesProgram studiesProgram;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "BLOCK_X_SUBJECT_CARD",
            joinColumns = @JoinColumn(name = "BLOCK_ID"),
            inverseJoinColumns = @JoinColumn(name = "SUBJECT_CARD_ID"))
    private Set<SubjectCard> subjectCards = new LinkedHashSet<>();

}
