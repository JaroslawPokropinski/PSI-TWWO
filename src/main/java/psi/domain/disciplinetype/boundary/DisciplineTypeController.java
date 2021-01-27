package psi.domain.disciplinetype.boundary;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psi.api.common.ResourceDTO;
import psi.domain.disciplinetype.control.DisciplineTypeService;
import psi.domain.disciplinetype.entity.DisciplineType;
import psi.infrastructure.security.annotation.HasCommissionMemberRole;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Discipline Type")
@RestController
@RequestMapping(DisciplineTypeController.DISCIPLINE_TYPE_RESOURCE)
@RequiredArgsConstructor
public class DisciplineTypeController {

    public static final String DISCIPLINE_TYPE_RESOURCE = "/api/discipline-type";
    public static final String HISTORY = "/history";

    private final DisciplineTypeService disciplineTypeService;
    private final DisciplineTypeMapper disciplineTypeMapper;

    @HasCommissionMemberRole
    @ApiOperation(value = "${api.disciplineTypes.createDisciplineTypes.value}", notes = "${api.disciplineTypes.createDisciplineTypes.notes}")
    @PostMapping
    public List<ResourceDTO> createDisciplineType(@Valid @RequestBody List<DisciplineType> disciplineTypes){
        List<DisciplineType> createdDisciplineTypes = disciplineTypeService.createDisciplineTypes(disciplineTypes);
        return disciplineTypeMapper.mapToResourceDTOs(createdDisciplineTypes);
    }

}
