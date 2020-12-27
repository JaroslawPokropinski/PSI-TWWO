package psi.domain.subjectcard.boundary;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import psi.api.common.ResourceDTO;
import psi.api.common.SearchResultDTO;
import psi.api.subjectcard.SubjectCardDTO;
import psi.api.subjectcard.SubjectCardDetailsDTO;
import psi.domain.subjectcard.entity.SubjectCard;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SubjectCardMapper {

    public SearchResultDTO<SubjectCardDetailsDTO> mapToSearchResultDTO(Page<SubjectCard> subjectCardPage, String query) {
        return null;
    }

    public List<SubjectCardDetailsDTO> mapToSubjectCardDetailsDTOs(Collection<SubjectCard> subjectCards) {
        return Collections.emptyList();
    }

    private SubjectCardDetailsDTO mapToSubjectCardDetailsDTO(SubjectCard subjectCard) {
        return null;
    }

    public List<SubjectCard> mapToSubjectCards(Collection<SubjectCardDTO> subjectCardDTOs) {
        return Collections.emptyList();
    }

    public List<ResourceDTO> mapToResourceDTOs(Collection<SubjectCard> subjectCards) {
        return Collections.emptyList();
    }

}
