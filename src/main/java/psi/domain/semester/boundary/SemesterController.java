package psi.domain.semester.boundary;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import psi.api.common.ResourceDTO;
import psi.api.common.ResponseDTO;
import psi.api.semester.SemesterDTO;
import psi.domain.semester.control.SemesterService;
import psi.domain.semester.entity.Semester;
import psi.infrastructure.security.UserInfo;
import psi.infrastructure.security.annotation.LoggedUser;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

import static psi.infrastructure.rest.ResourcePaths.*;

@Api(tags = "Semester")
@RestController
@RequestMapping(SemesterController.SEMESTER_RESOURCE)
@RequiredArgsConstructor
public class SemesterController {

    public static final String SEMESTER_RESOURCE = "/api/semester";
    public static final String SEARCH_RESOURCE = "/search";
    public static final String HISTORY = "/history";
    public static final String STATUS = "/status";
    public static final String STUDIES_PLAN = "/studies-plan";

    private final SemesterService semesterService;
    public final SemesterMapper semesterMapper;

    @ApiOperation(value =  "${api.semester.read.value}", notes = "${api.semester.read.notes}")
    @GetMapping(IDS_PATH)
    public List<SemesterDTO> getSemesters(@PathVariable(IDS) List<Long> ids){
        List<Semester> foundSemesters = semesterService.getSemestersByIds(ids);
        return semesterMapper.mapToSemesterDTOs(foundSemesters);
    }

    @ApiOperation(value =  "${api.semester.readWithStudiesPlan.value}", notes = "${api.semester.readWithStudiesPlan.notes}")
    @GetMapping(STUDIES_PLAN + ID_PATH)
    public List<SemesterDTO> getSemestersForStudiesPlan(@PathVariable(ID) Long id){
        List<Semester> foundSemesters = semesterService.getSemestersByStudiesPlanId(id);
        return semesterMapper.mapToSemesterDTOs(foundSemesters);
    }

    @ApiOperation(value =  "${api.semester.create.value}", notes = "${api.semester.create.notes}")
    @PostMapping
    public List<ResourceDTO> createSemesters(@Valid @RequestBody List<SemesterDTO> semesterDTOs){
        List<Semester> semesters = semesterMapper.mapToSemesters(semesterDTOs);
        List<Semester> createdSemesters = semesterService.createSemesters(semesters);
        return semesterMapper.mapToResourceDTOs(createdSemesters);
    }

    @ApiOperation(value =  "${api.semester.update.value}", notes = "${api.semester.update.notes}")
    @PutMapping
    public List<ResourceDTO> updateSemester(@Valid @RequestBody List<SemesterDTO> semesterDTOs, @ApiIgnore @LoggedUser UserInfo userInfo){
        List<Semester> semesters = semesterMapper.mapToSemesters(semesterDTOs);
        semesterService.updateSemesters(semesters, userInfo.getId());
        return semesterMapper.mapToResourceDTOs(semesters);
    }

    @ApiOperation(value =  "${api.semester.delete.value}", notes = "${api.semester.delete.notes}")
    @DeleteMapping(IDS_PATH)
    public ResponseDTO<Boolean> deleteSemesters(@PathVariable(IDS) List<Long> ids, @ApiIgnore @LoggedUser UserInfo userInfo){
        semesterService.deleteSemesters(ids, userInfo.getId());
        return new ResponseDTO<>(true, "Semesters deleted successfully");
    }

}
