package psi.domain.fieldofstudy.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import psi.domain.fieldofstudy.entity.FieldOfStudy;
import psi.infrastructure.exception.ExceptionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FieldOfStudyService {

    private final FieldOfStudyRepository fieldOfStudyRepository;

    public List<FieldOfStudy> getAllFiledOfStudies() {
        return fieldOfStudyRepository.findAll();
    }

    public FieldOfStudy getFieldOfStudyById(Long id) {
        return fieldOfStudyRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.getObjectNotFoundException(FieldOfStudy.class, id));
    }

}
