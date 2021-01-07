package psi.domain.subjectcard.control;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;
import psi.domain.subjectcard.entity.SubjectCard;

import java.util.Collection;
import java.util.List;

@Repository
public interface SubjectCardRepository extends JpaRepository<SubjectCard, Long>,
        JpaSpecificationExecutor<SubjectCard>, RevisionRepository<SubjectCard, Long, Integer> {

    List<SubjectCard> findAllBySubjectCodeIn(Collection<String> subjectCodes);
    List<SubjectCard> findAllBySubjectCodeInAndIdNotIn(Collection<String> subjectCodes, Collection<Long> ids);

}
