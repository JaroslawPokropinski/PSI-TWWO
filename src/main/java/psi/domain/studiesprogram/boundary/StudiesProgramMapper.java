package psi.domain.studiesprogram.boundary;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import psi.api.common.PaginatedResultsDTO;
import psi.api.common.ResourceDTO;
import psi.api.revision.RevisionDTO;
import psi.api.studiesprogram.StudiesProgramDTO;
import psi.api.subjectcard.SubjectCardDetailsDTO;
import psi.domain.discipline.boundary.DisciplineMapper;
import psi.domain.discipline.control.DisciplineService;
import psi.domain.discipline.entity.Discipline;
import psi.domain.fieldofstudy.boundary.FieldOfStudyMapper;
import psi.domain.fieldofstudy.control.FieldOfStudyService;
import psi.domain.fieldofstudy.entity.FieldOfStudy;
import psi.domain.simpleattribute.boundary.SimpleAttributeMapper;
import psi.domain.simpleattribute.entity.SimpleAttribute;
import psi.domain.studiesprogram.entity.StudiesProgram;
import psi.domain.subjectcard.boundary.SubjectCardController;
import psi.domain.subjectcard.entity.SubjectCard;
import psi.infrastructure.rest.PageUri;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static psi.infrastructure.rest.ResourcePaths.IDS_PATH;

@Component
@RequiredArgsConstructor
public class StudiesProgramMapper {

    private FieldOfStudyService fieldOfStudyService;
    private DisciplineService disciplineService;

    private FieldOfStudyMapper fieldOfStudyMapper;
    private DisciplineMapper disciplineMapper;
    private SimpleAttributeMapper simpleAttributeMapper;

    public List<StudiesProgramDTO> mapToStudiesProgramsDTOs(Collection<StudiesProgram> studiesPrograms){
        return studiesPrograms.stream()
        .map(this::mapToStudiesProgramsDTO)
        .collect(Collectors.toList());
    }

    public List<StudiesProgram> mapToStudiesPrograms(Collection<StudiesProgramDTO> studiesProgramsDTOs){
        return studiesProgramsDTOs.stream()
                .map(this::mapToStudiesProgram)
                .collect(Collectors.toList());
    }

    public List<ResourceDTO> mapToResourceDTOs(Collection<StudiesProgram> subjectCards){
        return subjectCards.stream()
                .map(this::mapToResourceDTO)
                .collect(Collectors.toList());
    }

    public PaginatedResultsDTO<RevisionDTO<StudiesProgramDTO>> mapToRevisionDTOs(Page<Revision<Integer, StudiesProgram>> studiesProgramsHistoryPage) {
        return PaginatedResultsDTO.<RevisionDTO<StudiesProgramDTO>>builder()
                .results(mapToRevisionDTOs(studiesProgramsHistoryPage.getContent()))
                .totalSize(studiesProgramsHistoryPage.getTotalElements())
                .pageSize(studiesProgramsHistoryPage.getSize())
                .pageNumber(studiesProgramsHistoryPage.getNumber())
                .nextPage(PageUri.generatePageUri(getHistoryResourceUri(), studiesProgramsHistoryPage.nextOrLastPageable()))
                .previousPage(PageUri.generatePageUri(getHistoryResourceUri(), studiesProgramsHistoryPage.previousOrFirstPageable()))
                .firstPage(PageUri.generatePageUri(getHistoryResourceUri(), studiesProgramsHistoryPage.getPageable().first()))
                .lastPage(PageUri.generatePageUri(getHistoryResourceUri(), PageUri.getLastPageable(studiesProgramsHistoryPage)))
                .build();
    }

