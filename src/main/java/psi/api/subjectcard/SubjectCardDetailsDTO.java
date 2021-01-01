package psi.api.subjectcard;

import lombok.Builder;
import lombok.Data;
import psi.api.educationaleffect.EducationalEffectDTO;
import psi.api.fieldofstudy.FieldOfStudyDTO;
import psi.api.organisationalunit.OrganisationalUnitDTO;
import psi.api.user.UserDTO;
import psi.domain.auditedobject.entity.ObjectState;
import psi.domain.studiesprogram.StudiesForm;
import psi.domain.studiesprogram.StudiesLevel;
import psi.domain.subjectcard.entity.SubjectType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Data
@Builder
public class SubjectCardDetailsDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String subjectName;

    @NotBlank
    private String subjectNameInEnglish;

    @NotNull
    private FieldOfStudyDTO fieldOfStudy;

    private String specialization;

    @NotNull
    private StudiesLevel studiesLevel;

    @NotNull
    private StudiesForm studiesForm;

    @NotNull
    private SubjectType subjectType;

    @NotNull
    private String subjectCode;

    @NotNull
    private Boolean isGroupOfCourses;

    @NotNull
    private OrganisationalUnitDTO organisationalUnit;

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
    private UserDTO supervisor;

    @NotNull
    private List<SubjectClassesDTO> subjectClasses;

    @NotNull
    private List<EducationalEffectDTO> educationalEffects;

    @NotNull
    private ObjectState objectState;

    @NotNull
    private UserDTO createdBy;

    @NotNull
    private Instant createdAt;

    @NotNull
    private UserDTO lastUpdatedBy;

    @NotNull
    private Instant lastUpdatedAt;

}
