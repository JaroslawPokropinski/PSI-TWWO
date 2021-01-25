package psi.domain.semester.control;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;
import psi.domain.semester.entity.Semester;

import java.util.Collection;
import java.util.List;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long>,
        JpaSpecificationExecutor<Semester>, RevisionRepository<Semester, Long, Integer> {

    List<Semester> findAllByStudiesPlanIdIn(Collection<Long> studiesPlanIds);

}
