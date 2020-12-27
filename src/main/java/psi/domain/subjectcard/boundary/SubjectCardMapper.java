package psi.domain.subjectcard.boundary;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import psi.api.common.ResourceDTO;
import psi.api.common.SearchResultDTO;
import psi.api.subjectcard.SubjectCardDTO;
import psi.api.subjectcard.SubjectCardDetailsDTO;
import psi.domain.educationaleffect.boundary.EducationalEffectMapper;
import psi.domain.educationaleffect.control.EducationalEffectService;
import psi.domain.fieldofstudy.boundary.FieldOfStudyMapper;
import psi.domain.fieldofstudy.control.FieldOfStudyService;
import psi.domain.organisationalunit.boundary.OrganisationalUnitMapper;
import psi.domain.organisationalunit.control.OrganisationalUnitService;
import psi.domain.simpleattribute.boundary.SimpleAttributeMapper;
import psi.domain.subjectcard.entity.SubjectCard;
import psi.domain.user.boundary.UserMapper;
import psi.domain.user.control.UserService;
import psi.infrastructure.rest.PageUri;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static psi.infrastructure.rest.ResourcePaths.IDS_PATH;

@Component
@RequiredArgsConstructor
public class SubjectCardMapper {

    private final FieldOfStudyService fieldOfStudyService;
    private final OrganisationalUnitService organisationalUnitService;
    private final EducationalEffectService educationalEffectService;
    private final UserService userService;

    private final FieldOfStudyMapper fieldOfStudyMapper;
    private final OrganisationalUnitMapper organisationalUnitMapper;
    private final SimpleAttributeMapper simpleAttributeMapper;
    private final UserMapper userMapper;
    private final SubjectClassMapper subjectClassMapper;
    private final LiteratureMapper literatureMapper;
    private final EducationalEffectMapper educationalEffectMapper;

    public SearchResultDTO<SubjectCardDetailsDTO> mapToSearchResultDTO(Page<SubjectCard> subjectCardPage, String query) {
        return SearchResultDTO.<SubjectCardDetailsDTO>builder()
                .results(mapToSubjectCardDetailsDTOs(subjectCardPage.getContent()))
                .totalSize(subjectCardPage.getTotalElements())
                .pageSize(subjectCardPage.getSize())
                .pageNumber(subjectCardPage.getNumber())
                .nextPage(PageUri.generatePageUri(getSearchResourceUri(query), subjectCardPage.nextOrLastPageable()))
                .previousPage(PageUri.generatePageUri(getSearchResourceUri(query), subjectCardPage.previousOrFirstPageable()))
                .firstPage(PageUri.generatePageUri(getSearchResourceUri(query), subjectCardPage.getPageable().first()))
                .lastPage(PageUri.generatePageUri(getSearchResourceUri(query), PageUri.getLastPageable(subjectCardPage)))
                .build();
    }

