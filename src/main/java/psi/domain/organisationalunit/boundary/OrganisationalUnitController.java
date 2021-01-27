package psi.domain.organisationalunit.boundary;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psi.api.common.ResourceDTO;
import psi.api.organisationalunit.OrganisationalUnitDTO;
import psi.domain.organisationalunit.entity.OrganisationalUnit;
import psi.domain.organisationalunit.control.OrganisationalUnitService;
import psi.infrastructure.security.annotation.HasCommissionMemberRole;

import javax.validation.Valid;
import java.util.List;

import static psi.infrastructure.rest.ResourcePaths.IDS;
import static psi.infrastructure.rest.ResourcePaths.IDS_PATH;

@Api(tags = "Organisational Unit")
@RestController
@RequestMapping(OrganisationalUnitController.ORGANISATIONAL_UNIT_RESOURCE)
@RequiredArgsConstructor
public class OrganisationalUnitController {

    public static final String ORGANISATIONAL_UNIT_RESOURCE = "/api/organisational-units";

    private final OrganisationalUnitService organisationalUnitService;
    private final OrganisationalUnitMapper organisationalUnitMapper;

    @ApiOperation(value = "${api.organisational-units.getAllOrganisationalUnits.value}", notes = "${api.organisational-units.getAllOrganisationalUnits.notes}")
    @GetMapping
    public List<OrganisationalUnitDTO> getAllOrganisationalUnits() {
        List<OrganisationalUnit> foundOrganisationalUnits = organisationalUnitService.getAllOrganisationalUnits();
        return organisationalUnitMapper.mapToOrganisationalUnitDTOs(foundOrganisationalUnits);
    }

    @ApiOperation(value = "${api.organisational-units.getOrganisationalUnitsByIds.value}", notes = "${api.organisational-units.getOrganisationalUnitsByIds.notes}")
    @GetMapping(IDS_PATH)
    public List<OrganisationalUnitDTO> getOrganisationalUnitsByIds(@PathVariable(IDS) List<Long> ids) {
        List<OrganisationalUnit> foundOrganisationalUnits = organisationalUnitService.getOrganisationalUnitsByIds(ids);
        return organisationalUnitMapper.mapToOrganisationalUnitDTOs(foundOrganisationalUnits);
    }

    @HasCommissionMemberRole
    @ApiOperation(value = "${api.organisational-units.createOrganisationalUnits.value}", notes = "${api.organisational-units.createOrganisationalUnits.notes}")
    @PostMapping
    public List<ResourceDTO> createOrganisationalUnits(@Valid @RequestBody List<OrganisationalUnitDTO> organisationalUnitDTOs) {
        List<OrganisationalUnit> organisationalUnitsToCreate = organisationalUnitMapper.mapToOrganisationalUnits(organisationalUnitDTOs);
        List<OrganisationalUnit> createdOrganisationalUnits = organisationalUnitService.createOrganisationalUnits(organisationalUnitsToCreate);
        return organisationalUnitMapper.mapToResourceDTOs(createdOrganisationalUnits);
    }

}
