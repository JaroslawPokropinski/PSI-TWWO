package psi.domain.educationaleffect.control;

import com.google.common.collect.Sets;
import io.github.perplexhub.rsql.RSQLJPASupport;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psi.domain.auditedobject.entity.ObjectState;
import psi.domain.educationaleffect.entity.EducationalEffect;
import psi.infrastructure.exception.ExceptionUtils;
import psi.infrastructure.exception.IllegalArgumentAppException;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static psi.domain.educationaleffect.boundary.EducationalEffectRSQLMapping.RSQL_TO_JPA_ATTRIBUTE_MAPPING;

@Service
@Transactional
@RequiredArgsConstructor
public class EducationalEffectService {

    private final EducationalEffectRepository educationalEffectRepository;

    public Page<EducationalEffect> getEducationalEffectsByRSQL(String query, Pageable pagination) {
        return educationalEffectRepository.findAll(RSQLJPASupport.toSpecification(query, RSQL_TO_JPA_ATTRIBUTE_MAPPING), pagination);
    }

    public List<EducationalEffect> getEducationalEffectsByIds(Collection<Long> ids) {
        return educationalEffectRepository.findAllById(ids);
    }

    public List<EducationalEffect> createEducationalEffects(Collection<EducationalEffect> educationalEffects) {
        prepareBeforeCreate(educationalEffects);
        validateBeforeCreate(educationalEffects);
        return educationalEffectRepository.saveAll(educationalEffects);
    }

    private void prepareBeforeCreate(Collection<EducationalEffect> educationalEffects) {
        educationalEffects.forEach(this::prepareBeforeCreate);
    }

    private void prepareBeforeCreate(EducationalEffect educationalEffect) {
        educationalEffect.setId(null);
        educationalEffect.setObjectState(ObjectState.ACTIVE);
    }

    private void validateBeforeCreate(Collection<EducationalEffect> educationalEffects) {
        validateCodeUniqueness(educationalEffects);
    }

    private void validateCodeUniqueness(Collection<EducationalEffect> educationalEffects) {
        validateIfThereAreNoCodeDuplicatesAmongSubmittedEducationalEffects(educationalEffects);
        validateIfThereAreNoCodeDuplicatesAmongExistingEducationalEffects(educationalEffects);
    }

    private void validateIfThereAreNoCodeDuplicatesAmongSubmittedEducationalEffects(Collection<EducationalEffect> educationalEffects) {
        List<String> effectCodes = getCodes(educationalEffects);
        if (effectCodes.size() != new HashSet<>(effectCodes).size()) {
            throw new IllegalArgumentAppException("Educational effects have non-unique codes!");
        }
    }

    private List<String> getCodes(Collection<EducationalEffect> educationalEffects) {
        return educationalEffects.stream()
                .map(EducationalEffect::getCode)
                .collect(Collectors.toList());
    }

    private void validateIfThereAreNoCodeDuplicatesAmongExistingEducationalEffects(Collection<EducationalEffect> educationalEffects) {
        List<EducationalEffect> foundDuplicates = findDuplicatedCodes(educationalEffects);
        if (!foundDuplicates.isEmpty()) {
            throw new IllegalArgumentAppException(MessageFormat.format("There are existing educational effects with given codes {0}", getCodes(foundDuplicates)));
        }
    }

    private List<EducationalEffect> findDuplicatedCodes(Collection<EducationalEffect> educationalEffects) {
        Set<Long> educationalEffectIds = getNonNullUniqueIds(educationalEffects);
        if (educationalEffectIds.isEmpty()) {
            return educationalEffectRepository.findAllByCodeIn(getCodes(educationalEffects));
        }
        return educationalEffectRepository.findAllByCodeInAndIdNotIn(getCodes(educationalEffects), educationalEffectIds);
    }

