package psi.domain.educationaleffect.control;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;
import psi.domain.educationaleffect.entity.EducationalEffect;

import java.util.Collection;
import java.util.List;

@Repository
public interface EducationalEffectRepository extends JpaRepository<EducationalEffect, Long>,
        JpaSpecificationExecutor<EducationalEffect>, RevisionRepository<EducationalEffect, Long, Integer> {

    List<EducationalEffect> findAllByCodeIn(Collection<String> codes);

    List<EducationalEffect> findAllByCodeInAndIdNotIn(Collection<String> codes, Collection<Long> ids);

}
