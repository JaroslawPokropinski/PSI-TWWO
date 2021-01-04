package psi.api.subjectcard;

import lombok.Builder;
import lombok.Data;
import psi.domain.studiesprogram.entity.StudiesForm;
import psi.domain.studiesprogram.entity.StudiesLevel;
import psi.domain.subjectcard.entity.SubjectType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class SubjectCardDTO {

    private Long id;

    @NotBlank
    private String subjectName;

    @NotBlank
    private String subjectNameInEnglish;

    @NotNull
    private Long fieldOfStudy;

    private String specialization;

    @NotNull
    private StudiesLevel studiesLevel;

    @NotNull
    private StudiesForm studiesForm;

    @NotNull
    private SubjectType subjectType;

    @NotNull
    private String code;

    @NotNull
    private Boolean isGroupOfCourses;

    @NotNull
    private Long organisationalUnit;

    @NotNull
    private List<String> prerequisites;

    @NotNull
    private List<String> subjectObjectives;

    @NotNull
    private List<String> primaryLiterature;

    @NotNull
    private List<String> secondaryLiterature;

    @NotNull
    private List<String> usedTeachingTools;

    @NotNull
    private Long supervisor;

    @NotNull
    private List<SubjectClassesDTO> subjectClasses;

    @NotNull
    private List<Long> educationalEffects;

}
