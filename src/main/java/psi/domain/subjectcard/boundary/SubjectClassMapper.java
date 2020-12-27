package psi.domain.subjectcard.boundary;

import org.springframework.stereotype.Component;
import psi.api.subjectcard.ProgramContentDTO;
import psi.api.subjectcard.SubjectClassesDTO;
import psi.domain.subjectcard.entity.ProgramContent;
import psi.domain.subjectcard.entity.SubjectClasses;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class SubjectClassMapper {

    public List<SubjectClasses> mapToSubjectClasses(Collection<SubjectClassesDTO> subjectClasses) {
        return subjectClasses.stream()
                .map(this::mapToSubjectClasses)
                .collect(Collectors.toList());
    }

    private SubjectClasses mapToSubjectClasses(SubjectClassesDTO subjectClassesDTO) {
        return SubjectClasses.builder()
                .subjectClassesType(subjectClassesDTO.getSubjectClassesType())
                .zzuHours(subjectClassesDTO.getZzuHours())
                .cnpsHours(subjectClassesDTO.getCnpsHours())
                .isFinalCourse(subjectClassesDTO.getIsFinalCourse())
                .creditingForm(subjectClassesDTO.getCreditingForm())
                .ectsPoints(subjectClassesDTO.getEctsPoints())
                .practicalEctsPoints(subjectClassesDTO.getPracticalEctsPoints())
                .buEctsPoints(subjectClassesDTO.getBuEctsPoints())
                .program(mapToProgramContents(subjectClassesDTO.getProgram()))
                .build();
    }

    private Set<ProgramContent> mapToProgramContents(List<ProgramContentDTO> programContentDTOs) {
        return IntStream.range(0, programContentDTOs.size())
                .boxed()
                .map(number -> mapToProgramContent(number, programContentDTOs.get(number)))
                .collect(Collectors.toSet());
    }

    private ProgramContent mapToProgramContent(int number, ProgramContentDTO programContentDTO) {
        return new ProgramContent(number, programContentDTO.getDescription(), programContentDTO.getNumberOfHours());
    }

    public List<SubjectClassesDTO> mapToSubjectClassesDTOs(Collection<SubjectClasses> subjectClasses) {
        return subjectClasses.stream()
                .map(this::mapToSubjectClassesDTO)
                .collect(Collectors.toList());
    }

    private SubjectClassesDTO mapToSubjectClassesDTO(SubjectClasses subjectClasses) {
        return SubjectClassesDTO.builder()
                .subjectClassesType(subjectClasses.getSubjectClassesType())
                .zzuHours(subjectClasses.getZzuHours())
                .cnpsHours(subjectClasses.getCnpsHours())
                .isFinalCourse(subjectClasses.getIsFinalCourse())
                .creditingForm(subjectClasses.getCreditingForm())
                .ectsPoints(subjectClasses.getEctsPoints())
                .practicalEctsPoints(subjectClasses.getPracticalEctsPoints())
                .buEctsPoints(subjectClasses.getBuEctsPoints())
                .program(mapToProgramContentDTOs(subjectClasses.getProgram()))
                .build();
    }

    private List<ProgramContentDTO> mapToProgramContentDTOs(Collection<ProgramContent> programContents) {
        return programContents.stream()
                .sorted(Comparator.comparing(ProgramContent::getNumber))
                .map(this::mapToProgramContentDTO)
                .collect(Collectors.toList());
    }

    private ProgramContentDTO mapToProgramContentDTO(ProgramContent programContent) {
        return new ProgramContentDTO(programContent.getDescription(), programContent.getNumberOfHours());
    }
    
}
