package psi.domain.organisationalunit.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import psi.domain.organisationalunit.entity.OrganisationalUnit;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganisationalUnitService {

    private final OrganisationalUnitRepository organisationalUnitRepository;

    public List<OrganisationalUnit> getAllOrganisationalUnits() {
        return organisationalUnitRepository.findAll();
    }

}
