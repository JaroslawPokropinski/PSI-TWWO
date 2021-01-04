package psi.domain.studiesprogram.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import psi.domain.studiesprogram.entity.StudiesProgram;
import psi.infrastructure.exception.ExceptionUtils;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StudiesProgramService {

    private final StudiesProgramRepository studiesProgramRepository;

    public List<StudiesProgram> getAllStudiesPrograms(){
        return studiesProgramRepository.findAll();
    }

    public StudiesProgram getStudiesProgramById(Long id){
        return studiesProgramRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.getObjectNotFoundException(StudiesProgram.class, id));
    }

    public List<StudiesProgram> getStudiesProgramByIds(Collection<Long> ids){
        return studiesProgramRepository.findAllById(ids);
    }

}
