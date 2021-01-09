package psi.infrastructure.datageneration;

import java.util.UUID;

public class UuidGenerator implements DataGenerator<String> {

    @Override
    public String generateNext() {
        return UUID.randomUUID().toString();
    }

}
