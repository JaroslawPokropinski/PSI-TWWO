package psi.domain.discipline.control;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import psi.domain.discipline.entity.Discipline;

import java.util.Collection;
import java.util.List;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {

    List<Discipline> findAllByNameIn(Collection<String> names);
    List<Discipline> findAllByNameInAndIdNotIn(Collection<String> names, Collection<Long> ids);

}
