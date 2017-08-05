package graf.server.DBService;

import graf.server.Base.Address;
import graf.server.Base.DBService;
import graf.server.Base.MasterService;
import graf.server.DBService.Account.HAccountDataSet;
import graf.server.DBService.Account.HAccountsDAO;
import graf.server.MasterService.messages.Frontend.FWrongLoginData;
import graf.server.Utils.ResourceSystem.ResourceFactory;
import graf.server.Utils.ResourceSystem.Resources.ServerConfig;
import graf.server.Utils.TickSleeper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.sql.SQLException;
import java.util.Objects;

public class HDBServiceImpl implements DBService {
    final MasterService masterService;
    final private Address address = new Address();
    SessionFactory sessionFactory;
    private ResourceFactory resourceFactory = ResourceFactory.instance();
    private String configPath;
    private String hbm2dll = "update";

    public HDBServiceImpl(MasterService masterService, String configPath) {
        this.masterService = masterService;
        this.configPath = configPath;
        resourceFactory = ResourceFactory.instance();
    }

    public void setHbm2dll(String hbm2dll) {
        this.hbm2dll = hbm2dll;
    }

    @Override
    public MasterService getMasterService() {
        return masterService;
    }

    @Override
    public Long getAccountId(String userName, String pass, Long sessionId) {
        HAccountDataSet dataSet;
        HAccountsDAO dao = new HAccountsDAO(sessionFactory);
        dataSet = dao.get(userName);
        if (Objects.equals(dataSet, null)) {
            masterService.addMessage(new FWrongLoginData(address, sessionId));
            return null;
        }
        if (!userName.equals(dataSet.getLogin()) || !pass.equals(dataSet.getPass())) {
            masterService.addMessage(new FWrongLoginData(address, sessionId));
            return null;
        }
        return dataSet.getId();
    }

    @Override
    public Long createAccount(String userName, String pass) {
        HAccountDataSet dataSet = null;
        HAccountsDAO dao = new HAccountsDAO(sessionFactory);
        try {
            dao.writeAccount(userName, pass);
            dataSet = dao.get(userName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (dataSet != null) {
            return dataSet.getId();
        }
        return -1L;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void run() {
        getMasterService().register(this);

        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(HAccountDataSet.class);

        this.sessionFactory = createSessionConfig(configuration);

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        System.out.append(transaction.getStatus().toString()).append(String.valueOf('\n'));
        session.close();

        TickSleeper tickSleeper = new TickSleeper();
        tickSleeper.setTickTimeMs(100L);
        //noinspection InfiniteLoopStatement
        while (true) {
            tickSleeper.tickStart();
            masterService.execNodeMessages(this);
            tickSleeper.tickEnd();
        }
    }

    public SessionFactory createSessionConfig(Configuration configuration) {
        ServerConfig serverConfig = (ServerConfig) resourceFactory.getResource(configPath);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", serverConfig.getDatabasePath());
        configuration.setProperty("hibernate.connection.username", serverConfig.getDbLogin());
        configuration.setProperty("hibernate.connection.password", serverConfig.getDbPass());
        configuration.setProperty("hibernate.show_sql", "false");
        configuration.setProperty("hibernate.hbm2ddl.auto", hbm2dll);

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        StandardServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
