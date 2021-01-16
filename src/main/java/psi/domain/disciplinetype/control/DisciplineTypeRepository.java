package psi.domain.disciplinetype.control;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import psi.domain.disciplinetype.entity.DisciplineType;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplineTypeRepository extends JpaRepository<DisciplineType, Long> {

    Optional<DisciplineType> findByName(String name);
    List<DisciplineType> findAllByNameIn(Collection<String> names);
    List<DisciplineType> findAllByNameInAndIdNotIn(Collection<String> names, Collection<Long> ids);

}
