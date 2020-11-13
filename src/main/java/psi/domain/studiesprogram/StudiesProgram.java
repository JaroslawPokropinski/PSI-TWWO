package psi.domain.studiesprogram;

import psi.domain.auditedobject.AuditedObject;
import psi.domain.fieldofstudy.FieldOfStudy;
import psi.domain.organisationalunit.OrganisationalUnit;
import psi.domain.subjectcard.Item;
import psi.domain.subjectcard.Item_;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import static psi.infrastructure.utils.PersistenceConstants.ID_GENERATOR;

@Entity
public class StudiesProgram extends AuditedObject {

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    private Long id;

    @ManyToOne
    private OrganisationalUnit faculty;

    @Enumerated(EnumType.STRING)
    private StudiesLevel studiesLevel;

    @Enumerated(EnumType.STRING)
    private StudiesForm studiesForm;

    @Enumerated(EnumType.STRING)
    private StudiesProfile studiesProfile;

    @ManyToOne
    private FieldOfStudy fieldOfStudy;

    int numberOfSemesters;

    int totalNumberOfEctsPoints;

    int totalNumberOfHours;

    @ElementCollection
    @CollectionTable(name = "PREREQUISITE")
    @OrderBy(Item_.NUMBER)
    private Set<Item> prerequisites = new LinkedHashSet<>();

    @Enumerated(EnumType.STRING)
    private DegreeTitle degreeTitle;

    private String languageOfStudies;

    @Column(length = 10000)
    private String graduateProfile;

    private String possibilityOfContinuingStudies;

    @Column(length = 10000)
    private String connectionWithMissionAndDevelopmentStrategy;

    private LocalDate inEffectSince;

}
