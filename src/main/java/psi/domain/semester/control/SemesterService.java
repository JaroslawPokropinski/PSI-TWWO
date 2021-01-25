package psi.domain.semester.control;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import psi.api.semester.SemesterDTO;
import psi.domain.auditedobject.entity.ObjectState;
import psi.domain.semester.entity.Semester;
import psi.domain.studiesplan.entity.StudiesPlan;
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
public class SemesterService {

    private final SemesterRepository semesterRepository;

    public Semester getSemesterById(Long id){
        return semesterRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.getObjectNotFoundException(Semester.class, id));
    }

    public List<Semester> getSemestersByIds(Collection<Long> ids){
        return semesterRepository.findAllById(ids);
    }

    public List<Semester> getSemestersByStudiesPlanId(Long studiesPlanId){
        var studiesPlanIds = Arrays.asList(studiesPlanId);
        return semesterRepository.findAllByStudiesPlanIdIn(studiesPlanIds);
    }

    public List<Semester> createSemesters(Collection<Semester> semesters){
        prepareBeforeCreate(semesters);
        validateBeforeCreate(semesters);
        return semesterRepository.saveAll(semesters);
    }

    public void updateSemesters(Collection<Semester> updatedSemesters, Long userId){
        List<Semester> foundSemesters = semesterRepository.findAllById(getNonNullUniqueIds(updatedSemesters));
        validateBeforeUpdate(foundSemesters, updatedSemesters, userId);
        updateSemesters(foundSemesters, updatedSemesters);
    }

    public void deleteSemesters(Collection<Long> ids, Long userId){
        List<Semester> foundSemesters = semesterRepository.findAllById(ids);
        validateBeforeDelete(ids, foundSemesters, userId);
        semesterRepository.deleteAll(foundSemesters);
    }

    private void prepareBeforeCreate(Collection<Semester> semesters){
        semesters.forEach(this::prepareBeforeCreate);
    }

    private void prepareBeforeCreate(Semester semester){
        semester.setId(null);
        //semester.setObjectState(ObjectState.UNVERIFIED);
    }

    private void validateBeforeCreate(Collection<Semester> semesters){
        validateSemesterUniqueness(semesters);
    }

    private void validateSemesterUniqueness(Collection<Semester> semesters){
        validateIfThereAreNoSemesterNumberDuplicatesAmongStudiesPlan(semesters);
    }

    private void validateIfThereAreNoSemesterNumberDuplicatesAmongStudiesPlan(Collection<Semester> semesters){
        semesters.forEach(semester -> validateIfThereAreNoSemesterNumberDuplicatesAmongStudiesPlan(semester));
    }

    private void validateIfThereAreNoSemesterNumberDuplicatesAmongStudiesPlan(Semester semester){
        List<Long> studiesPlans = Arrays.asList(semester.getStudiesPlan().getId());
        List<Semester> foundSemestersForStudiesPlan = semesterRepository.findAllByStudiesPlanIdIn(studiesPlans);
        if (!foundSemestersForStudiesPlan.isEmpty()){
            foundSemestersForStudiesPlan.forEach(newSemester -> compareSemesterNumber(newSemester, semester));
        }
    }

    private void compareSemesterNumber(Semester semOne, Semester semTwo){
        if (semOne.getNumber() > semTwo.getStudiesPlan().getStudiesProgram().getNumberOfSemesters()){
            throw new IllegalArgumentAppException(MessageFormat.format("Semesters number is greater than planed for connected studies plan {0}", StringUtils.join(semOne)));
        }
        else if (!semOne.getId().equals(semTwo.getId()) && semOne.getNumber().equals(semTwo.getNumber())){
            throw new IllegalArgumentAppException(MessageFormat.format("Semesters numbers are duplicating in studies plan for semester {0}", StringUtils.join(semOne)));
        }
    }

    private void validatePermissions(Collection<Semester> semesters, Long userId){
        List<Long> semestersWithoutPermissions = getSemestersWithoutPermissions(semesters, userId);
        if (!semestersWithoutPermissions.isEmpty()){
            throw new IllegalArgumentAppException(MessageFormat.format("No permissions to perform operation for semesters {0}", StringUtils.join(semestersWithoutPermissions)));
        }
    }

    private void validateBeforeUpdate(Collection<Semester> existingSemesters, Collection<Semester> updatedSemesters, Long userId){
        validateIfAllSemestersHaveUniqueId(updatedSemesters);
        validateIfAllSemestersExists(getNonNullUniqueIds(updatedSemesters), existingSemesters);
        validateSemesterUniqueness(updatedSemesters);
        validatePermissions(existingSemesters, userId);
    }

    private void validateIfAllSemestersHaveUniqueId(Collection<Semester> semesters){
        if (getNonNullUniqueIds(semesters).size() != semesters.size()){
            throw new IllegalArgumentAppException("There are some semesters without id or ids are not unique!");
        }
    }

    private void validateIfAllSemestersExists(Collection<Long> ids, Collection<Semester> foundSemesters){
        Set<Long> idsOfNonExistingSemesters = getIdsOfNonExistingSemesters(ids, foundSemesters);
        if (!idsOfNonExistingSemesters.isEmpty()){
            throw ExceptionUtils.getObjectNotFoundException(Semester.class, idsOfNonExistingSemesters);
        }
    }

    private void updateSemesters(Collection<Semester> existingSemesters, Collection<Semester> updatedSemesters){
        Map<Long, Semester> updatedSemestersById = getSemestersById(updatedSemesters);
        existingSemesters.forEach(semester -> updateSemester(semester, updatedSemestersById.get(semester.getId())));
    }

    private Map<Long, Semester> getSemestersById(Collection<Semester> semesters){
        return semesters.stream()
                .collect(Collectors.toMap(Semester::getId, Function.identity()));
    }

    private void updateSemester(Semester existingSemester, Semester updatedSemester){
        existingSemester.setNumber(updatedSemester.getNumber());
        existingSemester.setAllowedEctsDeficit(updatedSemester.getAllowedEctsDeficit());
        existingSemester.setStudiesPlan(updatedSemester.getStudiesPlan());
        existingSemester.setSubjectCards(updatedSemester.getSubjectCards());
    }

    private List<Long> getSemestersWithoutPermissions(Collection<Semester> semesters, Long userId){
        return Collections.emptyList();
    }

    private Set<Long> getIdsOfNonExistingSemesters(Collection<Long> ids, Collection<Semester> foundSemesters){
        return Sets.difference(new HashSet<>(ids), getNonNullUniqueIds(foundSemesters));
    }

    private Set<Long> getNonNullUniqueIds(Collection<Semester> semesters){
        return semesters.stream()
                .map(Semester::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private void validateBeforeDelete(Collection<Long> ids, Collection<Semester> semesters, Long userId ){
        validateIfAllSemestersExists(ids, semesters);
        validatePermissions(semesters, userId);
    }


}
