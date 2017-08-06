package masterService;

import java.util.concurrent.atomic.AtomicInteger;

public class Address {
    final static private AtomicInteger atomicInteger = new AtomicInteger();
    final private Integer id;

    public Address() {
        id = atomicInteger.incrementAndGet();
    }

    public Integer getId() {
        return id;
    }
}
