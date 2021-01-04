package psi.domain.studiesprogram.boundary;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psi.api.studiesprogram.StudiesProgramDTO;
import psi.domain.document.DocumentGenerator;
import psi.domain.studiesprogram.control.StudiesProgramService;
import psi.domain.studiesprogram.entity.StudiesProgram;
import psi.infrastructure.mediatype.MediaTypeResolver;

import java.util.List;

import static psi.infrastructure.rest.ResourcePaths.IDS;
import static psi.infrastructure.rest.ResourcePaths.IDS_PATH;

@Api(tags = "Studies Program")
@RestController
@RequestMapping(StudiesProgramController.STUDIES_PROGRAM_RESOURCE)
@RequiredArgsConstructor
public class StudiesProgramController {

    public static final String STUDIES_PROGRAM_RESOURCE = "/api/studies-program";
    public static final String HISTORY = "/history";

    private final StudiesProgramService studiesProgramService;
    private final StudiesProgramMapper studiesProgramMapper;
    private final MediaTypeResolver mediaTypeResolver;

    @ApiOperation(value = "${api.studies-program.getStudiesPrograms.value}", notes = "${api.studies-program.getStudiesPrograms.notes}")
    @GetMapping(IDS_PATH)
    public List<StudiesProgramDTO> getStudiesPrograms(@PathVariable(IDS) List<Long> ids){
        List<StudiesProgram> foundStudiesPrograms = studiesProgramService.getAllStudiesPrograms();
        return studiesProgramMapper.mapToStudiesProgramsDTOs(foundStudiesPrograms);
    }


}
