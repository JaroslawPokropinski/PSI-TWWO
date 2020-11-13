package psi.domain.subjectcard;

import psi.domain.auditedobject.AuditedObject;
import psi.domain.fieldofstudy.FieldOfStudy;
import psi.domain.organisationalunit.OrganisationalUnit;
import psi.domain.studiesprogram.StudiesForm;
import psi.domain.studiesprogram.StudiesLevel;
import psi.domain.user.User;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import java.util.LinkedHashSet;
import java.util.Set;

import static psi.infrastructure.utils.PersistenceConstants.ID_GENERATOR;

@Entity
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

    @Enumerated(EnumType.STRING)
    private StudiesLevel studiesLevel;

    @Enumerated(EnumType.STRING)
    private StudiesForm studiesForm;

    @Enumerated(EnumType.STRING)
    private SubjectType subjectType;

    @NotBlank
    private String subjectCode;

    private boolean isGroupOfCourses;

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
    @CollectionTable(name = "PRIMARY_LITERATURE")
    @OrderBy(Item_.NUMBER)
    private Set<Item> primaryLiterature = new LinkedHashSet<>();

    @ElementCollection
    @CollectionTable(name = "SECONDARY_LITERATURE")
    @OrderBy(Item_.NUMBER)
    private Set<Item> secondaryLiterature = new LinkedHashSet<>();

    @ElementCollection
    @CollectionTable(name = "TEACHING_TOOL")
    @OrderBy(Item_.NUMBER)
    private Set<Item> usedTeachingTools = new LinkedHashSet<>();

    @ManyToOne
    private User supervisor;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SubjectClasses> subjectClasses = new LinkedHashSet<>();

}
