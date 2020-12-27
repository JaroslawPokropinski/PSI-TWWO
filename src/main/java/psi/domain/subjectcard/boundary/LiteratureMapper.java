package psi.domain.subjectcard.boundary;

import org.springframework.stereotype.Component;
import psi.api.subjectcard.SubjectCardDTO;
import psi.domain.subjectcard.entity.Literature;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class LiteratureMapper {

    public List<Literature> mapToLiterature(SubjectCardDTO subjectCardDTO) {
        return Stream.of(mapToLiterature(subjectCardDTO.getPrimaryLiterature(), true), mapToLiterature(subjectCardDTO.getSecondaryLiterature(), false))
                .flatMap(Collection::parallelStream)
                .collect(Collectors.toList());
    }

    private List<Literature> mapToLiterature(List<String> literature, boolean isPrimary) {
        return IntStream.range(0, literature.size())
                .boxed()
                .map(number -> mapToLiterature(number, literature.get(number), isPrimary))
                .collect(Collectors.toList());
    }

    private Literature mapToLiterature(int number, String name, boolean isPrimary) {
        return new Literature(number, name, isPrimary);
    }

    public List<String> mapToPrimaryLiteratureStrings(Collection<Literature> literature) {
        return mapToStrings(literature, true);
    }

    public List<String> mapToSecondaryLiteratureStrings(Collection<Literature> literature) {
        return mapToStrings(literature, false);
    }

    private List<String> mapToStrings(Collection<Literature> literature, boolean isPrimary) {
        return literature.stream()
                .filter(lit -> isPrimary == lit.getIsPrimary())
                .sorted(Comparator.comparing(Literature::getNumber))
                .map(Literature::getName)
                .collect(Collectors.toList());
    }

}
