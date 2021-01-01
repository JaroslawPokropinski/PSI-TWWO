package psi.domain.organisationalunit.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psi.domain.organisationalunit.entity.OrganisationalUnit;
import psi.infrastructure.exception.ExceptionUtils;
import psi.infrastructure.exception.IllegalArgumentAppException;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrganisationalUnitService {

    private final OrganisationalUnitRepository organisationalUnitRepository;

    public List<OrganisationalUnit> getAllOrganisationalUnits() {
        return organisationalUnitRepository.findAll();
    }

    public OrganisationalUnit getOrganisationalUnitById(Long id) {
        return organisationalUnitRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.getObjectNotFoundException(OrganisationalUnit.class, id));
    }

    public List<OrganisationalUnit> getOrganisationalUnitsByIds(Collection<Long> ids) {
        return organisationalUnitRepository.findAllById(ids);
    }

    public List<OrganisationalUnit> createOrganisationalUnits(Collection<OrganisationalUnit> organisationalUnits) {
        prepareBeforeCreate(organisationalUnits);
        validateBeforeCreate(organisationalUnits);
        return organisationalUnitRepository.saveAll(organisationalUnits);
    }

    private void prepareBeforeCreate(Collection<OrganisationalUnit> organisationalUnits) {
        organisationalUnits.forEach(this::prepareBeforeCreate);
    }

    private void prepareBeforeCreate(OrganisationalUnit organisationalUnit) {
        organisationalUnit.setId(null);
    }

    private void validateBeforeCreate(Collection<OrganisationalUnit> organisationalUnits) {
        validateNameUniqueness(organisationalUnits);
    }

    private void validateNameUniqueness(Collection<OrganisationalUnit> organisationalUnits) {
        validateIfThereAreNoNameDuplicatesAmongSubmittedOrganisationalUnits(organisationalUnits);
        validateIfThereAreNoNameDuplicatesAmongExistingOrganisationalUnits(organisationalUnits);
    }

    private void validateIfThereAreNoNameDuplicatesAmongSubmittedOrganisationalUnits(Collection<OrganisationalUnit> organisationalUnits) {
        List<String> names = getNames(organisationalUnits);
        if (names.size() != new HashSet<>(names).size()) {
            throw new IllegalArgumentAppException("Organisational units to create have non-unique names!");
        }
    }

    private List<String> getNames(Collection<OrganisationalUnit> organisationalUnits) {
        return organisationalUnits.stream()
                .map(OrganisationalUnit::getName)
                .collect(Collectors.toList());
    }

    private void validateIfThereAreNoNameDuplicatesAmongExistingOrganisationalUnits(Collection<OrganisationalUnit> organisationalUnits) {
        List<OrganisationalUnit> foundDuplicates = findDuplicatedNames(organisationalUnits);
        if (!foundDuplicates.isEmpty()) {
            throw new IllegalArgumentAppException(MessageFormat.format("There are existing organisational units with given names {0}", getNames(foundDuplicates)));
        }
    }

    private List<OrganisationalUnit> findDuplicatedNames(Collection<OrganisationalUnit> organisationalUnits) {
        Set<Long> organisationalUnitIds = getNonNullUniqueIds(organisationalUnits);
        if (organisationalUnitIds.isEmpty()) {
            return organisationalUnitRepository.findAllByNameIn(getNames(organisationalUnits));
        }
        return organisationalUnitRepository.findAllByNameInAndIdNotIn(getNames(organisationalUnits), organisationalUnitIds);
    }

    private Set<Long> getNonNullUniqueIds(Collection<OrganisationalUnit> organisationalUnits) {
        return organisationalUnits.stream()
                .map(OrganisationalUnit::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

}
