package psi.api.studiesprogram;

import lombok.Builder;
import lombok.Data;
import psi.api.discipline.DisciplineDTO;
import psi.api.fieldofstudy.FieldOfStudyDTO;
import psi.domain.discipline.entity.Discipline;
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
    private Long fieldOfStudyId;

    @NotNull
    private StudiesLevel studiesLevel;

    @NotNull
    private StudiesForm studiesForm;

    @NotNull
    private StudiesProfile studiesProfile;

    @NotBlank
    private Integer numberOfSemesters;

    @NotBlank
    private Integer totalNumberOfEctsPoints;

    @NotBlank
    private Integer totalNumberOfHours;

    private List<String> prerequisites;

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
    private Long mainDisciplineId;

    private List<Long> disciplinesIds;

}
