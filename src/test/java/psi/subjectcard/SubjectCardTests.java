package psi.subjectcard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psi.domain.educationaleffect.entity.EducationalEffect;
import psi.domain.educationaleffect.entity.EducationalEffectCategory;
import psi.domain.educationaleffect.entity.EducationalEffectType;
import psi.domain.fieldofstudy.entity.FieldOfStudy;
import psi.domain.organisationalunit.entity.OrganisationalUnit;
import psi.domain.organisationalunit.entity.OrganisationalUnitType;
import psi.domain.simpleattribute.entity.SimpleAttribute;
import psi.domain.studiesprogram.entity.StudiesForm;
import psi.domain.studiesprogram.entity.StudiesLevel;
import psi.domain.subjectcard.control.SubjectCardRepository;
import psi.domain.subjectcard.control.SubjectCardService;
import psi.domain.subjectcard.entity.CreditingForm;
import psi.domain.subjectcard.entity.Literature;
import psi.domain.subjectcard.entity.ProgramContent;
import psi.domain.subjectcard.entity.SubjectCard;
import psi.domain.subjectcard.entity.SubjectClasses;
import psi.domain.subjectcard.entity.SubjectClassesType;
import psi.domain.subjectcard.entity.SubjectType;
import psi.domain.user.entity.User;
import psi.infrastructure.datageneration.DataGenerator;
import psi.infrastructure.datageneration.SequenceIdGenerator;
import psi.infrastructure.datageneration.UuidGenerator;
import psi.infrastructure.exception.ExceptionUtils;
import psi.infrastructure.exception.IllegalArgumentAppException;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Subject card tests")
@ExtendWith(MockitoExtension.class)
public class SubjectCardTests {

    @Mock
    private SubjectCardRepository subjectCardRepository;

    @InjectMocks
    private SubjectCardService subjectCardService;

    private DataGenerator<Long> idGenerator = new SequenceIdGenerator(1L);
    private DataGenerator<String> uuidGenerator = new UuidGenerator();

    @Test
    @DisplayName("Exception is thrown if subject code is not unique within already existing subject cards")
    public void testIfExceptionIsThrownWhenSubjectCodeIsNotUniqueAmongAlreadyExistingSubjectCards() {
        List<SubjectCard> alreadyExistingSubjectCards = generateDummySubjectCards(5);
        List<SubjectCard> subjectCardsToCreate = List.of(
                getNewDummySubjectCardWithTheSameSubjectCode(alreadyExistingSubjectCards.get(0)),
                getNewDummySubjectCardWithTheSameSubjectCode(alreadyExistingSubjectCards.get(3)),
                generateDummySubjectCard());
        List<String> duplicatedCodes = List.of(
                alreadyExistingSubjectCards.get(0).getSubjectCode(),
                alreadyExistingSubjectCards.get(3).getSubjectCode());
        when(subjectCardRepository.findAllBySubjectCodeIn(any(Collection.class))).thenReturn(alreadyExistingSubjectCards);
        IllegalArgumentAppException exception = assertThrows(IllegalArgumentAppException.class, () -> subjectCardService.createSubjectCards(subjectCardsToCreate), "Duplicated subject code was not detected by validations. Exception was not thrown!");
        assertTrue(exception.getMessage().contains("There are existing subject cards with given subject codes"), "Exception was thrown but because of wrong reason");
        duplicatedCodes.forEach(duplicatedCode -> assertTrue(exception.getMessage().contains(duplicatedCode), MessageFormat.format("Duplicated code {0} was not detected", duplicatedCode)));
    }

    private List<SubjectCard> generateDummySubjectCards(int count) {
        return Stream.generate(this::generateDummySubjectCard)
                .limit(count)
                .collect(Collectors.toList());
    }

    private SubjectCard generateDummySubjectCard() {
        FieldOfStudy dummyFieldOfStudy = generateDummyFieldOfStudy();
        return SubjectCard.builder()
                .id(idGenerator.generateNext())
                .subjectName("Dummy Subject Name")
                .subjectNameInEnglish("Dummy Subject Name In English")
                .mainFieldOfStudy(dummyFieldOfStudy)
                .specialization("Dummy Specialization")
                .studiesLevel(StudiesLevel.FIRST)
                .studiesForm(StudiesForm.FULL_TIME)
                .subjectType(SubjectType.OBLIGATORY)
                .subjectCode(uuidGenerator.generateNext())
                .isGroupOfCourses(false)
                .organisationalUnit(dummyFieldOfStudy.getOrganisationalUnit())
                .prerequisites(getDummySimpleAttribute(3, this::getDummyPrerequisite))
                .subjectObjectives(getDummySimpleAttribute(3, this::getDummySubjectObjectives))
                .literature(getDummyLiterature(5))
                .usedTeachingTools(getDummySimpleAttribute(3, this::getDummyTeachingToolsUsed))
                .supervisor(generateDummyUser())
                .subjectClasses(getDummySubjectClasses(EnumSet.allOf(SubjectClassesType.class)))
                .educationalEffects(generateDummyEducationalEffects(EnumSet.allOf(EducationalEffectCategory.class), 3))
                .build();
    }

