package psi.domain.educationaleffect;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import psi.domain.auditedobject.entity.ObjectState;
import psi.domain.common.IdGenerator;
import psi.domain.common.UuidGenerator;
import psi.domain.educationaleffect.control.EducationalEffectRepository;
import psi.domain.educationaleffect.control.EducationalEffectService;
import psi.domain.educationaleffect.entity.EducationalEffect;
import psi.domain.educationaleffect.entity.EducationalEffectCategory;
import psi.infrastructure.collection.CollectionUtils;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@DisplayName("Educational effect tests")
@ExtendWith(MockitoExtension.class)
public class EducationalEffectTests {

    @Mock
    private EducationalEffectRepository educationalEffectRepository;

    @InjectMocks
    private EducationalEffectService educationalEffectService;

    private final IdGenerator idGenerator = new IdGenerator(1L);
    private final UuidGenerator uuidGenerator = new UuidGenerator();
    private final EducationalEffectGenerator educationalEffectGenerator = new EducationalEffectGenerator(idGenerator, uuidGenerator);

    @Test
    @DisplayName("Exception is thrown if code is not unique within already existing educational effects")
    public void testIfExceptionIsThrownWhenCodeIsNotUniqueAmongAlreadyExistingEducationalEffects() {
        List<EducationalEffect> alreadyExistingEducationalEffects = educationalEffectGenerator.generate(5);
        List<EducationalEffect> educationalEffectsToCreate = List.of(
                getNewDummyEducationalEffectWithTheSameCode(alreadyExistingEducationalEffects.get(0)),
                getNewDummyEducationalEffectWithTheSameCode(alreadyExistingEducationalEffects.get(3)),
                educationalEffectGenerator.generate(EducationalEffectCategory.SKILLS));
        List<String> duplicatedCodes = List.of(
                alreadyExistingEducationalEffects.get(0).getCode(),
                alreadyExistingEducationalEffects.get(3).getCode());
        when(educationalEffectRepository.findAllByCodeIn(any(Collection.class))).thenReturn(alreadyExistingEducationalEffects);
        IllegalArgumentAppException exception = assertThrows(IllegalArgumentAppException.class, () -> educationalEffectService.createEducationalEffects(educationalEffectsToCreate), "Duplicated code was not detected by validations. Exception was not thrown!");
        assertTrue(exception.getMessage().contains("There are existing educational effects with given codes"), "Exception was thrown but because of wrong reason");
        duplicatedCodes.forEach(duplicatedCode -> assertTrue(exception.getMessage().contains(duplicatedCode), MessageFormat.format("Duplicated code {0} was not detected", duplicatedCode)));
    }

    private EducationalEffect getNewDummyEducationalEffectWithTheSameCode(EducationalEffect educationalEffect) {
        return educationalEffectGenerator.generate(EducationalEffectCategory.SKILLS).toBuilder()
                .code(educationalEffect.getCode())
                .build();
    }

    @Test
    @DisplayName("Exception is thrown when code is not unique within list of submitted educational effects to create")
    public void testIfExceptionIsThrownWhenCodeIsNotUniqueWithinListOfEducationalEffectsToCreate() {
        EducationalEffect dummyEducationalEffect = educationalEffectGenerator.generate(EducationalEffectCategory.SKILLS);
        List<EducationalEffect> educationalEffectsToCreate = List.of(
                getNewDummyEducationalEffectWithTheSameCode(dummyEducationalEffect),
                getNewDummyEducationalEffectWithTheSameCode(dummyEducationalEffect),
                educationalEffectGenerator.generate(EducationalEffectCategory.SKILLS));
        IllegalArgumentAppException exception = assertThrows(IllegalArgumentAppException.class, () -> educationalEffectService.createEducationalEffects(educationalEffectsToCreate), "Duplicated code was not detected by validations. Exception was not thrown!");
        assertTrue(exception.getMessage().contains("Educational effects have non-unique codes!"));
    }