    private UriComponentsBuilder getSearchResourceUri(String query) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(SubjectCardController.SUBJECT_CARD_RESOURCE)
                .path(SubjectCardController.SEARCH_RESOURCE)
                .queryParam("query", query);
    }

    public List<SubjectCardDetailsDTO> mapToSubjectCardDetailsDTOs(Collection<SubjectCard> subjectCards) {
        return subjectCards.stream()
                .map(this::mapToSubjectCardDetailsDTO)
                .collect(Collectors.toList());
    }

    private SubjectCardDetailsDTO mapToSubjectCardDetailsDTO(SubjectCard subjectCard) {
        if (subjectCard == null) {
            return null;
        }
        return SubjectCardDetailsDTO.builder()
                .id(subjectCard.getId())
                .subjectName(subjectCard.getSubjectName())
                .subjectNameInEnglish(subjectCard.getSubjectNameInEnglish())
                .fieldOfStudy(fieldOfStudyMapper.mapToFieldOfStudyDTO(subjectCard.getMainFieldOfStudy()))
                .specialization(subjectCard.getSpecialization())
                .studiesLevel(subjectCard.getStudiesLevel())
                .studiesForm(subjectCard.getStudiesForm())
                .subjectType(subjectCard.getSubjectType())
                .subjectCode(subjectCard.getSubjectCode())
                .isGroupOfCourses(subjectCard.getIsGroupOfCourses())
                .organisationalUnit(organisationalUnitMapper.mapToOrganisationalUnitDTO(subjectCard.getOrganisationalUnit()))
                .prerequisites(simpleAttributeMapper.mapToStrings(subjectCard.getPrerequisites()))
                .subjectObjectives(simpleAttributeMapper.mapToStrings(subjectCard.getSubjectObjectives()))
                .primaryLiterature(literatureMapper.mapToPrimaryLiteratureStrings(subjectCard.getLiterature()))
                .secondaryLiterature(literatureMapper.mapToSecondaryLiteratureStrings(subjectCard.getLiterature()))
                .usedTeachingTools(simpleAttributeMapper.mapToStrings(subjectCard.getUsedTeachingTools()))
                .supervisor(userMapper.mapToUserDTO(subjectCard.getSupervisor()))
                .subjectClasses(subjectClassMapper.mapToSubjectClassesDTOs(subjectCard.getSubjectClasses()))
                .educationalEffects(educationalEffectMapper.mapToEducationalEffectDTOs(subjectCard.getEducationalEffects()))
                .build();
    }

    public List<SubjectCard> mapToSubjectCards(Collection<SubjectCardDTO> subjectCardDTOs) {
        return subjectCardDTOs.stream()
                .map(this::mapToSubjectCard)
                .collect(Collectors.toList());
    }

    private SubjectCard mapToSubjectCard(SubjectCardDTO subjectCardDTO) {
        if (subjectCardDTO == null) {
            return null;
        }
        return SubjectCard.builder()
                .id(subjectCardDTO.getId())
                .subjectName(subjectCardDTO.getSubjectName())
                .subjectNameInEnglish(subjectCardDTO.getSubjectNameInEnglish())
                .mainFieldOfStudy(fieldOfStudyService.getFieldOfStudyById(subjectCardDTO.getFieldOfStudy()))
                .specialization(subjectCardDTO.getSpecialization())
                .studiesLevel(subjectCardDTO.getStudiesLevel())
                .studiesForm(subjectCardDTO.getStudiesForm())
                .subjectType(subjectCardDTO.getSubjectType())
                .subjectCode(subjectCardDTO.getCode())
                .isGroupOfCourses(subjectCardDTO.getIsGroupOfCourses())
                .organisationalUnit(organisationalUnitService.getOrganisationalUnitById(subjectCardDTO.getOrganisationalUnit()))
                .prerequisites(simpleAttributeMapper.mapToSimpleAttributes(subjectCardDTO.getPrerequisites()))
                .subjectObjectives(simpleAttributeMapper.mapToSimpleAttributes(subjectCardDTO.getSubjectObjectives()))
                .literature(new HashSet<>(literatureMapper.mapToLiterature(subjectCardDTO)))
                .usedTeachingTools(simpleAttributeMapper.mapToSimpleAttributes(subjectCardDTO.getUsedTeachingTools()))
                .supervisor(userService.getExistingUser(subjectCardDTO.getSupervisor()))
                .subjectClasses(new HashSet<>(subjectClassMapper.mapToSubjectClasses(subjectCardDTO.getSubjectClasses())))
                .educationalEffects(new HashSet<>(educationalEffectService.getEducationalEffectsByIds(subjectCardDTO.getEducationalEffects())))
                .build();
    }

    public List<ResourceDTO> mapToResourceDTOs(Collection<SubjectCard> subjectCards) {
        return subjectCards.stream()
                .map(this::mapToResourceDTO)
                .collect(Collectors.toList());
    }

    private ResourceDTO mapToResourceDTO(SubjectCard subjectCard) {
        if (subjectCard == null) {
            return null;
        }
        return ResourceDTO.builder()
                .id(subjectCard.getId())
                .identifier(subjectCard.getSubjectCode())
                .uri(getSubjectCardUrl(subjectCard))
                .build();
    }

    private URI getSubjectCardUrl(SubjectCard subjectCard) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(SubjectCardController.SUBJECT_CARD_RESOURCE)
                .path(IDS_PATH)
                .buildAndExpand(subjectCard.getId())
                .toUri();
    }

}
