package psi.infrastructure.datageneration;

import java.util.concurrent.atomic.AtomicLong;

public class SequenceIdGenerator implements DataGenerator<Long> {

    private final AtomicLong currentId;

    public SequenceIdGenerator(Long initialValue) {
        this.currentId = new AtomicLong(initialValue);
    }

    @Override
    public Long generateNext() {
        return currentId.getAndIncrement();
    }

}
