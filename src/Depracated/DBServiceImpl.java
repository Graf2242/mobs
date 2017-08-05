package graf.server.DBService;


import graf.server.Base.Address;
import graf.server.Base.DBService;
import graf.server.Base.MasterService;
import graf.server.DBService.Account.AccountsDAO;
import graf.server.DBService.Account.AccountsDataSet;
import graf.server.MasterService.messages.Frontend.FWrongLoginData;
import graf.server.Utils.DB.ResultHandler;
import graf.server.Utils.ResourceSystem.ResourceFactory;
import graf.server.Utils.ResourceSystem.Resources.ServerConfig;
import graf.server.Utils.TickSleeper;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class DBServiceImpl implements DBService {
    final MasterService masterService;
    private String configPath;
    final private Address address = new Address();
    final private Map<String, Integer> users = new HashMap<>();
    final private AtomicInteger atomicInteger = new AtomicInteger();
    private ResourceFactory resourceFactory;
    Connection connection;


    public DBServiceImpl(MasterService MasterService, String configPath) {
        this.masterService = MasterService;
        this.configPath = configPath;
        this.resourceFactory = ResourceFactory.instance();
        connectToDB();
    }

    public MasterService getMasterService() {
        return masterService;
    }

    @Override
    public Long getAccountId(String userName, String pass, Long sessionId) {

        AccountsDAO accountsDAO = new AccountsDAO(connection);
        AccountsDataSet accountDataSet;
        accountDataSet = accountsDAO.get(userName);
        if (Objects.equals(accountDataSet, null)) {
            masterService.addMessage(new FWrongLoginData(address, sessionId));
            return null;
        }
        if (!userName.equals(accountDataSet.getLogin()) || !pass.equals(accountDataSet.getPass())) {
            masterService.addMessage(new FWrongLoginData(address, sessionId));
            return null;
        }
        return accountDataSet.getId();
    }

    @Override
    public Long createAccount(String userName, String pass) {
        AccountsDAO accountsDAO = new AccountsDAO(connection);
        AccountsDataSet accountDataSet;
        accountDataSet = accountsDAO.get(userName);
        try {
            accountDataSet = accountsDAO.writeAccount(userName, pass);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accountDataSet.getId();
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void run() {
        getMasterService().register(this);
        TickSleeper tickSleeper = new TickSleeper();
        tickSleeper.setTickTimeMs(10000L);
        //noinspection InfiniteLoopStatement
        while (true) {
            tickSleeper.tickStart();
            masterService.execNodeMessages(this);
            tickSleeper.tickEnd();
        }
    }

    public <T> T execQuery(Connection connection,
                           String query,
                           ResultHandler<T> handler)
            throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(query);
        ResultSet result = stmt.getResultSet();
        T value = handler.handle(result);
        result.close();
        stmt.close();
        return value;
    }

    @Override
    public void connectToDB() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("org.postgresql.Driver").newInstance());
        } catch (SQLException | InstantiationException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
        ServerConfig serverConfig = (ServerConfig) resourceFactory.getResource(configPath);
        try {
            connection = DriverManager.getConnection(serverConfig.getDatabasePath(), serverConfig.getDbLogin(), serverConfig.getDbPass());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
