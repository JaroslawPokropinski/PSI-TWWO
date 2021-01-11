package psi.domain.studiesplan.control;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;
import psi.domain.studiesplan.entity.StudiesPlan;

import java.util.Collection;
import java.util.List;

@Repository
public interface StudiesPlanRepository extends JpaRepository<StudiesPlan, Long>,
        JpaSpecificationExecutor<StudiesPlan>, RevisionRepository<StudiesPlan, Long, Integer> {

    List<StudiesPlan> findAllByCodeIn(Collection<String> code);
    List<StudiesPlan> findAllByCodeInAndIdNotIn(Collection<String> studiesPlansCodes, Collection<Long> ids);

}
