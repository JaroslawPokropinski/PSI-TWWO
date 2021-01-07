package psi.domain.studiesplan.boundary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import psi.api.studiesplan.StudiesPlanDTO;
import psi.domain.studiesplan.entity.StudiesPlan;
import psi.domain.studiesprogram.boundary.StudiesProgramMapper;
import psi.domain.studiesprogram.control.StudiesProgramService;
import psi.domain.studiesprogram.entity.StudiesProgram;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

}
