package psi.domain.discipline.boundary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import psi.api.common.ResourceDTO;
import psi.api.discipline.DisciplineDTO;
import psi.api.discipline.DisciplineDetailsDTO;
import psi.domain.discipline.entity.Discipline;
import psi.domain.disciplinetype.control.DisciplineTypeService;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static psi.infrastructure.rest.ResourcePaths.IDS_PATH;

@Component
@RequiredArgsConstructor
public class DisciplineMapper {

    private final DisciplineTypeService disciplineTypeService;

    public List<DisciplineDetailsDTO> mapToDisciplineDTOs(Collection<Discipline> disciplines) {
        return disciplines.stream()
                .map(this::mapToDisciplineDTO)
                .collect(Collectors.toList());
    }

    public List<Discipline> mapToDisciplines(Collection<DisciplineDTO> disciplineDetailsDTOS){
        return disciplineDetailsDTOS.stream()
                .map(this::mapToDiscipline)
                .collect(Collectors.toList());
    }

    public List<ResourceDTO> mapToResourceDTOs(Collection<Discipline> disciplines){
        return disciplines.stream()
                .map(this::mapToResourceDTO)
                .collect(Collectors.toList());
    }

    private DisciplineDetailsDTO mapToDisciplineDTO(Discipline discipline) {
        return new DisciplineDetailsDTO(discipline.getId(), discipline.getName(), discipline.getDisciplineType());
    }

    private Discipline mapToDiscipline(DisciplineDTO disciplineDTO){
        if (disciplineDTO == null){
            return null;
        }
        return Discipline.builder()
                .id(disciplineDTO.getId())
                .name(disciplineDTO.getName())
                .disciplineType(disciplineTypeService.getDisciplineTypeById(disciplineDTO.getDisciplineType()))
                .build();
    }

    private ResourceDTO mapToResourceDTO(Discipline discipline){
        if (discipline == null){
            return null;
        }
        return ResourceDTO.builder()
                .id(discipline.getId())
                .identifier(discipline.getName())
                .uri(getDisciplineUrl(discipline))
                .build();
    }

    private URI getDisciplineUrl(Discipline discipline){
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(DisciplineController.DISCIPLINE_RESOURCE)
                .path(IDS_PATH)
                .buildAndExpand(discipline.getId())
                .toUri();
    }

}
