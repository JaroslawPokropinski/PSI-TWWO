package psi.api.studiesplan;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class StudiesPlanDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String code;

    @NotNull
    private LocalDate decreeDate;

    @NotNull
    private LocalDate inEffectSince;

    @NotBlank
    private Long studiesProgramId;

}
