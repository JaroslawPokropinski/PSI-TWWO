package psi.domain.discipline.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psi.domain.discipline.entity.Discipline;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;

    public List<Discipline> getAllDisciplines() {
        return disciplineRepository.findAll();
    }

}
