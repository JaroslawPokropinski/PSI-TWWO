package psi.domain.fieldofstudy.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psi.domain.fieldofstudy.entity.FieldOfStudy;
import psi.infrastructure.exception.ExceptionUtils;
import psi.infrastructure.exception.IllegalArgumentAppException;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FieldOfStudyService {

    private final FieldOfStudyRepository fieldOfStudyRepository;

    public List<FieldOfStudy> getAllFiledOfStudies() {
        return fieldOfStudyRepository.findAll();
    }

    public FieldOfStudy getFieldOfStudyById(Long id) {
        return fieldOfStudyRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.getObjectNotFoundException(FieldOfStudy.class, id));
    }

    public List<FieldOfStudy> getFieldOfStudiesByIds(Collection<Long> ids) {
        return fieldOfStudyRepository.findAllById(ids);
    }

    public List<FieldOfStudy> createFieldOfStudies(Collection<FieldOfStudy> fieldOfStudies) {
        prepareBeforeCreate(fieldOfStudies);
        validateBeforeCreate(fieldOfStudies);
        return fieldOfStudyRepository.saveAll(fieldOfStudies);
    }

    private void prepareBeforeCreate(Collection<FieldOfStudy> fieldOfStudies) {
        fieldOfStudies.forEach(this::prepareBeforeCreate);
    }

    private void prepareBeforeCreate(FieldOfStudy fieldOfStudy) {
        fieldOfStudy.setId(null);
    }

    private void validateBeforeCreate(Collection<FieldOfStudy> fieldOfStudies) {
        validateNameUniquenessWithingOrganisationalUnit(fieldOfStudies);
    }

    private void validateNameUniquenessWithingOrganisationalUnit(Collection<FieldOfStudy> fieldOfStudies) {
        validateIfThereAreNoNameDuplicatesAmongSubmittedFieldOfStudies(fieldOfStudies);
        validateIfThereAreNoNameDuplicatesAmongExistingFieldOfStudies(fieldOfStudies);
    }

    private void validateIfThereAreNoNameDuplicatesAmongSubmittedFieldOfStudies(Collection<FieldOfStudy> fieldOfStudies) {
        Map<Long, List<String>> namesByOrganisationalUnit = getNamesByOrganisationalUnit(fieldOfStudies);
        if (areThereNameDuplicatesWithinOrganisationalUnit(namesByOrganisationalUnit)) {
            throw new IllegalArgumentAppException("Field of studies to create have non-unique names within organisational unit!");
        }
    }

    private Map<Long, List<String>> getNamesByOrganisationalUnit(Collection<FieldOfStudy> fieldOfStudies) {
        return fieldOfStudies.stream()
                .collect(Collectors.groupingBy(filedOfStudy -> filedOfStudy.getOrganisationalUnit().getId(),
                        Collectors.mapping(FieldOfStudy::getName, Collectors.toList())));
    }

    private boolean areThereNameDuplicatesWithinOrganisationalUnit(Map<Long, List<String>> namesByOrganisationalUnit) {
        return namesByOrganisationalUnit.entrySet().stream()
                .anyMatch(fieldNamesByUnit -> fieldNamesByUnit.getValue().size() != new HashSet<>(fieldNamesByUnit.getValue()).size());
    }

    private void validateIfThereAreNoNameDuplicatesAmongExistingFieldOfStudies(Collection<FieldOfStudy> fieldOfStudies) {
        List<FieldOfStudy> foundDuplicates = findDuplicatedNamesByOrganisationalUnitId(fieldOfStudies);
        if (!foundDuplicates.isEmpty()) {
            throw new IllegalArgumentAppException(MessageFormat.format("There are existing field of studies with given names {0}", getNamesByOrganisationalUnit(foundDuplicates)));
        }
    }

    private List<FieldOfStudy> findDuplicatedNamesByOrganisationalUnitId(Collection<FieldOfStudy> fieldOfStudies) {
        //TODO: implement validation of field name uniqueness within organisational unit
        return Collections.emptyList();
    }

}
