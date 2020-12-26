package psi.domain.fieldofstudy.boundary;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psi.api.fieldofstudy.FieldOfStudyDTO;
import psi.domain.fieldofstudy.control.FieldOfStudyService;
import psi.domain.fieldofstudy.entity.FieldOfStudy;

import java.util.List;

@Api(tags = "Filed of study")
@RestController
@RequestMapping(FieldOfStudyController.FILED_OF_STUDY_RESOURCE)
@RequiredArgsConstructor
public class FieldOfStudyController {

    public static final String FILED_OF_STUDY_RESOURCE = "/api/filed-of-study";

    private final FieldOfStudyService fieldOfStudyService;
    private final FieldOfStudyMapper fieldOfStudyMapper;

    @GetMapping
    public List<FieldOfStudyDTO> getAllFiledOfStudies() {
        List<FieldOfStudy> foundFiledOfStudies = fieldOfStudyService.getAllFiledOfStudies();
        return fieldOfStudyMapper.mapToFiledOfStudyDTOs(foundFiledOfStudies);
    }

}