    private Set<Long> getNonNullUniqueIds(Collection<EducationalEffect> educationalEffects) {
        return educationalEffects.stream()
                .map(EducationalEffect::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public void updateEducationalEffects(Collection<EducationalEffect> updatedEducationalEffects, Long userId) {
        List<EducationalEffect> foundEducationalEffects = educationalEffectRepository.findAllById(getNonNullUniqueIds(updatedEducationalEffects));
        validateBeforeUpdate(foundEducationalEffects, updatedEducationalEffects, userId);
        updateEducationalEffects(foundEducationalEffects, updatedEducationalEffects);
    }

    private void validateBeforeUpdate(Collection<EducationalEffect> existingEducationalEffects, Collection<EducationalEffect> updatedEffects, Long userId) {
        validateIfAllEducationalEffectsHaveUniqueId(updatedEffects);
        validateIfAllEducationalEffectsExists(getNonNullUniqueIds(updatedEffects), existingEducationalEffects);
        validatePermissions(existingEducationalEffects, userId);
    }

    private void validateIfAllEducationalEffectsHaveUniqueId(Collection<EducationalEffect> educationalEffects) {
        if (getNonNullUniqueIds(educationalEffects).size() != educationalEffects.size()) {
            throw new IllegalArgumentAppException("There are some educational effects without id or ids are not unique!");
        }
    }

    private void validateIfAllEducationalEffectsExists(Collection<Long> ids, Collection<EducationalEffect> foundEducationalEffects) {
        Set<Long> idsOfNonExistingEducationalEffects = getIdsOfNonExistingEducationalEffects(ids, foundEducationalEffects);
        if (!idsOfNonExistingEducationalEffects.isEmpty()) {
            throw ExceptionUtils.getObjectNotFoundException(EducationalEffect.class, idsOfNonExistingEducationalEffects);
        }
    }

    private Set<Long> getIdsOfNonExistingEducationalEffects(Collection<Long> ids, Collection<EducationalEffect> foundEducationalEffects) {
        return Sets.difference(new HashSet<>(ids), getNonNullUniqueIds(foundEducationalEffects));
    }

    private void validatePermissions(Collection<EducationalEffect> educationalEffects, Long userId) {
        List<Long> effectsWithoutPermission = getEducationalEffectsWithoutPermissions(educationalEffects, userId);
        if (!effectsWithoutPermission.isEmpty()) {
            throw new IllegalArgumentAppException(MessageFormat.format("No permissions to perform operation for educational effects {0}", StringUtils.join(effectsWithoutPermission)));
        }
    }

    private List<Long> getEducationalEffectsWithoutPermissions(Collection<EducationalEffect> educationalEffects, Long userId) {
        return Collections.emptyList();
    }

    private void updateEducationalEffects(Collection<EducationalEffect> existingEducationalEffects, Collection<EducationalEffect> updatedEducationalEffects) {
        Map<Long, EducationalEffect> updatedEducationalEffectById = getEducationalEffectsById(updatedEducationalEffects);
        existingEducationalEffects.forEach(existingEffect -> updateEducationalEffect(existingEffect, updatedEducationalEffectById.get(existingEffect.getId())));
    }

    private Map<Long, EducationalEffect> getEducationalEffectsById(Collection<EducationalEffect> educationalEffects) {
        return educationalEffects.stream()
                .collect(Collectors.toMap(EducationalEffect::getId, Function.identity()));
    }

    private void updateEducationalEffect(EducationalEffect existingEducationalEffect, EducationalEffect updatedEducationalEffect) {
        existingEducationalEffect.setType(updatedEducationalEffect.getType());
        existingEducationalEffect.setPrkLevel(updatedEducationalEffect.getPrkLevel());
        existingEducationalEffect.setIsEngineerEffect(updatedEducationalEffect.getIsEngineerEffect());
        existingEducationalEffect.setIsLingualEffect(updatedEducationalEffect.getIsLingualEffect());
        existingEducationalEffect.setCategory(updatedEducationalEffect.getCategory());
        existingEducationalEffect.setDescription(updatedEducationalEffect.getDescription());
    }

    public void deleteEducationalEffects(Collection<Long> ids, Long userId) {
        List<EducationalEffect> foundEducationalEffects = educationalEffectRepository.findAllById(ids);
        validateBeforeDelete(ids, foundEducationalEffects, userId);
        foundEducationalEffects.forEach(educationalEffect -> educationalEffect.setObjectState(ObjectState.REMOVED));
    }

    private void validateBeforeDelete(Collection<Long> ids, Collection<EducationalEffect> educationalEffects, Long userId) {
        validateIfAllEducationalEffectsExists(ids, educationalEffects);
        validatePermissions(educationalEffects, userId);
    }

    public Page<Revision<Integer, EducationalEffect>> getEducationalEffectHistory(Long id, Pageable pageable) {
        return educationalEffectRepository.findRevisions(id, pageable);
    }

    public void changeEducationalEffect(Collection<Long> ids, ObjectState newState, Long userId){
        List<EducationalEffect> foundEducationalEffects = getEducationalEffectsByIds(ids);
        validateBeforeStateChange(ids, foundEducationalEffects, userId);
        foundEducationalEffects.forEach(educationalEffect -> educationalEffect.setObjectState(newState));
    }

    private void validateBeforeStateChange(Collection<Long> ids, Collection<EducationalEffect> educationalEffects, Long userId){
        validateIfAllEducationalEffectsExists(ids, educationalEffects);
        validatePermissions(educationalEffects, userId);
    }

}
