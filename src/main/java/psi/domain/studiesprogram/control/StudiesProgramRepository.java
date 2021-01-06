package psi.domain.studiesprogram.control;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;
import psi.domain.studiesprogram.entity.StudiesProgram;

@Repository
public interface StudiesProgramRepository extends JpaRepository<StudiesProgram, Long>,
        JpaSpecificationExecutor<StudiesProgram>, RevisionRepository<StudiesProgram, Long, Integer> {

}
