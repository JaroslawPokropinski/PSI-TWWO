package psi.api.studiesprogram;

import lombok.Builder;
import lombok.Data;
import psi.domain.simpleattribute.entity.SimpleAttribute;
import psi.domain.studiesprogram.entity.DegreeTitle;
import psi.domain.studiesprogram.entity.StudiesForm;
import psi.domain.studiesprogram.entity.StudiesLevel;
import psi.domain.studiesprogram.entity.StudiesProfile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class StudiesProgramDTO {

    private Long id;

    @NotBlank
    private String code;

    @NotBlank
    private Long fieldOfStudy;

    @NotNull
    private StudiesLevel studiesLevel;

    @NotNull
    private StudiesForm studiesForm;

    @NotNull
    private StudiesProfile studiesProfile;

    @NotBlank
    private Integer numberOfSemesters;

    @NotBlank
    private Integer totalNumberOfHours;

    private List<SimpleAttribute> prerequisites;

    @NotBlank
    private DegreeTitle degreeTitle;

    @NotBlank
    private String languageOfStudies;

    @NotBlank
    private String graduateProfile;

    @NotBlank
    private String possibilityOfContinuingStudies;

    @NotBlank
    private String connectionWithMissionAndDevelopmentStrategy;

    @NotNull
    private Long mainDiscipline;

    private List<Long> disciplines;

}
