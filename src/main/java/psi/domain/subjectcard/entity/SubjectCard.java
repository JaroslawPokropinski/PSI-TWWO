package psi.domain.subjectcard.entity;

import lombok.Getter;
import org.hibernate.annotations.NaturalId;
import psi.domain.auditedobject.entity.AuditedObject;
import psi.domain.educationaleffect.entity.EducationalEffect;
import psi.domain.fieldofstudy.entity.FieldOfStudy;
import psi.domain.organisationalunit.entity.OrganisationalUnit;
import psi.domain.studiesprogram.StudiesForm;
import psi.domain.studiesprogram.StudiesLevel;
import psi.domain.user.entity.User;

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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static psi.infrastructure.jpa.PersistenceConstants.ID_GENERATOR;

@Entity
@Getter
@Table(name = "SUBJECT_CARD")
public class SubjectCard extends AuditedObject {

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    private Long id;

    @NotBlank
    private String subjectName;

    @NotBlank
    private String subjectNameInEnglish;

    @ManyToOne
    private FieldOfStudy mainFieldOfStudy;

    private String specialization;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StudiesLevel studiesLevel;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StudiesForm studiesForm;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SubjectType subjectType;

    @NaturalId
    @NotBlank
    @Column(unique = true, nullable = false)
    private String subjectCode;

    @NotNull
    private Boolean isGroupOfCourses;

    @NotNull
    @ManyToOne
    private OrganisationalUnit organisationalUnit;

    @ElementCollection
    @CollectionTable(name = "PREREQUISITE")
    @OrderBy(Item_.NUMBER)
    private Set<Item> prerequisites = new LinkedHashSet<>();

    @ElementCollection
    @CollectionTable(name = "SUBJECT_GOAL")
    @OrderBy(Item_.NUMBER)
    private Set<Item> subjectObjectives = new LinkedHashSet<>();

    @ElementCollection
    @CollectionTable(name = "LITERATURE")
    @OrderBy(Literature_.NUMBER)
    private Set<Literature> literature = new LinkedHashSet<>();

    @ElementCollection
    @CollectionTable(name = "TEACHING_TOOL")
    @OrderBy(Item_.NUMBER)
    private Set<Item> usedTeachingTools = new LinkedHashSet<>();

    @NotNull
    @ManyToOne
    private User supervisor;

    @Size(max = 5)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "SUBJECT_CARD_ID")
    private Set<SubjectClasses> subjectClasses = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "SUBJECT_CARD_X_EDUCATIONAL_EFFECT",
            joinColumns = @JoinColumn(name = "SUBJECT_CARD_ID"),
            inverseJoinColumns = @JoinColumn(name = "EDUCATIONAL_EFFECT_ID"))
    private Set<EducationalEffect> educationalEffects = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubjectCard)) return false;

        SubjectCard that = (SubjectCard) o;

        return Objects.equals(getSubjectCode(), that.getSubjectCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSubjectCode());
    }

}
