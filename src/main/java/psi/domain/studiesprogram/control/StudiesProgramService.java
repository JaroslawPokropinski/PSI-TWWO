package psi.domain.studiesprogram.control;

import com.google.common.collect.Sets;
import io.github.perplexhub.rsql.RSQLJPASupport;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import psi.domain.auditedobject.entity.ObjectState;
import psi.domain.studiesprogram.boundary.StudiesProgramRSQLMapping;
import psi.domain.studiesprogram.entity.StudiesProgram;
import psi.infrastructure.exception.ExceptionUtils;
import psi.infrastructure.exception.IllegalArgumentAppException;

import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StudiesProgramService {

    private final StudiesProgramRepository studiesProgramRepository;

    public Page<StudiesProgram> searchStudiesProgramsByRSQL(String query, Pageable pageable){
        return studiesProgramRepository.findAll(RSQLJPASupport.toSpecification(query, StudiesProgramRSQLMapping.RSQL_TO_JPA_ATTRIBUTE_MAPPING), pageable);
    }

    public List<StudiesProgram> getAllStudiesPrograms(){
        return studiesProgramRepository.findAll();
    }

    public StudiesProgram getStudiesProgramById(Long id){
        return studiesProgramRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.getObjectNotFoundException(StudiesProgram.class, id));
    }

    public List<StudiesProgram> getStudiesProgramByIds(Collection<Long> ids){
        return studiesProgramRepository.findAllById(ids);
    }

    public List<StudiesProgram> createStudiesPrograms(Collection<StudiesProgram> studiesPrograms){
        prepareBeforeCreate(studiesPrograms);
        validateBeforeCreate(studiesPrograms);
        return studiesProgramRepository.saveAll(studiesPrograms);
    }

    public void updateStudiesPrograms(Collection<StudiesProgram> updatedStudiesPrograms, Long userId){
        List<StudiesProgram> foundStudiesPrograms = studiesProgramRepository.findAllById(getNonNullUniqueIds(updatedStudiesPrograms));
        validateBeforeUpdate(foundStudiesPrograms, updatedStudiesPrograms, userId);
        updatedStudiesPrograms(foundStudiesPrograms, updatedStudiesPrograms);
    }

    public void deleteStudiesPrograms(Collection<Long> ids, Long userId){
        List<StudiesProgram> foundStudiesPrograms = studiesProgramRepository.findAllById(ids);
        validateBeforeDelete(ids, foundStudiesPrograms, userId);
        foundStudiesPrograms.forEach(studiesProgram -> studiesProgram.setObjectState(ObjectState.REMOVED));
    }

    public Page<Revision<Integer, StudiesProgram>> getStudiesProgramHistory(Long id, Pageable pageable){
        return studiesProgramRepository.findRevisions(id, pageable);
    }

    private void prepareBeforeCreate(Collection<StudiesProgram> studiesPrograms) {
        studiesPrograms.forEach(this::prepareBeforeCreate);
    }

    private void prepareBeforeCreate(StudiesProgram studiesProgram) {
        studiesProgram.setId(null);
        studiesProgram.setObjectState(ObjectState.UNVERIFIED);
    }

    private void validateBeforeCreate(Collection<StudiesProgram> studiesPrograms){
        validateStudiesProgramsCodeUniqueness(studiesPrograms);
    }

    private void validateStudiesProgramsCodeUniqueness(Collection<StudiesProgram> studiesPrograms){
        //validateIfThereAreNoStudiesProgramsCodeDuplicatesAmongFieldOfStudy();??
        List<String> studiesProgramsCodes = getCodes(studiesPrograms);
        List<String> existingStudiesProgramsCodes = getCodes(studiesProgramRepository.findAll());
        if (studiesProgramsCodes.size() != new HashSet<>(studiesProgramsCodes).size() && checkIfStudiesProgramCodesNotDuplicates(studiesProgramsCodes, existingStudiesProgramsCodes)){
            throw new IllegalArgumentAppException("Studies Programs to create have non-unique studies program code!");
        }
    }

    private boolean checkIfStudiesProgramCodesNotDuplicates(List<String> firstList, List<String> secondList){
        boolean flag = true;
        for (String code : firstList){
            if ( secondList.contains(code) ){
                flag = false;
            }
        }
        return flag;
    }

    private List<String> getCodes(Collection<StudiesProgram> studiesPrograms){
        return studiesPrograms.stream()
                .map(StudiesProgram::getCode)
                .collect(Collectors.toList());
    }

    private void updatedStudiesPrograms(Collection<StudiesProgram> existingStudiesPrograms, Collection<StudiesProgram> updatedStudiesPrograms){
        Map<Long, StudiesProgram> updatedStudiesProgramsById = getStudiesProgramsById(updatedStudiesPrograms);
        existingStudiesPrograms.forEach( studiesProgram -> updateStudiesProgram(studiesProgram, updatedStudiesProgramsById.get(studiesProgram.getId())));
    }

    private void updateStudiesProgram(StudiesProgram existingStudiesProgram, StudiesProgram updatedStudiesProgram){
        existingStudiesProgram.setFieldOfStudy(updatedStudiesProgram.getFieldOfStudy());
        existingStudiesProgram.setStudiesLevel(updatedStudiesProgram.getStudiesLevel());
    }

    private void validateBeforeUpdate(Collection<StudiesProgram> existingStudiesPrograms, Collection<StudiesProgram> updatedStudiesPrograms, long userId ){

    }

    private Map<Long, StudiesProgram> getStudiesProgramsById(Collection<StudiesProgram> studiesPrograms) {
        return studiesPrograms.stream()
                .collect(Collectors.toMap(StudiesProgram::getId, Function.identity()));
    }

    private Set<Long> getNonNullUniqueIds(Collection<StudiesProgram> studiesPrograms) {
        return studiesPrograms.stream()
                .map(StudiesProgram::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private void validateBeforeDelete(Collection<Long> ids, Collection<StudiesProgram> studiesPrograms, Long userId) {
        validateIfAllStudiesProgramExists(ids, studiesPrograms);
        validatePermissions(studiesPrograms, userId);
    }

    private void validateIfAllStudiesProgramExists(Collection<Long> ids, Collection<StudiesProgram> foundStudiesPrograms){
        Set<Long> idsOfNonExistingStudiesProgram = getIdsOfNonExistingStudiesPrograms(ids, foundStudiesPrograms);
        if (!idsOfNonExistingStudiesProgram.isEmpty()){
            throw ExceptionUtils.getObjectNotFoundException(StudiesProgram.class, idsOfNonExistingStudiesProgram);
        }
    }

    private Set<Long> getIdsOfNonExistingStudiesPrograms(Collection<Long> ids, Collection<StudiesProgram> foundStudiesPrograms){
        return Sets.difference(new HashSet<>(ids), getNonNullUniqueIds(foundStudiesPrograms));
    }

    private void validatePermissions(Collection<StudiesProgram> studiesPrograms, Long userId) {
        List<Long> subjectCardsWithoutPermissions = getStudiesProgramsWithoutPermissions(studiesPrograms, userId);
        if (!subjectCardsWithoutPermissions.isEmpty()) {
            throw new IllegalArgumentAppException(MessageFormat.format("No permissions to perform operation for subject cards {0}", StringUtils.join(subjectCardsWithoutPermissions)));
        }
    }

    private List<Long> getStudiesProgramsWithoutPermissions(Collection<StudiesProgram> studiesPrograms, Long userId) {
        return Collections.emptyList();
    }

    public void changeStudiesProgramState(Collection<Long> ids, ObjectState newState, Long userId){
        List<StudiesProgram> foundStudiesProgram = getStudiesProgramByIds(ids);
        validateBeforeStateChange(ids, foundStudiesProgram, userId);
        foundStudiesProgram.forEach(studiesProgram -> studiesProgram.setObjectState(newState));
    }

    private void validateBeforeStateChange(Collection<Long> ids, Collection<StudiesProgram> studiesPrograms, Long userId){
        validateIfAllStudiesProgramExists(ids, studiesPrograms);
        validatePermissions(studiesPrograms, userId);
    }

}
