package psi.domain.organisationalunit.boundary;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import psi.api.common.ResourceDTO;
import psi.api.organisationalunit.OrganisationalUnitDTO;
import psi.domain.organisationalunit.entity.OrganisationalUnit;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static psi.infrastructure.rest.ResourcePaths.IDS_PATH;

@Component
public class OrganisationalUnitMapper {

    public List<OrganisationalUnitDTO> mapToOrganisationalUnitDTOs(Collection<OrganisationalUnit> organisationalUnits) {
        return organisationalUnits.stream()
                .map(this::mapToOrganisationalUnitDTO)
                .collect(Collectors.toList());
    }

    public OrganisationalUnitDTO mapToOrganisationalUnitDTO(OrganisationalUnit organisationalUnit) {
        return new OrganisationalUnitDTO(organisationalUnit.getId(), organisationalUnit.getName(), organisationalUnit.getType());
    }

    public List<OrganisationalUnit> mapToOrganisationalUnits(Collection<OrganisationalUnitDTO> organisationalUnitDTOs) {
        return organisationalUnitDTOs.stream()
                .map(this::mapToOrganisationalUnit)
                .collect(Collectors.toList());
    }

    private OrganisationalUnit mapToOrganisationalUnit(OrganisationalUnitDTO organisationalUnitDTO) {
        return new OrganisationalUnit(organisationalUnitDTO.getId(), organisationalUnitDTO.getName(), organisationalUnitDTO.getType());
    }

    public List<ResourceDTO> mapToResourceDTOs(Collection<OrganisationalUnit> organisationalUnits) {
        return organisationalUnits.stream()
                .map(this::mapToResourceDTO)
                .collect(Collectors.toList());
    }

    private ResourceDTO mapToResourceDTO(OrganisationalUnit organisationalUnit) {
        if (organisationalUnit == null) {
            return null;
        }
        return ResourceDTO.builder()
                .id(organisationalUnit.getId())
                .identifier(organisationalUnit.getName())
                .uri(getOrganisationalUnitUrl(organisationalUnit))
                .build();
    }

    private URI getOrganisationalUnitUrl(OrganisationalUnit organisationalUnit) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(OrganisationalUnitController.ORGANISATIONAL_UNIT_RESOURCE)
                .path(IDS_PATH)
                .buildAndExpand(organisationalUnit.getId())
                .toUri();
    }

}
