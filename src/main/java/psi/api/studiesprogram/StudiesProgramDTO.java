package psi.api.studiesprogram;

import lombok.Builder;
import lombok.Data;
import psi.domain.auditedobject.entity.ObjectState;
import psi.domain.studiesprogram.entity.DegreeTitle;
import psi.domain.studiesprogram.entity.StudiesForm;
import psi.domain.studiesprogram.entity.StudiesLevel;
import psi.domain.studiesprogram.entity.StudiesProfile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
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
    private LocalDate inEffectSince;

    @NotNull
    private Long mainDisciplineId;

    @NotNull
    private ObjectState objectState;

    private List<Long> disciplinesIds;

    private List<Long> educationalEffects;

}