    @Test
    @DisplayName("Exception is thrown when tried to update non-existing educational effect")
    public void testIfExceptionIsThrownWhenEducationalEffectToBeUpdatedNotExist() {
        List<EducationalEffect> existingEducationalEffects = educationalEffectGenerator.generate(5);
        List<EducationalEffect> educationalEffectsToUpdate = educationalEffectGenerator.generate(1);
        when(educationalEffectRepository.findAllById(any())).thenReturn(existingEducationalEffects);
        IllegalArgumentAppException exception = assertThrows(IllegalArgumentAppException.class, () -> educationalEffectService.updateEducationalEffects(educationalEffectsToUpdate, 1L), "Validation not raised, exception not thrown");
        assertEquals(ExceptionUtils.getObjectNotFoundException(EducationalEffect.class, getIds(educationalEffectsToUpdate)).getMessage(), exception.getMessage());
    }

    private Set<Long> getIds(Collection<EducationalEffect> educationalEffects) {
        return educationalEffects.stream()
                .map(EducationalEffect::getId)
                .collect(Collectors.toSet());
    }

    @Test
    @DisplayName("Exception is thrown when not all educational effect to update have unique ids")
    public void testIfExceptionIsThrownWhenAllEducationalEffectsOnUpdateDontHaveUniqueIds() {
        List<EducationalEffect> existingEducationalEffects = educationalEffectGenerator.generate(5);
        List<EducationalEffect> educationalEffectsToUpdateWithDuplicates =
                CollectionUtils.union(existingEducationalEffects, educationalEffectGenerator.generate(3), existingEducationalEffects);
        IllegalArgumentAppException exception = assertThrows(IllegalArgumentAppException.class, () -> educationalEffectService.updateEducationalEffects(educationalEffectsToUpdateWithDuplicates, 1L));
        assertEquals("There are some educational effects without id or ids are not unique!", exception.getMessage());
    }

    @Test
    @DisplayName("Subject cards state is changed to removed after deletion")
    public void testIfEducationalEffectsStateIsChangedToRemovedAfterDelete() {
        List<EducationalEffect> educationalEffectsToDelete = educationalEffectGenerator.generate(5);
        Set<Long> idsOfEducationalEffectsToDelete = getIds(educationalEffectsToDelete);
        when(educationalEffectRepository.findAllById(idsOfEducationalEffectsToDelete)).thenReturn(educationalEffectsToDelete);
        educationalEffectService.deleteEducationalEffects(idsOfEducationalEffectsToDelete, 1L);
        educationalEffectsToDelete.forEach(educationalEffect -> assertEquals(ObjectState.REMOVED, educationalEffect.getObjectState()));
    }

    @Test
    @DisplayName("Exception is thrown when tried to delete non-existing educational effect")
    public void testIfExceptionIsThrownWhenTriedToRemoveNonExistingCard() {
        List<EducationalEffect> existingEducationalEffectsToDelete = educationalEffectGenerator.generate(5);
        List<EducationalEffect> nonExistingEducationalEffectsToDelete = educationalEffectGenerator.generate(5);
        List<EducationalEffect> allEducationalEffectsToDelete = CollectionUtils.union(existingEducationalEffectsToDelete, nonExistingEducationalEffectsToDelete);
        Set<Long> idsOfEducationalEffectsToDelete = getIds(allEducationalEffectsToDelete);
        when(educationalEffectRepository.findAllById(idsOfEducationalEffectsToDelete)).thenReturn(existingEducationalEffectsToDelete);
        IllegalArgumentAppException exception = assertThrows(IllegalArgumentAppException.class, () -> educationalEffectService.deleteEducationalEffects(idsOfEducationalEffectsToDelete, 1L));
        assertEquals(ExceptionUtils.getObjectNotFoundException(EducationalEffect.class, getIds(nonExistingEducationalEffectsToDelete)).getMessage(), exception.getMessage());
    }
}
