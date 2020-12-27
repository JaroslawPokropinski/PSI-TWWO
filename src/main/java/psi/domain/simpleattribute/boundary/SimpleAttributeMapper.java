package psi.domain.simpleattribute.boundary;

import org.springframework.stereotype.Component;
import psi.domain.simpleattribute.entity.SimpleAttribute;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class SimpleAttributeMapper {

    public Set<SimpleAttribute> mapToSimpleAttributes(List<String> contents) {
        return IntStream.range(0, contents.size())
                .boxed()
                .map(number -> mapToSimpleAttribute(number, contents.get(number)))
                .collect(Collectors.toSet());
    }

    private SimpleAttribute mapToSimpleAttribute(int number, String content) {
        return new SimpleAttribute(number, content);
    }

    public List<String> mapToStrings(Collection<SimpleAttribute> simpleAttributes) {
        return simpleAttributes.stream()
                .sorted(Comparator.comparing(SimpleAttribute::getNumber))
                .map(SimpleAttribute::getContent)
                .collect(Collectors.toList());
    }

}
