package psi.api.studiesplan;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class StudiesPlanDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String code;

    @NotNull
    private String decreeDate;

    @NotNull
    private String inEffectSince;

    @NotBlank
    private Long studiesProgramId;

}
