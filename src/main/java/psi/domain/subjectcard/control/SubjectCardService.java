package psi.domain.subjectcard.control;

import com.google.common.collect.Sets;
import io.github.perplexhub.rsql.RSQLJPASupport;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psi.domain.auditedobject.entity.ObjectState;
import psi.domain.subjectcard.entity.SubjectCard;
import psi.domain.subjectcard.boundary.SubjectCardRSQLMapping;
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

@Service
@Transactional
@RequiredArgsConstructor
public class SubjectCardService {

    private final SubjectCardRepository subjectCardRepository;

    public Page<SubjectCard> searchSubjectCardsByRSQL(String query, Pageable pageable) {
        return subjectCardRepository.findAll(RSQLJPASupport.toSpecification(query, SubjectCardRSQLMapping.RSQL_TO_JPA_ATTRIBUTE_MAPPING), pageable);
    }

    public List<SubjectCard> getSubjectCardsByIds(Collection<Long> ids) {
        return subjectCardRepository.findAllById(ids);
    }

    public List<SubjectCard> createSubjectCards(Collection<SubjectCard> subjectCards) {
        prepareBeforeCreate(subjectCards);
        validateBeforeCreate(subjectCards);
        return subjectCardRepository.saveAll(subjectCards);
    }

    private void prepareBeforeCreate(Collection<SubjectCard> subjectCards) {
        subjectCards.forEach(this::prepareBeforeCreate);
    }

    private void prepareBeforeCreate(SubjectCard subjectCard) {
        subjectCard.setId(null);
        subjectCard.setObjectState(ObjectState.UNVERIFIED);
    }

    private void validateBeforeCreate(Collection<SubjectCard> subjectCards) {
        validateSubjectCodeUniqueness(subjectCards);
    }

    private void validateSubjectCodeUniqueness(Collection<SubjectCard> subjectCards) {
        validateIfThereAreNoSubjectCodeDuplicatesAmongSubmittedEducationalEffects(subjectCards);
        validateIfThereAreNoSubjectCodeDuplicatesAmongExistingEducationalEffects(subjectCards);
    }

    private void validateIfThereAreNoSubjectCodeDuplicatesAmongSubmittedEducationalEffects(Collection<SubjectCard> subjectCards) {
        List<String> subjectCodes = getCodes(subjectCards);
        if (subjectCodes.size() != new HashSet<>(subjectCodes).size()) {
            throw new IllegalArgumentAppException("Subject cards to create have non-unique subject codes!");
        }
    }

    private List<String> getCodes(Collection<SubjectCard> subjectCards) {
        return subjectCards.stream()
                .map(SubjectCard::getSubjectCode)
                .collect(Collectors.toList());
    }

    private void validateIfThereAreNoSubjectCodeDuplicatesAmongExistingEducationalEffects(Collection<SubjectCard> subjectCards) {
        List<SubjectCard> foundDuplicates = findDuplicatedCodes(subjectCards);
        if (!foundDuplicates.isEmpty()) {
            throw new IllegalArgumentAppException(MessageFormat.format("There are existing subject cards with given subject codes {0}", getCodes(foundDuplicates)));
        }
    }

    private List<SubjectCard> findDuplicatedCodes(Collection<SubjectCard> subjectCards) {
        Set<Long> subjectCardIds = getNonNullUniqueIds(subjectCards);
        if (subjectCardIds.isEmpty()) {
            return subjectCardRepository.findAllBySubjectCodeIn(getCodes(subjectCards));
        }
        return subjectCardRepository.findAllBySubjectCodeInAndIdNotIn(getCodes(subjectCards), subjectCardIds);
    }

