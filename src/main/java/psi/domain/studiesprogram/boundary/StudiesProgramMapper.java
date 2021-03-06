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
import psi.domain.discipline.control.DisciplineService;
import psi.domain.discipline.entity.Discipline;
import psi.domain.educationaleffect.boundary.EducationalEffectMapper;
import psi.domain.educationaleffect.control.EducationalEffectService;
import psi.domain.educationaleffect.entity.EducationalEffect;
import psi.domain.fieldofstudy.control.FieldOfStudyService;
import psi.domain.simpleattribute.boundary.SimpleAttributeMapper;
import psi.domain.studiesprogram.entity.StudiesProgram;
import psi.domain.subjectcard.boundary.SubjectCardController;
import psi.infrastructure.rest.PageUri;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static psi.infrastructure.rest.ResourcePaths.IDS_PATH;

@Component
@RequiredArgsConstructor
public class StudiesProgramMapper {

    private final FieldOfStudyService fieldOfStudyService;
    private final DisciplineService disciplineService;
    private final EducationalEffectService educationalEffectService;
    private final SimpleAttributeMapper simpleAttributeMapper;
    private final EducationalEffectMapper educationalEffectMapper;

    public PaginatedResultsDTO<StudiesProgramDTO> mapToSearchResultDTO(Page<StudiesProgram> studiesProgramPage, String query ){
        return PaginatedResultsDTO.<StudiesProgramDTO>builder()
                .results(mapToStudiesProgramsDTOs(studiesProgramPage.getContent()))
                .totalSize(studiesProgramPage.getTotalElements())
                .pageSize(studiesProgramPage.getSize())
                .pageNumber(studiesProgramPage.getNumber())
                .nextPage(PageUri.generatePageUri(getSearchResourceUri(query), studiesProgramPage.nextOrLastPageable()))
                .previousPage(PageUri.generatePageUri(getSearchResourceUri(query), studiesProgramPage.previousOrFirstPageable()))
                .firstPage(PageUri.generatePageUri(getSearchResourceUri(query), studiesProgramPage.getPageable().first()))
                .lastPage(PageUri.generatePageUri(getSearchResourceUri(query), PageUri.getLastPageable(studiesProgramPage)))
                .build();
    }

    private UriComponentsBuilder getSearchResourceUri(String query){
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(StudiesProgramController.STUDIES_PROGRAM_RESOURCE)
                .path(StudiesProgramController.SEARCH_RESOURCE)
                .queryParam("query", query);
    }

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
                .inEffectSince(studiesProgramDTO.getInEffectSince())
                .languageOfStudies(studiesProgramDTO.getLanguageOfStudies())
                .graduateProfile(studiesProgramDTO.getGraduateProfile())
                .possibilityOfContinuingStudies(studiesProgramDTO.getPossibilityOfContinuingStudies())
                .connectionWithMissionAndDevelopmentStrategy(studiesProgramDTO.getConnectionWithMissionAndDevelopmentStrategy())
                .mainDiscipline(disciplineService.getDisciplineById(studiesProgramDTO.getMainDisciplineId()))
                .disciplines(new HashSet<>(disciplineService.getDisciplinesByIds(studiesProgramDTO.getDisciplinesIds())))
                .educationalEffects(new HashSet<>(educationalEffectService.getEducationalEffectsByIds(studiesProgramDTO.getEducationalEffects())))
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
                .objectState(studiesProgram.getObjectState())
                .inEffectSince(studiesProgram.getInEffectSince())
                .educationalEffects(getEducationalEffectsIds(studiesProgram.getEducationalEffects()))
                .build();
    }

    private List<Long> getDisciplinesIds(Collection<Discipline> studiesPrograms) {
        return studiesPrograms.stream()
                .map(Discipline::getId)
                .collect(Collectors.toList());
    }

    private List<Long> getEducationalEffectsIds(Collection<EducationalEffect> studiesPrograms) {
        return studiesPrograms.stream()
                .map(EducationalEffect::getId)
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