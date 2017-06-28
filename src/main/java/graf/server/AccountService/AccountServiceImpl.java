package graf.server.AccountService;


import graf.server.Base.AccountService;
import graf.server.Base.Address;
import graf.server.Base.MasterService;
import graf.server.Utils.TickSleeper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AccountServiceImpl implements AccountService {
    final MasterService masterService;
    final private Address address = new Address();
    final private Map<String, Integer> users = new HashMap<>();
    final private AtomicInteger atomicInteger = new AtomicInteger();

    public AccountServiceImpl(MasterService MasterService) {
        this.masterService = MasterService;
    }

    public MasterService getMasterService() {
        return masterService;
    }

    public Integer getUserId(String userName) {
        return users.computeIfAbsent(userName, k -> atomicInteger.getAndIncrement());
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void run() {
        getMasterService().register(this);
        TickSleeper tickSleeper = new TickSleeper();
        //noinspection InfiniteLoopStatement
        while (true) {
            tickSleeper.tickStart();
            masterService.execNodeMessages(this);
            tickSleeper.tickEnd();
        }
    }
}
