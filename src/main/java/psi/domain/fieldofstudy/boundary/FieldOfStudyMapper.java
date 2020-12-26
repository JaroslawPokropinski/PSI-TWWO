package psi.domain.fieldofstudy.boundary;

import org.springframework.stereotype.Component;
import psi.api.fieldofstudy.FieldOfStudyDTO;
import psi.domain.fieldofstudy.entity.FieldOfStudy;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FieldOfStudyMapper {

    public List<FieldOfStudyDTO> mapToFiledOfStudyDTOs(Collection<FieldOfStudy> fieldOfStudies) {
        return fieldOfStudies.stream()
                .map(this::mapToFieldOfStudyDTO)
                .collect(Collectors.toList());
    }

    private FieldOfStudyDTO mapToFieldOfStudyDTO(FieldOfStudy fieldOfStudy) {
        return new FieldOfStudyDTO(fieldOfStudy.getId(), fieldOfStudy.getName(), fieldOfStudy.getOrganisationalUnit().getId());
    }

}
