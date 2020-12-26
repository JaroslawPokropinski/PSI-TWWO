package psi.domain.discipline.control;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import psi.domain.discipline.entity.Discipline;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
}
