package psi.domain.discipline.boundary;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psi.api.discipline.DisciplineDTO;
import psi.domain.discipline.control.DisciplineService;
import psi.domain.discipline.entity.Discipline;

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
    public List<DisciplineDTO> getAllDisciplines() {
        List<Discipline> allDisciplines = disciplineService.getAllDisciplines();
        return disciplineMapper.mapToDisciplineDTOs(allDisciplines);
    }

}
