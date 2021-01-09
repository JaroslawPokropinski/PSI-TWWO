package psi.domain.discipline.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psi.domain.discipline.entity.Discipline;
import psi.infrastructure.exception.ExceptionUtils;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;

    public List<Discipline> getAllDisciplines() {
        return disciplineRepository.findAll();
    }

    public List<Discipline> getDisciplinesByIds(Collection<Long> ids) {
        return disciplineRepository.findAllById(ids);
    }

    public Discipline getDisciplineById(Long id) {
        return disciplineRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.getObjectNotFoundException(Discipline.class, id));
    }

}
