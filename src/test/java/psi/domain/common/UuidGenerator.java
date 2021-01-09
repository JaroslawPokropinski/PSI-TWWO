package psi.domain.common;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UuidGenerator {

    public String generate() {
        return UUID.randomUUID().toString();
    }

    public List<String> generate(int count) {
        return Stream.generate(this::generate)
                .limit(count)
                .collect(Collectors.toList());
    }

}
