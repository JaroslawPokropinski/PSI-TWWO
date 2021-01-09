package psi.domain.common;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class SequenceIdGenerator {

    private final AtomicLong currentId;

    public SequenceIdGenerator(Long initialValue) {
        this.currentId = new AtomicLong(initialValue);
    }

    public Long generate() {
        return currentId.getAndIncrement();
    }

    public List<Long> generate(int count) {
        long first = currentId.getAndAdd(count);
        return LongStream.iterate(first, number -> number + 1)
                .limit(count)
                .boxed()
                .collect(Collectors.toList());
    }

}
