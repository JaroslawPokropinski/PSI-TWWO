package psi.domain.discipline.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import psi.domain.discipline.entity.Discipline;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;

    public List<Discipline> getAllDisciplines() {
        return disciplineRepository.findAll();
    }

}
