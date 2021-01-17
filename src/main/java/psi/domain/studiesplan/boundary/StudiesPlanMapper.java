package psi.domain.studiesplan.boundary;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import psi.api.common.PaginatedResultsDTO;
import psi.api.common.ResourceDTO;
import psi.api.revision.RevisionDTO;
import psi.api.studiesplan.StudiesPlanDTO;
import psi.domain.studiesplan.entity.StudiesPlan;
import psi.domain.studiesprogram.boundary.StudiesProgramMapper;
import psi.domain.studiesprogram.control.StudiesProgramService;
import psi.infrastructure.rest.PageUri;

import java.net.URI;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static psi.infrastructure.rest.ResourcePaths.IDS_PATH;

@Component
@RequiredArgsConstructor
public class StudiesPlanMapper {

    private final StudiesProgramService studiesProgramService;

    private final StudiesProgramMapper studiesProgramMapper;

    public List<StudiesPlanDTO> mapToStudiesPlansDTOs(Collection<StudiesPlan> studiesPlans){
        return studiesPlans.stream()
                .map(this::mapToStudiesPlanDTO)
                .collect(Collectors.toList());
    }

    public StudiesPlanDTO mapToStudiesPlanDTO(StudiesPlan studiesPlan){
        if (studiesPlan == null){
            return null;
        }
        return StudiesPlanDTO.builder()
                .id(studiesPlan.getId())
                .code(studiesPlan.getCode())
                .decreeDate(studiesPlan.getDecreeDate().toString())
                .inEffectSince(studiesPlan.getInEffectSince().toString())
                .studiesProgramId(studiesPlan.getStudiesProgram().getId())
                .build();
    }

    public List<StudiesPlan> mapToStudiesPlans(Collection<StudiesPlanDTO> studiesPlanDTOs){
        return studiesPlanDTOs.stream()
                .map(this::mapToStudiesPlan)
                .collect(Collectors.toList());
    }

    private StudiesPlan mapToStudiesPlan(StudiesPlanDTO studiesPlanDTO){
        if(studiesPlanDTO == null){
            return null;
        }
        return StudiesPlan.builder()
                .id(studiesPlanDTO.getId())
                .code(studiesPlanDTO.getCode())
                .decreeDate(LocalDate.parse(studiesPlanDTO.getDecreeDate()))
                .inEffectSince(LocalDate.parse(studiesPlanDTO.getInEffectSince()))
                .studiesProgram(studiesProgramService.getStudiesProgramById(studiesPlanDTO.getStudiesProgramId()))
                .build();
    }

    public List<ResourceDTO> mapToResourceDTOs(Collection<StudiesPlan> studiesPlans){
        return studiesPlans.stream()
                .map(this::mapToResourceDTO)
                .collect(Collectors.toList());
    }

    private ResourceDTO mapToResourceDTO(StudiesPlan studiesPlan){
        if(studiesPlan == null){
            return null;
        }
        return ResourceDTO.builder()
                .id(studiesPlan.getId())
                .identifier(studiesPlan.getCode())
                .uri(getStudiesPlanUrl(studiesPlan))
                .build();
    }

    private URI getStudiesPlanUrl(StudiesPlan studiesPlan){
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(StudiesPlanController.STUDIES_PLAN_RESOURCE)
                .path(IDS_PATH)
                .buildAndExpand(studiesPlan.getId())
                .toUri();
    }

    public PaginatedResultsDTO<RevisionDTO<StudiesPlanDTO>> mapToRevisionDTOs(Page<Revision<Integer, StudiesPlan>> studiesPlanHistoryPage){
        return PaginatedResultsDTO.<RevisionDTO<StudiesPlanDTO>>builder()
                .results(mapToRevisionDTOs(studiesPlanHistoryPage.getContent()))
                .totalSize(studiesPlanHistoryPage.getTotalElements())
                .pageSize(studiesPlanHistoryPage.getSize())
                .pageNumber(studiesPlanHistoryPage.getNumber())
                .nextPage(PageUri.generatePageUri(getHistoryResourceUri(), studiesPlanHistoryPage.nextOrLastPageable()))
                .previousPage(PageUri.generatePageUri(getHistoryResourceUri(), studiesPlanHistoryPage.previousOrFirstPageable()))
                .firstPage(PageUri.generatePageUri(getHistoryResourceUri(), studiesPlanHistoryPage.getPageable().first()))
                .lastPage(PageUri.generatePageUri(getHistoryResourceUri(), PageUri.getLastPageable(studiesPlanHistoryPage)))
                .build();
    }

    private List<RevisionDTO<StudiesPlanDTO>> mapToRevisionDTOs(Collection<Revision<Integer, StudiesPlan>> revisions){
        return revisions.stream()
                .map(this::mapToRevisionDTO)
                .collect(Collectors.toList());
    }

    private RevisionDTO<StudiesPlanDTO> mapToRevisionDTO(Revision<Integer, StudiesPlan> revision){
        if (revision == null){
            return null;
        }
        return RevisionDTO.<StudiesPlanDTO>builder()
                .revisionId(revision.getRequiredRevisionNumber())
                .revisionInstant(revision.getRequiredRevisionInstant())
                .revisionType(revision.getMetadata().getRevisionType())
                .entity(mapToStudiesPlanDTO(revision.getEntity()))
                .build();
    }

    private UriComponentsBuilder getHistoryResourceUri(){
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(StudiesPlanController.STUDIES_PLAN_RESOURCE)
                .path(StudiesPlanController.HISTORY);
    }

}
