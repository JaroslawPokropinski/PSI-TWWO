package psi.domain.fieldofstudy.boundary;

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
import psi.api.fieldofstudy.FieldOfStudyDTO;
import psi.domain.fieldofstudy.control.FieldOfStudyService;
import psi.domain.fieldofstudy.entity.FieldOfStudy;
import psi.infrastructure.security.annotation.HasCommissionMemberRole;

import javax.validation.Valid;
import java.util.List;

import static psi.infrastructure.rest.ResourcePaths.IDS;
import static psi.infrastructure.rest.ResourcePaths.IDS_PATH;

@Api(tags = "Field of study")
@RestController
@RequestMapping(FieldOfStudyController.FILED_OF_STUDY_RESOURCE)
@RequiredArgsConstructor
public class FieldOfStudyController {

    public static final String FILED_OF_STUDY_RESOURCE = "/api/field-of-study";

    private final FieldOfStudyService fieldOfStudyService;
    private final FieldOfStudyMapper fieldOfStudyMapper;

    @ApiOperation(value = "${api.field-of-studies.getAllFiledOfStudies.value}", notes = "${api.field-of-studies.getAllFiledOfStudies.notes}")
    @GetMapping
    public List<FieldOfStudyDTO> getAllFiledOfStudies() {
        List<FieldOfStudy> foundFiledOfStudies = fieldOfStudyService.getAllFiledOfStudies();
        return fieldOfStudyMapper.mapToFiledOfStudyDTOs(foundFiledOfStudies);
    }

    @ApiOperation(value = "${api.field-of-studies.getFieldOfStudiesByIds.value}", notes = "${api.field-of-studies.getFieldOfStudiesByIds.notes}")
    @GetMapping(IDS_PATH)
    public List<FieldOfStudyDTO> getFieldOfStudiesByIds(@PathVariable(IDS) List<Long> ids) {
        List<FieldOfStudy> foundFiledOfStudies = fieldOfStudyService.getFieldOfStudiesByIds(ids);
        return fieldOfStudyMapper.mapToFiledOfStudyDTOs(foundFiledOfStudies);
    }

    @HasCommissionMemberRole
    @ApiOperation(value = "${api.field-of-studies.createFiledOfStudies.value}", notes = "${api.field-of-studies.createFiledOfStudies.notes}")
    @PostMapping
    public List<ResourceDTO> createFiledOfStudies(@Valid @RequestBody List<FieldOfStudyDTO> fieldOfStudyDTOs) {
        List<FieldOfStudy> fieldOfStudies = fieldOfStudyMapper.mapToFiledOfStudies(fieldOfStudyDTOs);
        List<FieldOfStudy> createdFieldOfStudies = fieldOfStudyService.createFieldOfStudies(fieldOfStudies);
        return fieldOfStudyMapper.mapToResourceDTOs(createdFieldOfStudies);
    }

}
