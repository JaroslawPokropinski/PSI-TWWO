package psi.domain.subjectcard;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Item {

    private int number;
    private String content;

}
