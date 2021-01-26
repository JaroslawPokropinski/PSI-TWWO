package psi.domain.discipline.control;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psi.domain.auditedobject.entity.ObjectState;
import psi.domain.discipline.entity.Discipline;
import psi.domain.disciplinetype.control.DisciplineTypeRepository;
import psi.domain.disciplinetype.entity.DisciplineType;
import psi.infrastructure.exception.ExceptionUtils;
import psi.infrastructure.exception.IllegalArgumentAppException;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;
    private final DisciplineTypeRepository disciplineTypeRepository;

    public List<Discipline> getAllDisciplines() {
        return disciplineRepository.findAll();
    }

    public List<Discipline> getDisciplinesByIds(Collection<Long> ids) {
        return disciplineRepository.findAllById(ids);
    }

    public Discipline getDisciplineById(Long id) {
        return disciplineRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.getObjectNotFoundException(Discipline.class, id));
    }

    public List<Discipline> createDisciplines(Collection<Discipline> disciplines){
        prepareBeforeCreate(disciplines);
        validateBeforeCreate(disciplines);
        return disciplineRepository.saveAll(disciplines);
    }

    private void prepareBeforeCreate(Collection<Discipline> disciplines) {
        disciplines.forEach(this::prepareBeforeCreate);
    }

    private void validateBeforeCreate(Collection<Discipline> disciplines) {
        validateDisciplinesNameUniqueness(disciplines);
        ensureDisciplineTypesExist(disciplines);
    }

    private void validateDisciplinesNameUniqueness(Collection<Discipline> disciplines) {
        validateIfThereAreNoDisciplineNameDuplicatesAmongSubmittedDiscipline(disciplines);
        validateIfThereAreNoDisciplineNameDuplicatesAmongExistingDiscipline(disciplines);
    }

    private void validateIfThereAreNoDisciplineNameDuplicatesAmongSubmittedDiscipline(Collection<Discipline> disciplines) {
        List<String> disciplinesNames = getNames(disciplines);
        if(disciplinesNames.size() != new HashSet<>(disciplinesNames).size()){
            throw new IllegalArgumentAppException("Disciplines to create have non-unique names!");
        }
    }

    private void validateIfThereAreNoDisciplineNameDuplicatesAmongExistingDiscipline(Collection<Discipline> disciplines) {
        List<Discipline> foundDuplicates = findDuplicatedNames(disciplines);
        if(!foundDuplicates.isEmpty()){
            throw new IllegalArgumentAppException(MessageFormat.format("There are existing disciplines with given codes {0}", getNames(foundDuplicates)));
        }
    }

    private List<Discipline> findDuplicatedNames(Collection<Discipline> disciplines){
        Set<Long> disciplinesIds = getNonNullUniqueIds(disciplines);
        if (disciplinesIds.isEmpty()){
            return disciplineRepository.findAllByNameIn(getNames(disciplines));
        }
        return disciplineRepository.findAllByNameInAndIdNotIn(getNames(disciplines), disciplinesIds);
    }

    private List<String> getNames(Collection<Discipline> disciplines){
        return disciplines.stream()
                .map(Discipline::getName)
                .collect(Collectors.toList());
    }

    private Set<Long> getNonNullUniqueIds(Collection<Discipline> disciplines){
        return disciplines.stream()
                .map(Discipline::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private void prepareBeforeCreate(Discipline discipline){
        discipline.setId(null);
    }

    private void ensureDisciplineTypesExist(Collection<Discipline> disciplines){
        disciplines.forEach(this::ensureDisciplineTypeExist);
    }

    private void ensureDisciplineTypeExist(Discipline discipline){
        Optional<DisciplineType> foundDisciplineType = disciplineTypeRepository.findByName(discipline.getDisciplineType().getName());
        if (foundDisciplineType.isEmpty()){
            disciplineTypeRepository.save(discipline.getDisciplineType());
        }
        else{
            discipline.getDisciplineType().setId(foundDisciplineType.get().getId());
        }
    }

}