    private StudiesProgram mapToStudiesProgram(StudiesProgramDTO studiesProgramDTO){
        if ( studiesProgramDTO == null){
            return null;
        }
        return StudiesProgram.builder()
                .id(studiesProgramDTO.getId())
                .code(studiesProgramDTO.getCode())
                .fieldOfStudy(fieldOfStudyService.getFieldOfStudyById(studiesProgramDTO.getFieldOfStudyId()))
                .studiesLevel(studiesProgramDTO.getStudiesLevel())
                .studiesForm(studiesProgramDTO.getStudiesForm())
                .studiesProfile(studiesProgramDTO.getStudiesProfile())
                .numberOfSemesters(studiesProgramDTO.getNumberOfSemesters())
                .totalNumberOfEctsPoints(studiesProgramDTO.getTotalNumberOfEctsPoints())
                .totalNumberOfHours(studiesProgramDTO.getTotalNumberOfHours())
                .prerequisites(simpleAttributeMapper.mapToSimpleAttributes(studiesProgramDTO.getPrerequisites()))
                .degreeTitle(studiesProgramDTO.getDegreeTitle())
                .languageOfStudies(studiesProgramDTO.getLanguageOfStudies())
                .graduateProfile(studiesProgramDTO.getGraduateProfile())
                .possibilityOfContinuingStudies(studiesProgramDTO.getPossibilityOfContinuingStudies())
                .connectionWithMissionAndDevelopmentStrategy(studiesProgramDTO.getConnectionWithMissionAndDevelopmentStrategy())
                .mainDiscipline(disciplineService.getDisciplineById(studiesProgramDTO.getMainDisciplineId()))
                .disciplines(new HashSet<Discipline>(disciplineService.getDisciplinesByIds(studiesProgramDTO.getDisciplinesIds())))
                .build();
    }

    private StudiesProgramDTO mapToStudiesProgramsDTO(StudiesProgram studiesProgram){
        if (studiesProgram == null){
            return null;
        }
        return StudiesProgramDTO.builder()
                .id(studiesProgram.getId())
                .code(studiesProgram.getCode())
                .fieldOfStudyId(studiesProgram.getFieldOfStudy().getId())
                .studiesLevel(studiesProgram.getStudiesLevel())
                .studiesForm(studiesProgram.getStudiesForm())
                .studiesProfile(studiesProgram.getStudiesProfile())
                .numberOfSemesters(studiesProgram.getNumberOfSemesters())
                .totalNumberOfEctsPoints(studiesProgram.getTotalNumberOfEctsPoints())
                .totalNumberOfHours(studiesProgram.getTotalNumberOfHours())
                .prerequisites(simpleAttributeMapper.mapToStrings(studiesProgram.getPrerequisites()))
                .degreeTitle(studiesProgram.getDegreeTitle())
                .languageOfStudies(studiesProgram.getLanguageOfStudies())
                .graduateProfile(studiesProgram.getGraduateProfile())
                .possibilityOfContinuingStudies(studiesProgram.getPossibilityOfContinuingStudies())
                .connectionWithMissionAndDevelopmentStrategy(studiesProgram.getConnectionWithMissionAndDevelopmentStrategy())
                .mainDisciplineId(studiesProgram.getMainDiscipline().getId())
                .disciplinesIds(getDisciplinesIds(studiesProgram.getDisciplines()))
                .build();
    }

    private List<Long> getDisciplinesIds(Collection<Discipline> studiesPrograms) {
        return studiesPrograms.stream()
                .map(Discipline::getId)
                .collect(Collectors.toList());
    }

    private ResourceDTO mapToResourceDTO(StudiesProgram studiesProgram){
        if ( studiesProgram == null){
            return null;
        }
        return ResourceDTO.builder()
                .id(studiesProgram.getId())
                .identifier(studiesProgram.getCode())
                .uri(getStudiesProgramUrl(studiesProgram))
                .build();
    }

    private URI getStudiesProgramUrl(StudiesProgram studiesProgram){
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(SubjectCardController.SUBJECT_CARD_RESOURCE)
                .path(IDS_PATH)
                .buildAndExpand(studiesProgram.getId())
                .toUri();
    }

    private List<RevisionDTO<StudiesProgramDTO>> mapToRevisionDTOs(Collection<Revision<Integer, StudiesProgram>> revisions) {
        return revisions.stream()
                .map(this::mapToRevisionDTO)
                .collect(Collectors.toList());
    }

    private RevisionDTO<StudiesProgramDTO> mapToRevisionDTO(Revision<Integer, StudiesProgram> revision) {
        if (revision == null) {
            return null;
        }
        return RevisionDTO.<StudiesProgramDTO>builder()
                .revisionId(revision.getRequiredRevisionNumber())
                .revisionInstant(revision.getRequiredRevisionInstant())
                .revisionType(revision.getMetadata().getRevisionType())
                .entity(mapToStudiesProgramsDTO(revision.getEntity()))
                .build();
    }



    private UriComponentsBuilder getHistoryResourceUri() {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(StudiesProgramController.STUDIES_PROGRAM_RESOURCE)
                .path(StudiesProgramController.HISTORY);
    }



}
