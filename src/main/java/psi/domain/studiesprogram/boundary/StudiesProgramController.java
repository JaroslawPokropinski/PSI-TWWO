package psi.domain.studiesprogram.boundary;

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
import psi.api.common.StatusDTO;
import psi.api.revision.RevisionDTO;
import psi.api.studiesprogram.StudiesProgramDTO;
import psi.domain.auditedobject.entity.ObjectState;
import psi.domain.studiesprogram.control.StudiesProgramService;
import psi.domain.studiesprogram.entity.StudiesProgram;
import psi.infrastructure.security.UserInfo;
import psi.infrastructure.security.annotation.LoggedUser;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static psi.infrastructure.rest.ResourcePaths.*;

@Api(tags = "Studies Program")
@RestController
@RequestMapping(StudiesProgramController.STUDIES_PROGRAM_RESOURCE)
@RequiredArgsConstructor
public class StudiesProgramController {

    public static final String STUDIES_PROGRAM_RESOURCE = "/api/studies-program";
    public static final String HISTORY = "/history";
    public static final String SEARCH_RESOURCE = "/search";
    public static final String STATUS = "/status";

    private final StudiesProgramService studiesProgramService;
    private final StudiesProgramMapper studiesProgramMapper;

    @ApiOperation(value = "${api.studies-program.searchStudiesProgram.value}", notes = "${api.studies-program.searchStudiesProgram.notes}")
    @GetMapping(STUDIES_PROGRAM_RESOURCE)
    public PaginatedResultsDTO<StudiesProgramDTO> searchStudiesPrograms(@RequestParam String query, @Valid Pageable pageable){
        Page<StudiesProgram> studiesProgramPage = studiesProgramService.searchStudiesProgramsByRSQL(query, pageable);
        return studiesProgramMapper.mapToSearchResultDTO(studiesProgramPage, query);
    }

    @ApiOperation(value = "${api.studies-program.getStudiesPrograms.value}", notes = "${api.studies-program.getStudiesPrograms.notes}")
    @GetMapping(IDS_PATH)
    public List<StudiesProgramDTO> getStudiesPrograms(@PathVariable(IDS) List<Long> ids){
        List<StudiesProgram> foundStudiesPrograms = studiesProgramService.getStudiesProgramByIds(ids);
        return studiesProgramMapper.mapToStudiesProgramsDTOs(foundStudiesPrograms);
    }

    @ApiOperation(value = "${api.studies-program.getAllStudiesPrograms.value}", notes = "${api.studies-program.getAllStudiesPrograms.notes}")
    @GetMapping()
    public List<StudiesProgramDTO> getAllStudiesPrograms(){
        List<StudiesProgram> foundStudiesPrograms = studiesProgramService.getAllStudiesPrograms();
        return studiesProgramMapper.mapToStudiesProgramsDTOs(foundStudiesPrograms);
    }

    @ApiOperation(value = "${api.studies-program.createStudiesPrograms.value}", notes = "${api.studies-program.createStudiesPrograms.notes}")
    @PostMapping
    public List<ResourceDTO> createStudiesPrograms(@Valid @RequestBody List<StudiesProgramDTO> studiesProgramDTOs){
        List<StudiesProgram> studiesPrograms = studiesProgramMapper.mapToStudiesPrograms(studiesProgramDTOs);
        List<StudiesProgram> createdStudiesPrograms = studiesProgramService.createStudiesPrograms(studiesPrograms);
        return studiesProgramMapper.mapToResourceDTOs(createdStudiesPrograms);
    }

    @ApiOperation(value = "${api.studies-program.updateStudiesPrograms.value}", notes = "${api.studies-program.updateStudiesPrograms.notes}")
    @PutMapping
    public List<ResourceDTO> updateStudiesPrograms(@Valid @RequestBody List<StudiesProgramDTO> studiesProgramsDTOs, @ApiIgnore @LoggedUser UserInfo userInfo){
        List<StudiesProgram> studiesPrograms = studiesProgramMapper.mapToStudiesPrograms(studiesProgramsDTOs);
        studiesProgramService.updateStudiesPrograms(studiesPrograms, userInfo.getId());
        return studiesProgramMapper.mapToResourceDTOs(studiesPrograms);
    }

    @ApiOperation(value = "${api.studies-program.deleteStudiesPrograms.value}", notes = "${api.studies-program.deleteStudiesPrograms.notes}")
    @DeleteMapping(IDS_PATH)
    public ResponseDTO<Boolean> deleteStudiesPrograms(@PathVariable(IDS) List<Long> ids, @ApiIgnore @LoggedUser UserInfo userInfo){
        studiesProgramService.deleteStudiesPrograms(ids, userInfo.getId());
        return new ResponseDTO<>(true, "Studies Programs deleted successfully");
    }

    @ApiOperation(value = "${api.studies-program.getStudiesProgramsHistory.value}", notes = "${api.studies-program.getStudiesProgramsHistory.notes}")
    @GetMapping(HISTORY + ID_PATH)
    public PaginatedResultsDTO<RevisionDTO<StudiesProgramDTO>> getSubjectCardHistory(@PathVariable(ID) Long id, Pageable pageable) {
        Page<Revision<Integer, StudiesProgram>> subjectCardHistoryPage = studiesProgramService.getStudiesProgramHistory(id, pageable);
        return studiesProgramMapper.mapToRevisionDTOs(subjectCardHistoryPage);
    }

    @ApiOperation(value = "${api.studies-program.changeStatus.value}", notes = "${api.studies-program.changeStatus.notes}")
    @PatchMapping(STATUS)
    public ResponseDTO<Boolean> changeStatus(@PathVariable(ID) Collection<Long> ids, @Valid @RequestBody StatusDTO statusDTO, @ApiIgnore UserInfo userInfo){
        studiesProgramService.changeStudiesProgramState(ids, ObjectState.valueOf(statusDTO.getStatus().name()), userInfo.getId());
        return new ResponseDTO<>(true, "Status changed successfully");
    }

}
