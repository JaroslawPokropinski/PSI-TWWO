package psi.domain.subjectcard.boundary;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import psi.api.common.ResourceDTO;
import psi.api.common.ResponseDTO;
import psi.api.common.PaginatedResultsDTO;
import psi.api.common.StatusDTO;
import psi.api.revision.RevisionDTO;
import psi.api.subjectcard.SubjectCardDTO;
import psi.api.subjectcard.SubjectCardDetailsDTO;
import psi.domain.auditedobject.entity.ObjectState;
import psi.domain.document.Document;
import psi.domain.document.DocumentGenerator;
import psi.domain.subjectcard.entity.SubjectCard;
import psi.domain.subjectcard.control.SubjectCardService;
import psi.infrastructure.mediatype.MediaTypeResolver;
import psi.infrastructure.security.UserInfo;
import psi.infrastructure.security.annotation.HasCommissionMemberRole;
import psi.infrastructure.security.annotation.HasSupervisorOrCommissionMemberRole;
import psi.infrastructure.security.annotation.LoggedUser;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static psi.infrastructure.rest.ResourcePaths.ID;
import static psi.infrastructure.rest.ResourcePaths.IDS;
import static psi.infrastructure.rest.ResourcePaths.IDS_PATH;
import static psi.infrastructure.rest.ResourcePaths.ID_PATH;

@Api(tags = "Subject Card")
@RestController
@RequestMapping(SubjectCardController.SUBJECT_CARD_RESOURCE)
@RequiredArgsConstructor
public class SubjectCardController {

    public static final String SUBJECT_CARD_RESOURCE = "/api/subject-card";
    public static final String SEARCH_RESOURCE = "/search";
    public static final String HISTORY = "/history";
    public static final String STATUS = "/status";

    private final SubjectCardService subjectCardService;
    private final SubjectCardMapper subjectCardMapper;
    private final DocumentGenerator<SubjectCard> subjectCardPdfDocumentGenerator;
    private final MediaTypeResolver mediaTypeResolver;

    @ApiOperation(value = "${api.subject-cards.searchSubjectCards.value}", notes = "${api.subject-cards.searchSubjectCards.notes}")
    @GetMapping(SEARCH_RESOURCE)
    public PaginatedResultsDTO<SubjectCardDetailsDTO> searchSubjectCards(@RequestParam String query, @Valid Pageable pageable) {
        Page<SubjectCard> subjectCardPage = subjectCardService.searchSubjectCardsByRSQL(query, pageable);
        return subjectCardMapper.mapToSearchResultDTO(subjectCardPage, query);
    }

    @ApiOperation(value = "${api.subject-cards.getSubjectCards.value}", notes = "${api.subject-cards.getSubjectCards.notes}")
    @GetMapping(IDS_PATH)
    public List<SubjectCardDetailsDTO> getSubjectCards(@PathVariable(IDS) List<Long> ids) {
        List<SubjectCard> foundSubjectCards = subjectCardService.getSubjectCardsByIds(ids);
        return subjectCardMapper.mapToSubjectCardDetailsDTOs(foundSubjectCards);
    }

    @HasSupervisorOrCommissionMemberRole
    @ApiOperation(value = "${api.subject-cards.createSubjectCards.value}", notes = "${api.subject-cards.createSubjectCards.notes}")
    @PostMapping
    public List<ResourceDTO> createSubjectCards(@Valid @RequestBody List<SubjectCardDTO> subjectCardDTOs) {
        List<SubjectCard> subjectCards = subjectCardMapper.mapToSubjectCards(subjectCardDTOs);
        List<SubjectCard> createdSubjectCards = subjectCardService.createSubjectCards(subjectCards);
        return subjectCardMapper.mapToResourceDTOs(createdSubjectCards);
    }

    @HasSupervisorOrCommissionMemberRole
    @ApiOperation(value = "${api.subject-cards.updateSubjectCards.value}", notes = "${api.subject-cards.updateSubjectCards.notes}")
    @PutMapping
    public List<ResourceDTO> updateSubjectCards(@Valid @RequestBody List<SubjectCardDTO> subjectCardDTOs, @ApiIgnore @LoggedUser UserInfo userInfo) {
        List<SubjectCard> subjectCards = subjectCardMapper.mapToSubjectCards(subjectCardDTOs);
        subjectCardService.updateSubjectCards(subjectCards, userInfo.getId());
        return subjectCardMapper.mapToResourceDTOs(subjectCards);
    }

    @HasSupervisorOrCommissionMemberRole
    @ApiOperation(value = "${api.subject-cards.deleteSubjectCards.value}", notes = "${api.subject-cards.deleteSubjectCards.notes}")
    @DeleteMapping(IDS_PATH)
    public ResponseDTO<Boolean> deleteSubjectCards(@PathVariable(IDS) List<Long> ids, @ApiIgnore @LoggedUser UserInfo userInfo) {
        subjectCardService.deleteSubjectCards(ids, userInfo.getId());
        return new ResponseDTO<>(true, "Subject cards deleted successfully");
    }

    @ApiOperation(value = "${api.subject-cards.downloadSubjectCardFile.value}", notes = "${api.subject-cards.downloadSubjectCardFile.notes}")
    @GetMapping("/download" + ID_PATH)
    public ResponseEntity<Resource> downloadSubjectCardFile(@PathVariable(ID) Long id) {
        SubjectCard subjectCard = subjectCardService.getSubjectCardById(id);
        Document document = subjectCardPdfDocumentGenerator.generateDocument(subjectCard);
        return ResponseEntity.ok()
                .contentType(mediaTypeResolver.getMediaTypeForFile(document.getName()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getName() + "\"")
                .body(document.getResource());
    }

    @ApiOperation(value = "${api.subject-cards.getSubjectCardHistory.value}", notes = "${api.subject-cards.getSubjectCardHistory.notes}")
    @GetMapping(HISTORY + ID_PATH)
    public PaginatedResultsDTO<RevisionDTO<SubjectCardDetailsDTO>> getSubjectCardHistory(@PathVariable(ID) Long id, Pageable pageable) {
        Page<Revision<Integer, SubjectCard>> subjectCardHistoryPage = subjectCardService.getSubjectCardHistory(id, pageable);
        return subjectCardMapper.mapToRevisionDTOs(subjectCardHistoryPage);
    }

    @HasCommissionMemberRole
    @ApiOperation(value = "${api.subject-cards.changeStatus.value}", notes = "${api.subject-cards.changeStatus.notes}")
    @PatchMapping(STATUS + IDS_PATH)
    public ResponseDTO<Boolean> changeStatus(@PathVariable(IDS) Collection<Long> ids, @Valid @RequestBody StatusDTO statusDTO, @ApiIgnore UserInfo userInfo) {
        subjectCardService.changeSubjectCardState(ids, ObjectState.valueOf(statusDTO.getStatus().name()), userInfo.getId());
        return new ResponseDTO<>(true, "Status changed successfully");
    }

}
