package psi.infrastructure.collection;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class CollectionUtils {

    @SafeVarargs
    public <T> List<T> union(List<T>... lists) {
        return Stream.of(lists)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

}
