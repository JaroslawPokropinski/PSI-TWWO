package psi.domain.disciplinetype.boundary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import psi.api.common.ResourceDTO;
import psi.domain.disciplinetype.entity.DisciplineType;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DisciplineTypeMapper {

    public List<ResourceDTO> mapToResourceDTOs(Collection<DisciplineType> disciplineTypes){
        return disciplineTypes.stream()
                .map(this::mapToResourceDTO)
                .collect(Collectors.toList());
    }

    private ResourceDTO mapToResourceDTO(DisciplineType disciplineType){
        if (disciplineType == null){
            return null;
        }
        return ResourceDTO.builder()
                .id(disciplineType.getId())
                .identifier(disciplineType.getName())
                .uri(getDisciplineTypeUrl(disciplineType))
                .build();
    }

    private URI getDisciplineTypeUrl(DisciplineType disciplineType){
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(DisciplineTypeController.DISCIPLINE_TYPE_RESOURCE)
                .buildAndExpand(disciplineType.getId())
                .toUri();
    }

}
