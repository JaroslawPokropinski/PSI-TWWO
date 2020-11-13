package psi.domain.subjectcard;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class ProgramContent {

    private int number;
    private String content;
    private double numberOfHours;

}
