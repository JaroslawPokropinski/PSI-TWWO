package psi.api.semester;

import lombok.Builder;
import lombok.Data;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class SemesterDTO {

    private Long id;

    @NotBlank
    private Integer number;

    @NotBlank
    private Integer allowedEctsDeficit;

    @NotNull
    @ManyToOne
    private Long studiesPlanId;

    @NotNull
    private List<Long> subjectCardIds;

}
