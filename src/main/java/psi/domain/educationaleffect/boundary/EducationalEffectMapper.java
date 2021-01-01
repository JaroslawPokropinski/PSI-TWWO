package psi.domain.educationaleffect.boundary;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import psi.api.common.ResourceDTO;
import psi.api.common.PaginatedResultsDTO;
import psi.api.educationaleffect.EducationalEffectDTO;
import psi.api.educationaleffect.EducationalEffectDetailsDTO;
import psi.api.revision.RevisionDTO;
import psi.domain.educationaleffect.entity.EducationalEffect;
import psi.domain.user.boundary.UserMapper;
import psi.infrastructure.rest.PageUri;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static psi.infrastructure.rest.ResourcePaths.IDS_PATH;

@Component
@RequiredArgsConstructor
public class EducationalEffectMapper {

    private final UserMapper userMapper;

    public PaginatedResultsDTO<EducationalEffectDetailsDTO> mapToSearchResultDTO(Page<EducationalEffect> educationalEffectPage, String query) {
        if (educationalEffectPage == null) {
            return null;
        }
        return PaginatedResultsDTO.<EducationalEffectDetailsDTO>builder()
                .results(mapToEducationalEffectDetailsDTOs(educationalEffectPage.getContent()))
                .totalSize(educationalEffectPage.getTotalElements())
                .pageSize(educationalEffectPage.getSize())
                .pageNumber(educationalEffectPage.getNumber())
                .nextPage(PageUri.generatePageUri(getSearchResourceUri(query), educationalEffectPage.nextOrLastPageable()))
                .previousPage(PageUri.generatePageUri(getSearchResourceUri(query), educationalEffectPage.previousOrFirstPageable()))
                .firstPage(PageUri.generatePageUri(getSearchResourceUri(query), educationalEffectPage.getPageable().first()))
                .lastPage(PageUri.generatePageUri(getSearchResourceUri(query), PageUri.getLastPageable(educationalEffectPage)))
                .build();
    }

