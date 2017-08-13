package masterService.nodes;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Address implements Serializable {
    final static private AtomicInteger atomicInteger = new AtomicInteger();
    final private Integer id;

    public Address() {
        id = atomicInteger.incrementAndGet();
    }

    public Integer getId() {
        return id;
    }
}
