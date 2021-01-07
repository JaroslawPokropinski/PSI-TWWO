package psi.domain.studiesplan.boundary;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psi.api.studiesplan.StudiesPlanDTO;
import psi.domain.studiesplan.control.StudiesPlanService;
import psi.domain.studiesplan.entity.StudiesPlan;

import java.util.List;

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


}