    private UriComponentsBuilder getSearchResourceUri(String query) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(EducationalEffectController.EDUCATIONAL_EFFECT_RESOURCE)
                .path(EducationalEffectController.SEARCH_RESOURCE)
                .queryParam("query", query);
    }

    public List<EducationalEffectDetailsDTO> mapToEducationalEffectDetailsDTOs(Collection<EducationalEffect> educationalEffects) {
        return educationalEffects.stream()
                .map(this::mapToEducationalEffectDetailsDTO)
                .collect(Collectors.toList());
    }

    private EducationalEffectDetailsDTO mapToEducationalEffectDetailsDTO(EducationalEffect educationalEffect) {
        if (educationalEffect == null) {
            return null;
        }
        return EducationalEffectDetailsDTO.builder()
                .id(educationalEffect.getId())
                .code(educationalEffect.getCode())
                .type(educationalEffect.getType())
                .prkLevel(educationalEffect.getPrkLevel())
                .isEngineerEffect(educationalEffect.getIsEngineerEffect())
                .isLingualEffect(educationalEffect.getIsLingualEffect())
                .category(educationalEffect.getCategory())
                .description(educationalEffect.getDescription())
                .objectState(educationalEffect.getObjectState())
                .createdBy(userMapper.mapToUserDTO(educationalEffect.getCreatedBy()))
                .createdAt(educationalEffect.getCreatedAt())
                .lastUpdatedBy(userMapper.mapToUserDTO(educationalEffect.getUpdatedBy()))
                .lastUpdatedAt(educationalEffect.getUpdatedAt())
                .build();
    }

    public List<EducationalEffect> mapToEducationalEffects(Collection<EducationalEffectDTO> educationalEffectDTOs) {
        return educationalEffectDTOs.stream()
                .map(this::mapToEducationalEffect)
                .collect(Collectors.toList());
    }

    private EducationalEffect mapToEducationalEffect(EducationalEffectDTO educationalEffectDTO) {
        if (educationalEffectDTO == null) {
            return null;
        }
        return EducationalEffect.builder()
                .id(educationalEffectDTO.getId())
                .code(educationalEffectDTO.getCode())
                .type(educationalEffectDTO.getType())
                .prkLevel(educationalEffectDTO.getPrkLevel())
                .isEngineerEffect(educationalEffectDTO.getIsEngineerEffect())
                .isLingualEffect(educationalEffectDTO.getIsLingualEffect())
                .category(educationalEffectDTO.getCategory())
                .description(educationalEffectDTO.getDescription())
                .build();
    }

    public List<EducationalEffectDTO> mapToEducationalEffectDTOs(Collection<EducationalEffect> educationalEffects) {
        return educationalEffects.stream()
                .map(this::mapToEducationalEffectDTO)
                .collect(Collectors.toList());
    }

    private EducationalEffectDTO mapToEducationalEffectDTO(EducationalEffect educationalEffect) {
        return EducationalEffectDTO.builder()
                .id(educationalEffect.getId())
                .code(educationalEffect.getCode())
                .type(educationalEffect.getType())
                .prkLevel(educationalEffect.getPrkLevel())
                .isEngineerEffect(educationalEffect.getIsEngineerEffect())
                .isLingualEffect(educationalEffect.getIsLingualEffect())
                .category(educationalEffect.getCategory())
                .description(educationalEffect.getDescription())
                .build();
    }

    public List<ResourceDTO> mapToResourceDTOs(Collection<EducationalEffect> educationalEffects) {
        return educationalEffects.stream()
                .map(this::mapToResourceDTO)
                .collect(Collectors.toList());
    }

    private ResourceDTO mapToResourceDTO(EducationalEffect educationalEffect) {
        if (educationalEffect == null) {
            return null;
        }
        return ResourceDTO.builder()
                .id(educationalEffect.getId())
                .identifier(educationalEffect.getCode())
                .uri(getEducationalEffectUrl(educationalEffect))
                .build();
    }

    private URI getEducationalEffectUrl(EducationalEffect educationalEffect) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(EducationalEffectController.EDUCATIONAL_EFFECT_RESOURCE)
                .path(IDS_PATH)
                .buildAndExpand(educationalEffect.getId())
                .toUri();
    }

    public PaginatedResultsDTO<RevisionDTO<EducationalEffectDetailsDTO>> mapToRevisionDTOs(Page<Revision<Integer, EducationalEffect>> educationalEffectPage) {
        return PaginatedResultsDTO.<RevisionDTO<EducationalEffectDetailsDTO>>builder()
                .results(mapToRevisionDTOs(educationalEffectPage.getContent()))
                .totalSize(educationalEffectPage.getTotalElements())
                .pageSize(educationalEffectPage.getSize())
                .pageNumber(educationalEffectPage.getNumber())
                .nextPage(PageUri.generatePageUri(getHistoryResourceUri(), educationalEffectPage.nextOrLastPageable()))
                .previousPage(PageUri.generatePageUri(getHistoryResourceUri(), educationalEffectPage.previousOrFirstPageable()))
                .firstPage(PageUri.generatePageUri(getHistoryResourceUri(), educationalEffectPage.getPageable().first()))
                .lastPage(PageUri.generatePageUri(getHistoryResourceUri(), PageUri.getLastPageable(educationalEffectPage)))
                .build();
    }

    private List<RevisionDTO<EducationalEffectDetailsDTO>> mapToRevisionDTOs(Collection<Revision<Integer, EducationalEffect>> revisions) {
        return revisions.stream()
                .map(this::mapToRevisionDTO)
                .collect(Collectors.toList());
    }

    private RevisionDTO<EducationalEffectDetailsDTO> mapToRevisionDTO(Revision<Integer, EducationalEffect> revision) {
        if (revision == null) {
            return null;
        }
        return RevisionDTO.<EducationalEffectDetailsDTO>builder()
                .revisionId(revision.getRequiredRevisionNumber())
                .revisionInstant(revision.getRequiredRevisionInstant())
                .revisionType(revision.getMetadata().getRevisionType())
                .entity(mapToEducationalEffectDetailsDTO(revision.getEntity()))
                .build();
    }

    private UriComponentsBuilder getHistoryResourceUri() {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(EducationalEffectController.EDUCATIONAL_EFFECT_RESOURCE)
                .path(EducationalEffectController.HISTORY);
    }

}