    private Set<Long> getNonNullUniqueIds(Collection<SubjectCard> subjectCards) {
        return subjectCards.stream()
                .map(SubjectCard::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public void updateSubjectCards(Collection<SubjectCard> updatedSubjectCards, Long userId) {
        List<SubjectCard> foundSubjectCards = subjectCardRepository.findAllById(getNonNullUniqueIds(updatedSubjectCards));
        validateBeforeUpdate(foundSubjectCards, updatedSubjectCards, userId);
        updateSubjectCards(foundSubjectCards, updatedSubjectCards);
    }

    private void validateBeforeUpdate(Collection<SubjectCard> existingSubjectCards, Collection<SubjectCard> updatedSubjectCards, Long userId) {
        validateIfAllSubjectCardsHaveUniqueId(updatedSubjectCards);
        validateIfAllSubjectCardsExists(getNonNullUniqueIds(updatedSubjectCards), existingSubjectCards);
        validatePermissions(existingSubjectCards, userId);
    }

    private void validateIfAllSubjectCardsHaveUniqueId(Collection<SubjectCard> subjectCards) {
        if (getNonNullUniqueIds(subjectCards).size() != subjectCards.size()) {
            throw new IllegalArgumentAppException("There are some subject cards without id or ids are not unique!");
        }
    }

    private void validateIfAllSubjectCardsExists(Collection<Long> ids, Collection<SubjectCard> foundSubjectCards) {
        Set<Long> idsOfNonExistingSubjectCards = getIdsOfNonExistingSubjectCards(ids, foundSubjectCards);
        if (!idsOfNonExistingSubjectCards.isEmpty()) {
            throw ExceptionUtils.getObjectNotFoundException(SubjectCard.class, idsOfNonExistingSubjectCards);
        }
    }

    private Set<Long> getIdsOfNonExistingSubjectCards(Collection<Long> ids, Collection<SubjectCard> foundSubjectCards) {
        return Sets.difference(new HashSet<>(ids), getNonNullUniqueIds(foundSubjectCards));
    }

    private void validatePermissions(Collection<SubjectCard> subjectCards, Long userId) {
        List<Long> subjectCardsWithoutPermissions = getSubjectCardsWithoutPermissions(subjectCards, userId);
        if (!subjectCardsWithoutPermissions.isEmpty()) {
            throw new IllegalArgumentAppException(MessageFormat.format("No permissions to perform operation for subject cards {0}", StringUtils.join(subjectCardsWithoutPermissions)));
        }
    }

    private List<Long> getSubjectCardsWithoutPermissions(Collection<SubjectCard> subjectCards, Long userId) {
        return Collections.emptyList();
    }

    private void updateSubjectCards(Collection<SubjectCard> existingSubjectCards, Collection<SubjectCard> updatedSubjectCards) {
        Map<Long, SubjectCard> updatedSubjectCardsById = getSubjectCardsById(updatedSubjectCards);
        existingSubjectCards.forEach(subjectCard -> updateSubjectCard(subjectCard, updatedSubjectCardsById.get(subjectCard.getId())));
    }

    private Map<Long, SubjectCard> getSubjectCardsById(Collection<SubjectCard> subjectCards) {
        return subjectCards.stream()
                .collect(Collectors.toMap(SubjectCard::getId, Function.identity()));
    }

    private void updateSubjectCard(SubjectCard existingSubjectCard, SubjectCard updatedSubjectCard) {
        existingSubjectCard.setSubjectName(updatedSubjectCard.getSubjectName());
        existingSubjectCard.setSubjectNameInEnglish(updatedSubjectCard.getSubjectNameInEnglish());
        existingSubjectCard.setMainFieldOfStudy(updatedSubjectCard.getMainFieldOfStudy());
        existingSubjectCard.setSpecialization(updatedSubjectCard.getSpecialization());
        existingSubjectCard.setStudiesLevel(updatedSubjectCard.getStudiesLevel());
        existingSubjectCard.setStudiesForm(updatedSubjectCard.getStudiesForm());
        existingSubjectCard.setSubjectType(updatedSubjectCard.getSubjectType());
        existingSubjectCard.setIsGroupOfCourses(updatedSubjectCard.getIsGroupOfCourses());
        existingSubjectCard.setOrganisationalUnit(updatedSubjectCard.getOrganisationalUnit());
        existingSubjectCard.setPrerequisites(updatedSubjectCard.getPrerequisites());
        existingSubjectCard.setSubjectObjectives(updatedSubjectCard.getSubjectObjectives());
        existingSubjectCard.setLiterature(updatedSubjectCard.getLiterature());
        existingSubjectCard.setUsedTeachingTools(updatedSubjectCard.getUsedTeachingTools());
        existingSubjectCard.setSupervisor(updatedSubjectCard.getSupervisor());
        existingSubjectCard.setSubjectClasses(updatedSubjectCard.getSubjectClasses());
        existingSubjectCard.setEducationalEffects(updatedSubjectCard.getEducationalEffects());
    }

    public void deleteSubjectCards(Collection<Long> ids, Long userId) {
        List<SubjectCard> foundSubjectCards = subjectCardRepository.findAllById(ids);
        validateBeforeDelete(ids, foundSubjectCards, userId);
        foundSubjectCards.forEach(subjectCard -> subjectCard.setObjectState(ObjectState.REMOVED));
    }

    private void validateBeforeDelete(Collection<Long> ids, Collection<SubjectCard> subjectCards, Long userId) {
        validateIfAllSubjectCardsExists(ids, subjectCards);
        validatePermissions(subjectCards, userId);
    }

}
