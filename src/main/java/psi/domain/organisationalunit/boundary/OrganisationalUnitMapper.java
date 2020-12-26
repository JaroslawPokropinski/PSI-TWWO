package psi.domain.organisationalunit.boundary;

import org.springframework.stereotype.Component;
import psi.api.organisationalunit.OrganisationalUnitDTO;
import psi.domain.organisationalunit.entity.OrganisationalUnit;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrganisationalUnitMapper {

    public List<OrganisationalUnitDTO> mapToOrganisationalUnitDTOs(Collection<OrganisationalUnit> organisationalUnits) {
        return organisationalUnits.stream()
                .map(this::mapToOrganisationalUnitDTO)
                .collect(Collectors.toList());
    }

    private OrganisationalUnitDTO mapToOrganisationalUnitDTO(OrganisationalUnit organisationalUnit) {
        return new OrganisationalUnitDTO(organisationalUnit.getId(), organisationalUnit.getName(), organisationalUnit.getType());
    }

}
