package psi.domain.subjectcard.control;

import io.github.perplexhub.rsql.RSQLJPASupport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psi.domain.subjectcard.entity.SubjectCard;
import psi.domain.subjectcard.boundary.SubjectCardRSQLMapping;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SubjectCardService {

    private final SubjectCardRepository subjectCardRepository;

    public Page<SubjectCard> searchSubjectCardsByRSQL(String query, Pageable pageable) {
        return subjectCardRepository.findAll(RSQLJPASupport.toSpecification(query, SubjectCardRSQLMapping.RSQL_TO_JPA_ATTRIBUTE_MAPPING), pageable);
    }

    public List<SubjectCard> getSubjectCardsByIds(Collection<Long> ids) {
        return subjectCardRepository.findAllById(ids);
    }

    public List<SubjectCard> createSubjectCards(Collection<SubjectCard> subjectCards) {
        return Collections.emptyList();
    }

    public void updateSubjectCards(Collection<SubjectCard> updatedSubjectCards) {
    }

    public void deleteSubjectCards(Collection<Long> ids) {

    }

}
