package psi.api.subjectcard;

import lombok.Builder;
import lombok.Data;
import psi.domain.subjectcard.entity.CreditingForm;
import psi.domain.subjectcard.entity.SubjectClassesType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@Data
@Builder
public class SubjectClassDTO {

    private Long id;

    @NotNull
    private SubjectClassesType subjectClassesType;

    @NotNull
    @Positive
    private Integer zzuHours;

    @NotNull
    @Positive
    private Integer cnpsHours;

    @NotNull
    private Boolean isFinalCourse;

    @NotNull
    private CreditingForm creditingForm;

    @NotNull
    @PositiveOrZero
    private Integer ectsPoints;

    @NotNull
    @PositiveOrZero
    private Integer practicalEctsPoints;

    @NotNull
    @PositiveOrZero
    private Integer buEctsPoints;

    @NotNull
    private List<ProgramContentDTO> program;

}
