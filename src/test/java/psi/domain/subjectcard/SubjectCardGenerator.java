package psi.domain.subjectcard;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.SetUtils;
import psi.domain.educationaleffect.EducationalEffectGenerator;
import psi.domain.fieldofstudy.entity.FieldOfStudy;
import psi.domain.organisationalunit.entity.OrganisationalUnitType;
import psi.domain.organizationalunit.OrganizationalUnitGenerator;
import psi.domain.simpleattribute.entity.SimpleAttribute;
import psi.domain.studiesprogram.entity.StudiesForm;
import psi.domain.studiesprogram.entity.StudiesLevel;
import psi.domain.subjectcard.entity.CreditingForm;
import psi.domain.subjectcard.entity.Literature;
import psi.domain.subjectcard.entity.ProgramContent;
import psi.domain.subjectcard.entity.SubjectCard;
import psi.domain.subjectcard.entity.SubjectClasses;
import psi.domain.subjectcard.entity.SubjectClassesType;
import psi.domain.subjectcard.entity.SubjectType;
import psi.domain.user.entity.User;
import psi.domain.common.IdGenerator;
import psi.domain.common.UuidGenerator;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class SubjectCardGenerator {

    private final IdGenerator idGenerator;
    private final UuidGenerator uuidGenerator;
    private final EducationalEffectGenerator educationalEffectGenerator;
    private final OrganizationalUnitGenerator organizationalUnitGenerator;

    public SubjectCard generate() {
        return generateDummySubjectCard();
    }

    public List<SubjectCard> generate(int count) {
        return generateDummySubjectCards(count);
    }

    private List<SubjectCard> generateDummySubjectCards(int count) {
        return Stream.generate(this::generateDummySubjectCard)
                .limit(count)
                .collect(Collectors.toList());
    }

    private SubjectCard generateDummySubjectCard() {
        FieldOfStudy dummyFieldOfStudy = generateDummyFieldOfStudy();
        return SubjectCard.builder()
                .id(idGenerator.generate())
                .subjectName("Dummy Subject Name")
                .subjectNameInEnglish("Dummy Subject Name In English")
                .mainFieldOfStudy(dummyFieldOfStudy)
                .specialization("Dummy Specialization")
                .studiesLevel(StudiesLevel.FIRST)
                .studiesForm(StudiesForm.FULL_TIME)
                .subjectType(SubjectType.OBLIGATORY)
                .subjectCode(uuidGenerator.generate())
                .isGroupOfCourses(false)
                .organisationalUnit(dummyFieldOfStudy.getOrganisationalUnit())
                .prerequisites(getDummySimpleAttribute(3, this::getDummyPrerequisite))
                .subjectObjectives(getDummySimpleAttribute(3, this::getDummySubjectObjectives))
                .literature(getDummyLiterature(5))
                .usedTeachingTools(getDummySimpleAttribute(3, this::getDummyTeachingToolsUsed))
                .supervisor(generateDummyUser())
                .subjectClasses(getDummySubjectClasses(EnumSet.allOf(SubjectClassesType.class)))
                .educationalEffects(new HashSet<>(educationalEffectGenerator.generate(3)))
                .build();
    }

    private FieldOfStudy generateDummyFieldOfStudy() {
        return FieldOfStudy.builder()
                .id(idGenerator.generate())
                .name(uuidGenerator.generate())
                .organisationalUnit(organizationalUnitGenerator.generate(OrganisationalUnitType.FACULTY))
                .build();
    }

    private Set<SimpleAttribute> getDummySimpleAttribute(int count, Function<Integer, SimpleAttribute> simpleAttributeGenerator) {
        return IntStream.range(0, count)
                .boxed()
                .map(this::getDummyPrerequisite)
                .collect(Collectors.toSet());
    }

    private SimpleAttribute getDummyPrerequisite(int number) {
        return new SimpleAttribute(number, "Dummy prerequisite " + number);
    }

    private SimpleAttribute getDummySubjectObjectives(int number) {
        return new SimpleAttribute(number, "Dummy objective " + number);
    }

    private SimpleAttribute getDummyTeachingToolsUsed(int number) {
        return new SimpleAttribute(number, "Dummy objective " + number);
    }

    private Set<Literature> getDummyLiterature(int count) {
        return SetUtils.union(getDummyLiterature(count, this::getDummyPrimaryLiterature), getDummyLiterature(count, this::getDummySecondaryLiterature));
    }

    private Set<Literature> getDummyLiterature(int count, Function<Integer, Literature> literatureGenerator) {
        return IntStream.range(0, count)
                .boxed()
                .map(literatureGenerator)
                .collect(Collectors.toSet());
    }

    private Literature getDummyPrimaryLiterature(int number) {
        return new Literature(number, "Primary literature " + number, true);
    }

    private Literature getDummySecondaryLiterature(int number) {
        return new Literature(number, "Secondary literature " + number, false);
    }

    private User generateDummyUser() {
        return User.builder()
                .id(idGenerator.generate())
                .name("John")
                .surname("Example")
                .username("dummy")
                .password("12345678")
                .email("dummy@example.com")
                .phoneNumber("123")
                .build();
    }

    private Set<SubjectClasses> getDummySubjectClasses(Set<SubjectClassesType> subjectClassesTypes) {
        return subjectClassesTypes.stream()
                .map(this::getDummySubjectClasses)
                .collect(Collectors.toSet());
    }

    private SubjectClasses getDummySubjectClasses(SubjectClassesType subjectClassesType) {
        return SubjectClasses.builder()
                .subjectClassesType(subjectClassesType)
                .zzuHours(30)
                .cnpsHours(60)
                .isFinalCourse(false)
                .creditingForm(CreditingForm.CREDITING_WITH_GRADE)
                .ectsPoints(4)
                .practicalEctsPoints(1)
                .buEctsPoints(1)
                .program(getDummyProgramContents(10))
                .build();
    }

    private Set<ProgramContent> getDummyProgramContents(int count) {
        return IntStream.range(0, count)
                .boxed()
                .map(this::getDummyProgramContent)
                .collect(Collectors.toSet());
    }

    private ProgramContent getDummyProgramContent(int number) {
        return new ProgramContent(number, "Program content " + number, 5D);
    }

}
