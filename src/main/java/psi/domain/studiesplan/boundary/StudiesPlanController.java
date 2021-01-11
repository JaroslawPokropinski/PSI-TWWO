package psi.domain.studiesplan.boundary;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.web.bind.annotation.*;
import psi.api.common.PaginatedResultsDTO;
import psi.api.common.ResourceDTO;
import psi.api.common.ResponseDTO;
import psi.api.revision.RevisionDTO;
import psi.api.studiesplan.StudiesPlanDTO;
import psi.domain.studiesplan.control.StudiesPlanService;
import psi.domain.studiesplan.entity.StudiesPlan;
import psi.infrastructure.security.UserInfo;
import psi.infrastructure.security.annotation.LoggedUser;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

import static psi.infrastructure.rest.ResourcePaths.*;

@Api(tags = "Studies Plan")
@RestController
@RequestMapping(StudiesPlanController.STUDIES_PLAN_RESOURCE)
@RequiredArgsConstructor
public class StudiesPlanController {

    public static final String STUDIES_PLAN_RESOURCE = "/api/studies-plan";
    public static final String SEARCH_RESOURCE = "/search";
    public static final String HISTORY = "/history";

    private final StudiesPlanService studiesPlanService;
    private final StudiesPlanMapper studiesPlanMapper;

    @ApiOperation(value = "${api.studies-program.getAllStudiesPlan.value}", notes = "${api.studies-program.getAllStudiesPlan.notes}")
    @GetMapping()
    public List<StudiesPlanDTO> getAllStudiesPlans(){
        List<StudiesPlan> studiesPlans = studiesPlanService.getAllStudiesPlans();
        return studiesPlanMapper.mapToStudiesPlansDTOs(studiesPlans);
    }

    @ApiOperation(value = "${api.studies-program.getStudiesPlan.value}", notes = "${api.studies-program.getStudiesPlan.notes}")
    @GetMapping(IDS_PATH)
    public List<StudiesPlanDTO> getStudiesPlans(@PathVariable(IDS) List<Long> ids){
        List<StudiesPlan> foundStudiesPlans = studiesPlanService.getStudiesPlansByIds(ids);
        return studiesPlanMapper.mapToStudiesPlansDTOs(foundStudiesPlans);
    }

    @ApiOperation(value = "${api.studies-program.createStudiesPlan.value}", notes = "${api.studies-program.createStudiesPlan.notes}")
    @PostMapping
    public List<ResourceDTO> createStudiesPlans(@Valid @RequestBody List<StudiesPlanDTO> studiesPlanDTOs){
        List<StudiesPlan> studiesPlans = studiesPlanMapper.mapToStudiesPlans(studiesPlanDTOs);
        List<StudiesPlan> createdStudiesPlans = studiesPlanService.createStudiesPlans(studiesPlans);
        return studiesPlanMapper.mapToResourceDTOs(createdStudiesPlans);
    }

    @ApiOperation(value = "${api.studies-program.updateStudiesPlan.value}", notes = "${api.studies-program.updateStudiesPlan.notes}")
    @PutMapping
    public List<ResourceDTO> updateStudiesPlans(@Valid @RequestBody List<StudiesPlanDTO> studiesPlansDTOs, @ApiIgnore @LoggedUser UserInfo userInfo){
        List<StudiesPlan> studiesPlans = studiesPlanMapper.mapToStudiesPlans(studiesPlansDTOs);
        studiesPlanService.updateStudiesPlans(studiesPlans, userInfo.getId());
        return studiesPlanMapper.mapToResourceDTOs(studiesPlans);
    }

    @ApiOperation(value = "${api.studies-program.deleteStudiesPlan.value}", notes = "${api.studies-program.deleteStudiesPlan.notes}")
    @DeleteMapping(IDS_PATH)
    public ResponseDTO<Boolean> deleteStudiesPlan(@PathVariable(IDS) List<Long> ids, @ApiIgnore @LoggedUser UserInfo userInfo){
        studiesPlanService.deleteStudiesPlans(ids, userInfo.getId());
        return new ResponseDTO<>(true, "Studies plans deleted successfully");
    }

    @ApiOperation(value = "${api.studies-program.getStudiesPlanHistory.value}", notes = "${api.studies-program.getStudiesPlanHistory.notes}")
    @GetMapping(HISTORY + ID_PATH)
    public PaginatedResultsDTO<RevisionDTO<StudiesPlanDTO>> getStudiesPlanHistory(@PathVariable(ID) Long id, Pageable pageable){
        Page<Revision<Integer, StudiesPlan>> studiesPlanHistoryPage = studiesPlanService.getStudiesPlanHistory(id, pageable);
        return studiesPlanMapper.mapToRevisionDTOs(studiesPlanHistoryPage);
    }


}
