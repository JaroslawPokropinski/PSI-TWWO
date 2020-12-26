package psi.domain.organisationalunit.boundary;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psi.api.organisationalunit.OrganisationalUnitDTO;
import psi.domain.organisationalunit.entity.OrganisationalUnit;
import psi.domain.organisationalunit.control.OrganisationalUnitService;

import java.util.List;

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

}
