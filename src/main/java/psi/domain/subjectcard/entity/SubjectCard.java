package psi.domain.subjectcard.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import psi.domain.auditedobject.entity.AuditedObject;
import psi.domain.educationaleffect.entity.EducationalEffect;
import psi.domain.educationaleffect.entity.EducationalEffectCategory;
import psi.domain.fieldofstudy.entity.FieldOfStudy;
import psi.domain.organisationalunit.entity.OrganisationalUnit;
import psi.domain.simpleattribute.entity.SimpleAttribute;
import psi.domain.simpleattribute.entity.SimpleAttribute_;
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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static psi.infrastructure.jpa.PersistenceConstants.ID_GENERATOR;

@Entity
@Getter
@Setter
@Audited
@SuperBuilder
@NoArgsConstructor
@Loader(namedQuery = "findSubjectCardById")
@NamedQuery(name = "findSubjectCardById", query = "SELECT s FROM SubjectCard s WHERE s.id = ?1 AND s.objectState <> psi.domain.auditedobject.entity.ObjectState.REMOVED")
@Where(clause = AuditedObject.IS_NOT_REMOVED_OBJECT)
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
    @OrderBy(SimpleAttribute_.NUMBER)
    private Set<SimpleAttribute> prerequisites = new LinkedHashSet<>();

    @ElementCollection
    @CollectionTable(name = "SUBJECT_GOAL")
    @OrderBy(SimpleAttribute_.NUMBER)
    private Set<SimpleAttribute> subjectObjectives = new LinkedHashSet<>();

    @ElementCollection
    @CollectionTable(name = "LITERATURE")
    @OrderBy(Literature_.NUMBER)
    private Set<Literature> literature = new LinkedHashSet<>();

    @ElementCollection
    @CollectionTable(name = "TEACHING_TOOL")
    @OrderBy(SimpleAttribute_.NUMBER)
    private Set<SimpleAttribute> usedTeachingTools = new LinkedHashSet<>();

    @NotNull
    @ManyToOne
    private User supervisor;

    @Size(max = 5)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "SUBJECT_CARD_ID")
    private Set<SubjectClasses> subjectClasses = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "SUBJECT_CARD_X_EDUCATIONAL_EFFECT",
            joinColumns = @JoinColumn(name = "SUBJECT_CARD_ID"),
            inverseJoinColumns = @JoinColumn(name = "EDUCATIONAL_EFFECT_ID"))
    private Set<EducationalEffect> educationalEffects = new LinkedHashSet<>();

    public Optional<FieldOfStudy> getMainFieldOfStudy() {
        return Optional.ofNullable(mainFieldOfStudy);
    }

    public Optional<String> getSpecialization() {
        return Optional.ofNullable(specialization);
    }

    public Map<EducationalEffectCategory, List<String>> getEducationalEffectCodesByCategory() {
        return educationalEffects.stream()
                .sorted(Comparator.comparing(EducationalEffect::getCode))
                .collect(Collectors.groupingBy(EducationalEffect::getCategory, Collectors.mapping(EducationalEffect::getCode, Collectors.toList())));
    }

    public Map<SubjectClassesType, SubjectClasses> getSubjectClassesByType() {
        return subjectClasses.stream()
                .collect(Collectors.toMap(SubjectClasses::getSubjectClassesType, Function.identity(), (e1, e2) -> e1));
    }

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
