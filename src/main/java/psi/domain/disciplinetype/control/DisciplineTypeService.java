package psi.domain.disciplinetype.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import psi.domain.disciplinetype.entity.DisciplineType;
import psi.infrastructure.exception.IllegalArgumentAppException;

import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DisciplineTypeService {

    private final DisciplineTypeRepository disciplineTypeRepository;

    public DisciplineType getDisciplineTypeById(Long id){
        var disciplineType = disciplineTypeRepository.findById(id);
        if(disciplineType.isPresent()){
            return disciplineType.get();
        }
        else{
            throw new IllegalArgumentAppException("Discipline type Id not exist.");
        }
    }

    public List<DisciplineType> createDisciplineTypes(Collection<DisciplineType> disciplineTypes){
        prepareBeforeCreate(disciplineTypes);
        validateBeforeCreate(disciplineTypes);
        return disciplineTypeRepository.saveAll(disciplineTypes);
    }

    private void prepareBeforeCreate(Collection<DisciplineType> disciplineTypes){
        disciplineTypes.forEach(this::prepareBeforeCreate);
    }

    private void prepareBeforeCreate(DisciplineType disciplineType){
        disciplineType.setId(null);
    }

    private void validateBeforeCreate(Collection<DisciplineType> disciplineTypes){
        validateDisciplineTypeNameUniqueness(disciplineTypes);
    }

    private void validateDisciplineTypeNameUniqueness(Collection<DisciplineType> disciplineTypes){
        validateIfThereAreNoDisciplineTypeNamesDuplicatesAmongSubmittedDisciplineTypes(disciplineTypes);
        validateIfThereAreNoDisciplineTypeNamesDuplicatesAmongExistingDisciplineTypes(disciplineTypes);
    }

    private void validateIfThereAreNoDisciplineTypeNamesDuplicatesAmongSubmittedDisciplineTypes(Collection<DisciplineType> disciplineTypes){
        List<String> disciplineTypeNames = getNames(disciplineTypes);
        if(disciplineTypeNames.size() != new HashSet<>(disciplineTypeNames).size()){
            throw new IllegalArgumentAppException("");
        }
    }

    private void validateIfThereAreNoDisciplineTypeNamesDuplicatesAmongExistingDisciplineTypes(Collection<DisciplineType> disciplineTypes){
        List<DisciplineType> foundDuplicates = findDuplicatedNames(disciplineTypes);
        if (!foundDuplicates.isEmpty()){
            throw new IllegalArgumentAppException(MessageFormat.format("There are existing subject cards with given subject codes {0}", getNames(foundDuplicates)));
        }
    }

    private List<String> getNames(Collection<DisciplineType> disciplineTypes){
        return disciplineTypes.stream()
                .map(DisciplineType::getName)
                .collect(Collectors.toList());
    }

    private List<DisciplineType> findDuplicatedNames(Collection<DisciplineType> disciplineTypes){
        Set<Long> disciplineTypesIds = getNonNullUniqueIds(disciplineTypes);
        if (disciplineTypesIds.isEmpty()){
            return disciplineTypeRepository.findAllByNameIn(getNames(disciplineTypes));
        }
        return disciplineTypeRepository.findAllByNameInAndIdNotIn(getNames(disciplineTypes), disciplineTypesIds);
    }

    private Set<Long> getNonNullUniqueIds(Collection<DisciplineType> disciplineTypes){
        return disciplineTypes.stream()
                .map(DisciplineType::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

}
