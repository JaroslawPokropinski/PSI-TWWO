package psi.domain.fieldofstudy.control;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import psi.domain.fieldofstudy.entity.FieldOfStudy;

@Repository
public interface FieldOfStudyRepository extends JpaRepository<FieldOfStudy, Long> {
}
