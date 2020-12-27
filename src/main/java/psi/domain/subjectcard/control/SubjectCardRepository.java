package psi.domain.subjectcard.control;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import psi.domain.subjectcard.entity.SubjectCard;

@Repository
public interface SubjectCardRepository extends JpaRepository<SubjectCard, Long>, JpaSpecificationExecutor<SubjectCard> {
}
