package psi.domain.studiesplan.control;

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
import psi.domain.semester.entity.Semester;
import psi.domain.studiesplan.boundary.StudiesPlanRSQLMapping;
import psi.domain.studiesplan.entity.StudiesPlan;
import psi.domain.subjectcard.entity.SubjectCard;
import psi.infrastructure.exception.ExceptionUtils;
import psi.infrastructure.exception.IllegalArgumentAppException;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StudiesPlanService {

    private final StudiesPlanRepository studiesPlanRepository;

    public Page<StudiesPlan> searchStudiesPlansByRSQL(String query, Pageable pageable){
        return studiesPlanRepository.findAll(RSQLJPASupport.toSpecification(query, StudiesPlanRSQLMapping.RSQL_TO_JPA_ATTRIBUTE_MAPPING), pageable);
    }

    public List<StudiesPlan> getAllStudiesPlans(){
        return studiesPlanRepository.findAll();
    }

    public List<StudiesPlan> getStudiesPlansByIds(Collection<Long> ids){
        return studiesPlanRepository.findAllById(ids);
    }

    public StudiesPlan getStudiesPlan(Long id){
        var studiesPlanO = studiesPlanRepository.findById(id);
        if ( studiesPlanO.isPresent())
            return studiesPlanO.get();
        else {
            throw ExceptionUtils.getObjectNotFoundException(Semester.class, id);
        }
    }

    public List<StudiesPlan> createStudiesPlans(Collection<StudiesPlan> studiesPlans){
        prepareBeforeCreate(studiesPlans);
        validateBeforeCreate(studiesPlans);
        return studiesPlanRepository.saveAll(studiesPlans);
    }

    public void updateStudiesPlans(Collection<StudiesPlan> updatedStudiesPlans, Long userId){
        List<StudiesPlan> foundStudiesPlans = studiesPlanRepository.findAllById(getNonNullUniqueIds(updatedStudiesPlans));
        validateBeforeUpdate(foundStudiesPlans, updatedStudiesPlans, userId);
        updateStudiesPlans(foundStudiesPlans, updatedStudiesPlans);
    }

    private void prepareBeforeCreate(Collection<StudiesPlan> studiesPlans){
        studiesPlans.forEach(this::prepareBeforeCreate);
    }

    private void prepareBeforeCreate(StudiesPlan studiesPlan){
        studiesPlan.setId(null);
        studiesPlan.setObjectState(ObjectState.UNVERIFIED);
    }

    private void validateBeforeCreate(Collection<StudiesPlan> studiesPlan){
        validateStudiesPlanCodeUniqueness(studiesPlan);
    }

    private void validateStudiesPlanCodeUniqueness(Collection<StudiesPlan> studiesPlans){
        validateIfThereAreNoStudiesPlansCodeDuplicatesAmongSubmittedStudiesPrograms(studiesPlans);
        validateIfThereAreNoStudiesPlansCodeDuplicatesAmongExistingStudiesPlan(studiesPlans);
    }

    private void validateIfThereAreNoStudiesPlansCodeDuplicatesAmongSubmittedStudiesPrograms(Collection<StudiesPlan> studiesPlans){
        List<String> studiesPlansCodes = getCodes(studiesPlans);
        if (studiesPlans.size() != new HashSet<>(studiesPlansCodes).size()){
            throw new IllegalArgumentAppException("Studies plans to create have non-unique studies programs codes!");
        }
    }

    private List<String> getCodes(Collection<StudiesPlan> studiesPlans){
        return studiesPlans.stream()
                .map(StudiesPlan::getCode)
                .collect(Collectors.toList());
    }

    private void validateIfThereAreNoStudiesPlansCodeDuplicatesAmongExistingStudiesPlan(Collection<StudiesPlan> studiesPlans){
        List<StudiesPlan> foundDuplicates = findDuplicatedCodes(studiesPlans);
        if(!foundDuplicates.isEmpty()){
            throw new IllegalArgumentAppException(MessageFormat.format("There are existing studies plan with given studies plan {0}", getCodes(foundDuplicates)));
        }
    }

    private List<StudiesPlan> findDuplicatedCodes(Collection<StudiesPlan> studiesPlans){
        Set<Long> studiesPlansIds = getNonNullUniqueIds(studiesPlans);
        if(studiesPlansIds.isEmpty()){
            return studiesPlanRepository.findAllByCodeIn(getCodes(studiesPlans));
        }
        return studiesPlanRepository.findAllByCodeInAndIdNotIn(getCodes(studiesPlans), studiesPlansIds);
    }

    private Set<Long> getNonNullUniqueIds(Collection<StudiesPlan> studiesPlans){
        return studiesPlans.stream()
                .map(StudiesPlan::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private void validateBeforeUpdate(Collection<StudiesPlan> existingStudiesPlans, Collection<StudiesPlan> updatedStudiesPlan, Long userId){
        validateIfAllStudiesPlansHaveUniqueId(updatedStudiesPlan);
        validateIfAllStudiesPlansExists(getNonNullUniqueIds(updatedStudiesPlan), existingStudiesPlans);
        validatePermissions(existingStudiesPlans, userId);
    }

    private void validateIfAllStudiesPlansHaveUniqueId(Collection<StudiesPlan> studiesPlans){
        if (getNonNullUniqueIds(studiesPlans).size() != studiesPlans.size()){
            throw new IllegalArgumentAppException("There are some studies plans without id or ids are not unique!");
        }
    }

    private void validateIfAllStudiesPlansExists(Collection<Long> ids, Collection<StudiesPlan> foundStudiesPlans){
        Set<Long> idsOfNonExistingSubjectCards = getIdsOfNonExistingStudiesPlans(ids, foundStudiesPlans);
        if (!idsOfNonExistingSubjectCards.isEmpty()){
            throw ExceptionUtils.getObjectNotFoundException(SubjectCard.class, idsOfNonExistingSubjectCards);
        }
    }

    private Set<Long> getIdsOfNonExistingStudiesPlans(Collection<Long> ids, Collection<StudiesPlan> foundStudiesPlans){
        return Sets.difference(new HashSet<>(ids), getNonNullUniqueIds(foundStudiesPlans));
    }

    private void validatePermissions(Collection<StudiesPlan> studiesPlans, Long userId){
        List<Long> studiesPlansWithoutPermissions = getStudiesPlansWithoutPermissions(studiesPlans, userId);
        if (!studiesPlansWithoutPermissions.isEmpty()){
            throw new IllegalArgumentAppException(MessageFormat.format("No permissions to perform operation for studies plans {0}", StringUtils.join(studiesPlansWithoutPermissions)));
        }
    }

    private List<Long> getStudiesPlansWithoutPermissions(Collection<StudiesPlan> studiesPlans, Long userId){
        return Collections.emptyList();
    }

    private void updateStudiesPlans(Collection<StudiesPlan> existingStudiesPlans, Collection<StudiesPlan> updatedStudiesPlans){
        Map<Long, StudiesPlan> updatedSubjectCardsById = getStudiesPlansById(updatedStudiesPlans);
        existingStudiesPlans.forEach(studiesPlan -> updateStudiesPlans(studiesPlan, updatedSubjectCardsById.get(studiesPlan.getId())));
    }

    private Map<Long, StudiesPlan> getStudiesPlansById(Collection<StudiesPlan> studiesPlans){
        return studiesPlans.stream()
                .collect(Collectors.toMap(StudiesPlan::getId, Function.identity()));
    }

    private void updateStudiesPlans(StudiesPlan existingStudiesPlan, StudiesPlan updatedStudiesPlan){
        existingStudiesPlan.setDecreeDate(updatedStudiesPlan.getDecreeDate());
        existingStudiesPlan.setInEffectSince(updatedStudiesPlan.getInEffectSince());
        existingStudiesPlan.setStudiesProgram(updatedStudiesPlan.getStudiesProgram());
        existingStudiesPlan.setCode(updatedStudiesPlan.getCode());
    }

    public void deleteStudiesPlans(Collection<Long> ids, Long userId){
        List<StudiesPlan> foundStudiesPlans = studiesPlanRepository.findAllById(ids);
        validateBeforeDelete(ids, foundStudiesPlans, userId);
        foundStudiesPlans.forEach(studiesPlan -> studiesPlan.setObjectState(ObjectState.REMOVED));
    }

    private void validateBeforeDelete(Collection<Long> ids, Collection<StudiesPlan> studiesPlans, Long userId){
        validateIfAllStudiesPlansExists(ids, studiesPlans);
        validatePermissions(studiesPlans, userId);
    }

    public Page<Revision<Integer, StudiesPlan>> getStudiesPlanHistory(Long id, Pageable pageable){
        return studiesPlanRepository.findRevisions(id, pageable);
    }

    public void changeStudiesPlanState(Collection<Long> ids, ObjectState newState, Long userId){
        List<StudiesPlan> foundStudiesPlans = getStudiesPlansByIds(ids);
        validateBeforeStateChange(ids, foundStudiesPlans, userId);
        foundStudiesPlans.forEach(studiesPlan -> studiesPlan.setObjectState(newState));
    }

    private void validateBeforeStateChange(Collection<Long> ids, Collection<StudiesPlan> studiesPlans, Long userId){
        validateIfAllStudiesPlansExists(ids, studiesPlans);
        validatePermissions(studiesPlans, userId);
    }

}
