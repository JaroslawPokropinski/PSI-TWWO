package psi.domain.subjectcard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psi.domain.auditedobject.entity.ObjectState;
import psi.domain.educationaleffect.EducationalEffectGenerator;
import psi.domain.organisationalunit.entity.OrganisationalUnitType;
import psi.domain.organizationalunit.OrganizationalUnitGenerator;
import psi.domain.subjectcard.control.SubjectCardRepository;
import psi.domain.subjectcard.control.SubjectCardService;
import psi.domain.subjectcard.entity.SubjectCard;
import psi.infrastructure.collection.CollectionUtils;
import psi.domain.common.IdGenerator;
import psi.domain.common.UuidGenerator;
import psi.infrastructure.exception.ExceptionUtils;
import psi.infrastructure.exception.IllegalArgumentAppException;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    private final IdGenerator idGenerator = new IdGenerator(1L);
    private final UuidGenerator uuidGenerator = new UuidGenerator();
    private final EducationalEffectGenerator educationalEffectGenerator = new EducationalEffectGenerator(idGenerator, uuidGenerator);
    private final OrganizationalUnitGenerator organizationalUnitGenerator = new OrganizationalUnitGenerator(idGenerator, uuidGenerator);
    private final SubjectCardGenerator subjectCardGenerator = new SubjectCardGenerator(idGenerator, uuidGenerator, educationalEffectGenerator, organizationalUnitGenerator);

    @Test
    @DisplayName("Exception is thrown if subject code is not unique within already existing subject cards")
    public void testIfExceptionIsThrownWhenSubjectCodeIsNotUniqueAmongAlreadyExistingSubjectCards() {
        List<SubjectCard> alreadyExistingSubjectCards = subjectCardGenerator.generate(5);
        List<SubjectCard> subjectCardsToCreate = List.of(
                getNewDummySubjectCardWithTheSameSubjectCode(alreadyExistingSubjectCards.get(0)),
                getNewDummySubjectCardWithTheSameSubjectCode(alreadyExistingSubjectCards.get(3)),
                subjectCardGenerator.generate());
        List<String> duplicatedCodes = List.of(
                alreadyExistingSubjectCards.get(0).getSubjectCode(),
                alreadyExistingSubjectCards.get(3).getSubjectCode());
        when(subjectCardRepository.findAllBySubjectCodeIn(any(Collection.class))).thenReturn(alreadyExistingSubjectCards);
        IllegalArgumentAppException exception = assertThrows(IllegalArgumentAppException.class, () -> subjectCardService.createSubjectCards(subjectCardsToCreate), "Duplicated subject code was not detected by validations. Exception was not thrown!");
        assertTrue(exception.getMessage().contains("There are existing subject cards with given subject codes"), "Exception was thrown but because of wrong reason");
        duplicatedCodes.forEach(duplicatedCode -> assertTrue(exception.getMessage().contains(duplicatedCode), MessageFormat.format("Duplicated code {0} was not detected", duplicatedCode)));
    }

    private SubjectCard getNewDummySubjectCardWithTheSameSubjectCode(SubjectCard subjectCard) {
        return subjectCardGenerator.generate().toBuilder()
                .subjectCode(subjectCard.getSubjectCode())
                .build();
    }

    @Test
    @DisplayName("Exception is thrown when subject code is not unique within list of submitted subject cards to create")
    public void testIfExceptionIsThrownWhenSubjectCodeIsNotUniqueWithinListOfSubjectCardsToCreate() {
        SubjectCard dummySubjectCard = subjectCardGenerator.generate();
        List<SubjectCard> subjectCardsToCreate = List.of(
                getNewDummySubjectCardWithTheSameSubjectCode(dummySubjectCard),
                getNewDummySubjectCardWithTheSameSubjectCode(dummySubjectCard),
                subjectCardGenerator.generate());
        IllegalArgumentAppException exception = assertThrows(IllegalArgumentAppException.class, () -> subjectCardService.createSubjectCards(subjectCardsToCreate), "Duplicated subject code was not detected by validations. Exception was not thrown!");
        assertTrue(exception.getMessage().contains("Subject cards to create have non-unique subject codes!"));
    }

    @Test
    @DisplayName("Exception is thrown when field of study and faculty are inconsistent on create")
    public void testIfExceptionIsThrownWhenFieldOfStudyAndFacultyAreInconsistentOnCreate() {
        SubjectCard subjectCardToCreate = getSubjectCardWithFieldOfStudyFromDifferentFaculty();
        IllegalArgumentAppException exception = assertThrows(IllegalArgumentAppException.class, () -> subjectCardService.createSubjectCards(List.of(subjectCardToCreate)), "Wrong faculty for field of study was not detected! Exception was not thrown!");
        assertEquals("Some subject cards have different organisational unit than faculty of specified field of study.", exception.getMessage());
    }

    private SubjectCard getSubjectCardWithFieldOfStudyFromDifferentFaculty() {
        SubjectCard subjectCard = subjectCardGenerator.generate();
        return subjectCard.toBuilder()
                .organisationalUnit(organizationalUnitGenerator.generate(OrganisationalUnitType.FACULTY))
                .build();
    }

    @Test
    @DisplayName("Exception is thrown when field of study and faculty are inconsistent on update")
    public void testIfExceptionIsThrownWhenFieldOfStudyAndFacultyAreInconsistentOnUpdate() {
        SubjectCard subjectCardToUpdate = getSubjectCardWithFieldOfStudyFromDifferentFaculty();
        when(subjectCardRepository.findAllById(any())).thenReturn(List.of(subjectCardToUpdate));
        IllegalArgumentAppException exception = assertThrows(IllegalArgumentAppException.class, () -> subjectCardService.updateSubjectCards(List.of(subjectCardToUpdate), 1L), "Wrong faculty for field of study was not detected! Exception was not thrown!");
        assertEquals("Some subject cards have different organisational unit than faculty of specified field of study.", exception.getMessage());
    }

    @Test
    @DisplayName("Exception is thrown when tried to update non-existing subject card")
    public void testIfExceptionIsThrownWhenSubjectCardToBeUpdatedNotExist() {
        List<SubjectCard> existingSubjectCards = subjectCardGenerator.generate(5);
        List<SubjectCard> subjectCardsToUpdate = subjectCardGenerator.generate(1);
        when(subjectCardRepository.findAllById(any())).thenReturn(existingSubjectCards);
        IllegalArgumentAppException exception = assertThrows(IllegalArgumentAppException.class, () -> subjectCardService.updateSubjectCards(subjectCardsToUpdate, 1L), "Validation not raised, exception not thrown");
        assertEquals(ExceptionUtils.getObjectNotFoundException(SubjectCard.class, getIds(subjectCardsToUpdate)).getMessage(), exception.getMessage());
    }

    private Set<Long> getIds(Collection<SubjectCard> subjectCards) {
        return subjectCards.stream()
                .map(SubjectCard::getId)
                .collect(Collectors.toSet());
    }

    @Test
    @DisplayName("Exception is thrown when not all subject card to update have unique ids")
    public void testIfExceptionIsThrownWhenAllSubjectCardsOnUpdateDontHaveUniqueIds() {
        List<SubjectCard> existingSubjectCards = subjectCardGenerator.generate(5);
        List<SubjectCard> subjectCardsToUpdateWithDuplicates =
                CollectionUtils.union(existingSubjectCards, subjectCardGenerator.generate(3), existingSubjectCards);
        IllegalArgumentAppException exception = assertThrows(IllegalArgumentAppException.class, () -> subjectCardService.updateSubjectCards(subjectCardsToUpdateWithDuplicates, 1L));
        assertEquals("There are some subject cards without id or ids are not unique!", exception.getMessage());
    }

    @Test
    @DisplayName("Subject cards state is changed to removed after deletion")
    public void testIfSubjectCardsStateIsChangedToRemovedAfterDelete() {
        List<SubjectCard> subjectCardsToDelete = subjectCardGenerator.generate(5);
        Set<Long> idsOfSubjectCardsToDelete = getIds(subjectCardsToDelete);
        when(subjectCardRepository.findAllById(idsOfSubjectCardsToDelete)).thenReturn(subjectCardsToDelete);
        subjectCardService.deleteSubjectCards(idsOfSubjectCardsToDelete, 1L);
        subjectCardsToDelete.forEach(subjectCard -> assertEquals(ObjectState.REMOVED, subjectCard.getObjectState()));
    }

    @Test
    @DisplayName("Exception is thrown when tried to delete non-existing subject card")
    public void testIfExceptionIsThrownWhenTriedToRemoveNonExistingCard() {
        List<SubjectCard> existingSubjectCardsToDelete = subjectCardGenerator.generate(5);
        List<SubjectCard> nonExistingSubjectCardsToDelete = subjectCardGenerator.generate(5);
        List<SubjectCard> allSubjectCardsToDelete = CollectionUtils.union(existingSubjectCardsToDelete, nonExistingSubjectCardsToDelete);
        Set<Long> idsOfSubjectCardsToDelete = getIds(allSubjectCardsToDelete);
        when(subjectCardRepository.findAllById(idsOfSubjectCardsToDelete)).thenReturn(existingSubjectCardsToDelete);
        IllegalArgumentAppException exception = assertThrows(IllegalArgumentAppException.class, () -> subjectCardService.deleteSubjectCards(idsOfSubjectCardsToDelete, 1L));
        assertEquals(ExceptionUtils.getObjectNotFoundException(SubjectCard.class, getIds(nonExistingSubjectCardsToDelete)).getMessage(), exception.getMessage());
    }

}
