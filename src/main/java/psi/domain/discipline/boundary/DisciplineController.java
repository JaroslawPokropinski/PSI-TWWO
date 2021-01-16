package psi.domain.discipline.boundary;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import psi.api.common.ResourceDTO;
import psi.api.discipline.DisciplineDTO;
import psi.api.discipline.DisciplineDetailsDTO;
import psi.domain.discipline.control.DisciplineService;
import psi.domain.discipline.entity.Discipline;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Discipline")
@RestController
@RequestMapping(DisciplineController.DISCIPLINE_RESOURCE)
@RequiredArgsConstructor
public class DisciplineController {

    public static final String DISCIPLINE_RESOURCE = "/api/disciplines";

    private final DisciplineService disciplineService;
    private final DisciplineMapper disciplineMapper;

    @ApiOperation(value = "${api.disciplines.getAllDisciplines.value}", notes = "${api.disciplines.getAllDisciplines.notes}")
    @GetMapping
    public List<DisciplineDetailsDTO> getAllDisciplines() {
        List<Discipline> allDisciplines = disciplineService.getAllDisciplines();
        return disciplineMapper.mapToDisciplineDTOs(allDisciplines);
    }

    @ApiOperation(value = "${api.disciplines.createDisciplines.value}", notes = "${api.disciplines.createDisciplines.notes}")
    @PostMapping
    public List<ResourceDTO> createDiscipline(@Valid @RequestBody List<DisciplineDTO> disciplineDTOs){
        List<Discipline> disciplines = disciplineMapper.mapToDisciplines(disciplineDTOs);
        List<Discipline> createdDisciplines = disciplineService.createDisciplines(disciplines);
        return disciplineMapper.mapToResourceDTOs(createdDisciplines);
    }

}