    private FieldOfStudy generateDummyFieldOfStudy() {
        return FieldOfStudy.builder()
                .id(idGenerator.generateNext())
                .name(uuidGenerator.generateNext())
                .organisationalUnit(generateDummyFaculty())
                .build();
    }

    private OrganisationalUnit generateDummyFaculty() {
        return new OrganisationalUnit(idGenerator.generateNext(), uuidGenerator.generateNext(), OrganisationalUnitType.FACULTY);
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
        return Stream.of(getDummyLiterature(count, this::getDummyPrimaryLiterature), getDummyLiterature(count, this::getDummySecondaryLiterature))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
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
                .id(idGenerator.generateNext())
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

    private Set<EducationalEffect> generateDummyEducationalEffects(Set<EducationalEffectCategory> categories, int count) {
        return categories.stream()
                .map(category -> generateDummyEducationalEffects(category, count))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<EducationalEffect> generateDummyEducationalEffects(EducationalEffectCategory category, int count) {
        return Stream.generate(() -> generateDummyEducationalEffect(category))
                .limit(count)
                .collect(Collectors.toSet());
    }

    private EducationalEffect generateDummyEducationalEffect(EducationalEffectCategory category) {
        return EducationalEffect.builder()
                .id(idGenerator.generateNext())
                .code(uuidGenerator.generateNext())
                .type(EducationalEffectType.SUBJECT)
                .prkLevel(4)
                .isEngineerEffect(false)
                .isLingualEffect(false)
                .category(category)
                .description("Dummy description")
                .build();
    }

    private SubjectCard getNewDummySubjectCardWithTheSameSubjectCode(SubjectCard subjectCard) {
        return generateDummySubjectCard().toBuilder()
                .subjectCode(subjectCard.getSubjectCode())
                .build();
    }

    @Test
    @DisplayName("Exception is thrown when subject code is not unique within list of submitted subject cards to create/edit")
    public void testIfExceptionIsThrownWhenSubjectCodeIsNotUniqueWithinListOfSubjectCardsToCreate() {
        SubjectCard dummySubjectCard = generateDummySubjectCard();
        List<SubjectCard> subjectCardsToCreate = List.of(
                getNewDummySubjectCardWithTheSameSubjectCode(dummySubjectCard),
                getNewDummySubjectCardWithTheSameSubjectCode(dummySubjectCard),
                generateDummySubjectCard());
        IllegalArgumentAppException exception = assertThrows(IllegalArgumentAppException.class, () -> subjectCardService.createSubjectCards(subjectCardsToCreate), "Duplicated subject code was not detected by validations. Exception was not thrown!");
        assertTrue(exception.getMessage().contains("Subject cards to create have non-unique subject codes!"));
    }

    @Test
    @DisplayName("Exception is thrown when field of study and faculty are inconsistent on create")
    public void testIfExceptionIsThrownWhenFieldOfStudyAndFacultyAreInconsistent() {
        SubjectCard subjectCardToCreate = getSubjectCardWithFieldOfStudyFromDifferentFaculty();
        IllegalArgumentAppException exception = assertThrows(IllegalArgumentAppException.class, () -> subjectCardService.createSubjectCards(List.of(subjectCardToCreate)), "Wrong faculty for field of study was not detected! Exception was not thrown!");
        assertEquals("Some subject cards have different organisational unit than faculty of specified field of study.", exception.getMessage());
    }

    private SubjectCard getSubjectCardWithFieldOfStudyFromDifferentFaculty() {
        SubjectCard subjectCard = generateDummySubjectCard();
        return subjectCard.toBuilder()
                .organisationalUnit(generateDummyFaculty())
                .build();
    }

    @Test
    @DisplayName("Exception is thrown when subject card to be updated does not exist")
    public void testIfExceptionIsThrownWhenSubjectCardToBeUpdatedNotExist() {
        List<SubjectCard> existingSubjectCards = generateDummySubjectCards(5);
        List<SubjectCard> subjectCardsToUpdate = generateDummySubjectCards(1);
        when(subjectCardRepository.findAllById(any())).thenReturn(existingSubjectCards);
        IllegalArgumentAppException exception = assertThrows(IllegalArgumentAppException.class, () -> subjectCardService.updateSubjectCards(subjectCardsToUpdate, 1L), "Validation not raised, exception not thrown");
        assertEquals(ExceptionUtils.getObjectNotFoundException(SubjectCard.class, getIds(subjectCardsToUpdate)).getMessage(), exception.getMessage());
    }

    private Set<Long> getIds(Collection<SubjectCard> subjectCards) {
        return subjectCards.stream()
                .map(SubjectCard::getId)
                .collect(Collectors.toSet());
    }

}
