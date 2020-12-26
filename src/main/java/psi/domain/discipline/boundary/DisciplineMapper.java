package psi.domain.discipline.boundary;

import org.springframework.stereotype.Component;
import psi.api.discipline.DisciplineDTO;
import psi.domain.discipline.entity.Discipline;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DisciplineMapper {

    public List<DisciplineDTO> mapToDisciplineDTOs(Collection<Discipline> disciplines) {
        return disciplines.stream()
                .map(this::mapToDisciplineDTO)
                .collect(Collectors.toList());
    }

    private DisciplineDTO mapToDisciplineDTO(Discipline discipline) {
        return new DisciplineDTO(discipline.getId(), discipline.getName(), discipline.getDisciplineType());
    }

}
