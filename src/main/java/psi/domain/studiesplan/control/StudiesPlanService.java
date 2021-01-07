package psi.domain.studiesplan.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psi.api.studiesplan.StudiesPlanDTO;
import psi.domain.studiesplan.entity.StudiesPlan;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StudiesPlanService {

    private final StudiesPlanRepository studiesPlanRepository;

    public List<StudiesPlan> getAllStudiesPlans(){
        return studiesPlanRepository.findAll();
    }

}
