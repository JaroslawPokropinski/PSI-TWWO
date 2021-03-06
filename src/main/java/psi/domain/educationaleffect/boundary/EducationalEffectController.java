package psi.domain.educationaleffect.boundary;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.web.bind.annotation.*;
import psi.api.common.ResourceDTO;
import psi.api.common.ResponseDTO;
import psi.api.common.PaginatedResultsDTO;
import psi.api.common.StatusDTO;
import psi.api.educationaleffect.EducationalEffectDTO;
import psi.api.educationaleffect.EducationalEffectDetailsDTO;
import psi.api.revision.RevisionDTO;
import psi.domain.auditedobject.entity.ObjectState;
import psi.domain.educationaleffect.control.EducationalEffectService;
import psi.domain.educationaleffect.entity.EducationalEffect;
import psi.infrastructure.security.UserInfo;
import psi.infrastructure.security.annotation.HasCommissionMemberRole;
import psi.infrastructure.security.annotation.LoggedUser;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static psi.infrastructure.rest.ResourcePaths.ID;
import static psi.infrastructure.rest.ResourcePaths.IDS;
import static psi.infrastructure.rest.ResourcePaths.IDS_PATH;
import static psi.infrastructure.rest.ResourcePaths.ID_PATH;

@Api(tags = "Educational Effect")
@RestController
@RequestMapping(EducationalEffectController.EDUCATIONAL_EFFECT_RESOURCE)
@RequiredArgsConstructor
public class EducationalEffectController {

    public static final String EDUCATIONAL_EFFECT_RESOURCE = "/api/educational-effects";
    public static final String SEARCH_RESOURCE = "/search";
    public static final String HISTORY = "/history";
    public static final String STATUS = "/status";

    private final EducationalEffectMapper educationalEffectMapper;
    private final EducationalEffectService educationalEffectService;

    @ApiOperation(value = "${api.educational-effects.searchEducationalEffects.value}", notes = "${api.educational-effects.searchEducationalEffects.notes}")
    @GetMapping(SEARCH_RESOURCE)
    public PaginatedResultsDTO<EducationalEffectDetailsDTO> searchEducationalEffects(@RequestParam String query, Pageable pageable) {
        Page<EducationalEffect> educationalEffectPage = educationalEffectService.getEducationalEffectsByRSQL(query, pageable);
        return educationalEffectMapper.mapToSearchResultDTO(educationalEffectPage, query);
    }

    @ApiOperation(value = "${api.educational-effects.getEducationalEffects.value}", notes = "${api.educational-effects.getEducationalEffects.notes}")
    @GetMapping(IDS_PATH)
    public List<EducationalEffectDetailsDTO> getEducationalEffects(@PathVariable(IDS) List<Long> ids) {
        List<EducationalEffect> foundEducationalEffects = educationalEffectService.getEducationalEffectsByIds(ids);
        return educationalEffectMapper.mapToEducationalEffectDetailsDTOs(foundEducationalEffects);
    }

    @HasCommissionMemberRole
    @ApiOperation(value = "${api.educational-effects.createEducationalEffects.value}", notes = "${api.educational-effects.createEducationalEffects.notes}")
    @PostMapping
    public List<ResourceDTO> createEducationalEffects(@Valid @RequestBody List<EducationalEffectDTO> educationalEffectDTOs) {
        List<EducationalEffect> educationalEffectsToCreate = educationalEffectMapper.mapToEducationalEffects(educationalEffectDTOs);
        List<EducationalEffect> createdEducationalEffects = educationalEffectService.createEducationalEffects(educationalEffectsToCreate);
        return educationalEffectMapper.mapToResourceDTOs(createdEducationalEffects);
    }

    @HasCommissionMemberRole
    @ApiOperation(value = "${api.educational-effects.updateEducationalEffects.value}", notes = "${api.educational-effects.updateEducationalEffects.notes}")
    @PutMapping
    public List<ResourceDTO> updateEducationalEffects(@Valid @RequestBody List<EducationalEffectDTO> educationalEffectDTOs, @ApiIgnore @LoggedUser UserInfo userInfo) {
        List<EducationalEffect> educationalEffectsToUpdate = educationalEffectMapper.mapToEducationalEffects(educationalEffectDTOs);
        educationalEffectService.updateEducationalEffects(educationalEffectsToUpdate, userInfo.getId());
        return educationalEffectMapper.mapToResourceDTOs(educationalEffectsToUpdate);
    }

    @HasCommissionMemberRole
    @ApiOperation(value = "${api.educational-effects.deleteEducationalEffects.value}", notes = "${api.educational-effects.deleteEducationalEffects.notes}")
    @DeleteMapping(IDS_PATH)
    public ResponseDTO<Boolean> deleteEducationalEffects(@PathVariable(IDS) List<Long> ids, @ApiIgnore @LoggedUser UserInfo userInfo) {
        educationalEffectService.deleteEducationalEffects(ids, userInfo.getId());
        return new ResponseDTO<>(true, "Educational effects deleted successfully");
    }

    @ApiOperation(value = "${api.educational-effects.getEducationalEffectHistory.value}", notes = "${api.educational-effects.getEducationalEffectHistory.notes}")
    @GetMapping(HISTORY + ID_PATH)
    public PaginatedResultsDTO<RevisionDTO<EducationalEffectDetailsDTO>> getEducationalEffectHistory(@PathVariable(ID) Long id, Pageable pageable) {
        Page<Revision<Integer, EducationalEffect>> educationalEffectPage = educationalEffectService.getEducationalEffectHistory(id, pageable);
        return educationalEffectMapper.mapToRevisionDTOs(educationalEffectPage);
    }

    @HasCommissionMemberRole
    @ApiOperation(value = "${api.educational-effects.changeStatus.values}", notes = "${api.educational-effects.changeStatus.notes}")
    @PatchMapping(STATUS)
    public ResponseDTO<Boolean> changeStatus(@PathVariable(ID)Collection<Long> ids, @Valid @RequestBody StatusDTO statusDTO, @ApiIgnore UserInfo userInfo){
        educationalEffectService.changeEducationalEffect(ids, ObjectState.valueOf(statusDTO.getStatus().name()), userInfo.getId());
        return new ResponseDTO<>(true, "Status changed successfully");
    }

}
