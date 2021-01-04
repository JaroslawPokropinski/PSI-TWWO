package psi.domain.studiesprogram.boundary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import psi.api.studiesprogram.StudiesProgramDTO;
import psi.domain.discipline.control.DisciplineService;
import psi.domain.fieldofstudy.control.FieldOfStudyService;
import psi.domain.studiesprogram.entity.StudiesProgram;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StudiesProgramMapper {

    private FieldOfStudyService fieldOfStudyService;
    private DisciplineService disciplineService;

    public List<StudiesProgramDTO> mapToStudiesProgramsDTOs(Collection<StudiesProgram> studiesPrograms){
        return studiesPrograms.stream()
        .map(this::mapToStudiesProgramsDTO)
        .collect(Collectors.toList());
    }

    private StudiesProgramDTO mapToStudiesProgramsDTO(StudiesProgram studiesProgram){
        if (studiesProgram == null){
            return null;
        }
        return StudiesProgramDTO.builder()
                .id(studiesProgram.getId())
                .code(studiesProgram.getCode())
                .build();
    }

}
