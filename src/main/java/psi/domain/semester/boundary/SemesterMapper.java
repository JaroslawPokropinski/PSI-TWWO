package psi.domain.semester.boundary;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import psi.api.common.ResourceDTO;
import psi.api.semester.SemesterDTO;
import psi.domain.semester.entity.Semester;
import psi.domain.studiesplan.control.StudiesPlanService;
import psi.domain.subjectcard.boundary.SubjectCardMapper;
import psi.domain.subjectcard.control.SubjectCardService;
import psi.domain.subjectcard.entity.SubjectCard;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static psi.infrastructure.rest.ResourcePaths.IDS_PATH;

@Component
@RequiredArgsConstructor
public class SemesterMapper {

    private final SubjectCardMapper subjectCardMapper;
    private final StudiesPlanService studiesPlanService;
    private final SubjectCardService subjectCardService;

    public List<SemesterDTO> mapToSemesterDTOs(Collection<Semester> semesters){
        return semesters.stream()
                .map(this::mapToSemesterDTO)
                .collect(Collectors.toList());
    }

    public SemesterDTO mapToSemesterDTO(Semester semester){
        if (semester == null){
            return null;
        }
        return SemesterDTO.builder()
                .id(semester.getId())
                .allowedEctsDeficit(semester.getAllowedEctsDeficit())
                .number(semester.getNumber())
                .studiesPlanId(semester.getStudiesPlan().getId())
                .subjectCardIds(getSubjectCardIds(semester.getSubjectCards()))
                .build();
    }

    public List<Semester> mapToSemesters(List<SemesterDTO> semesterDTOs){
        return semesterDTOs.stream()
                .map(this::mapToSemester)
                .collect(Collectors.toList());
    }

    public Semester mapToSemester(SemesterDTO semesterDTO){
        if (semesterDTO == null){
            return null;
        }
        return Semester.builder()
                .id(semesterDTO.getId())
                .allowedEctsDeficit(semesterDTO.getAllowedEctsDeficit())
                .number(semesterDTO.getNumber())
                .studiesPlan(studiesPlanService.getStudiesPlan(semesterDTO.getStudiesPlanId()))
                .subjectCards(new HashSet<>(subjectCardService.getSubjectCardsByIds(semesterDTO.getSubjectCardIds())))
                .build();
    }

    public List<ResourceDTO> mapToResourceDTOs(Collection<Semester> semesters){
        return semesters.stream()
                .map(this::mapToResourceDTO)
                .collect(Collectors.toList());
    }

    private ResourceDTO mapToResourceDTO(Semester semester){
        if (semester == null){
            return null;
        }
        return ResourceDTO.builder()
                .id(semester.getId())
                .identifier("semester id: "+ semester.getId() + " semester number: " + semester.getNumber().toString())
                .uri(getSemesterUrl(semester))
                .build();
    }

    private URI getSemesterUrl(Semester semester){
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(SemesterController.SEMESTER_RESOURCE)
                .path(IDS_PATH)
                .buildAndExpand(semester.getId())
                .toUri();
    }

    private List<Long> getSubjectCardIds(Collection<SubjectCard> subjectCards){
        return subjectCards.stream()
                .map(SubjectCard::getId)
                .collect(Collectors.toList());
    }


}
