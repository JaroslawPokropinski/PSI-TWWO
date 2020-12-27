package psi.domain.organisationalunit.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psi.domain.organisationalunit.entity.OrganisationalUnit;
import psi.infrastructure.exception.ExceptionUtils;

import java.util.List;

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

}
