package psi.domain.fieldofstudy.boundary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import psi.api.common.ResourceDTO;
import psi.api.fieldofstudy.FieldOfStudyDTO;
import psi.domain.fieldofstudy.entity.FieldOfStudy;
import psi.domain.organisationalunit.control.OrganisationalUnitService;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static psi.infrastructure.rest.ResourcePaths.IDS_PATH;

@Component
@RequiredArgsConstructor
public class FieldOfStudyMapper {

    private final OrganisationalUnitService organisationalUnitService;

    public List<FieldOfStudyDTO> mapToFiledOfStudyDTOs(Collection<FieldOfStudy> fieldOfStudies) {
        return fieldOfStudies.stream()
                .map(this::mapToFieldOfStudyDTO)
                .collect(Collectors.toList());
    }

    public FieldOfStudyDTO mapToFieldOfStudyDTO(FieldOfStudy fieldOfStudy) {
        return new FieldOfStudyDTO(fieldOfStudy.getId(), fieldOfStudy.getName(), fieldOfStudy.getOrganisationalUnit().getId());
    }

    public List<FieldOfStudy> mapToFiledOfStudies(Collection<FieldOfStudyDTO> fieldOfStudyDTOs) {
        return fieldOfStudyDTOs.stream()
                .map(this::mapToFiledOfStudy)
                .collect(Collectors.toList());
    }

    private FieldOfStudy mapToFiledOfStudy(FieldOfStudyDTO fieldOfStudyDTO) {
        return FieldOfStudy.builder()
                .id(fieldOfStudyDTO.getId())
                .name(fieldOfStudyDTO.getName())
                .organisationalUnit(organisationalUnitService.getOrganisationalUnitById(fieldOfStudyDTO.getFaculty()))
                .build();
    }

    public List<ResourceDTO> mapToResourceDTOs(Collection<FieldOfStudy> fieldOfStudies) {
        return fieldOfStudies.stream()
                .map(this::mapToResourceDTO)
                .collect(Collectors.toList());
    }

    private ResourceDTO mapToResourceDTO(FieldOfStudy fieldOfStudy) {
        if (fieldOfStudy == null) {
            return null;
        }
        return ResourceDTO.builder()
                .id(fieldOfStudy.getId())
                .identifier(fieldOfStudy.getName())
                .uri(getFieldOfStudyUrl(fieldOfStudy))
                .build();
    }

    private URI getFieldOfStudyUrl(FieldOfStudy fieldOfStudy) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(FieldOfStudyController.FILED_OF_STUDY_RESOURCE)
                .path(IDS_PATH)
                .buildAndExpand(fieldOfStudy.getId())
                .toUri();
    }

}
