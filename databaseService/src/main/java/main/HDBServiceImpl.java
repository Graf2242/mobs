package main;

import Account.HAccountDataSet;
import Account.HAccountsDAO;
import MessageSystem.NodeMessageReceiver;
import MessageSystem.NodeMessageSender;
import MessageSystem.messages.Frontend.FWrongLoginData;
import MessageSystem.messages.masterService.MRegister;
import ResourceSystem.ResourceFactory;
import ResourceSystem.Resources.configs.ServerConfig;
import databaseService.DBService;
import masterService.Message;
import masterService.nodes.Address;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import tickSleeper.TickSleeper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class HDBServiceImpl implements DBService {
    final private Address address = new Address();
    SessionFactory sessionFactory;
    private ResourceFactory resourceFactory = ResourceFactory.instance();
    private final NodeMessageReceiver messageReceiver;
    ServerConfig serverConfig;
    private String hbm2dll = "update";
    private Socket masterService;
    private Queue<Message> unhandledMessages = new LinkedBlockingQueue<>();

    public HDBServiceImpl(String configPath) {
        serverConfig = (ServerConfig) resourceFactory.getResource(configPath);
        InetAddress address;
        try {
            address = InetAddress.getByName(serverConfig.getDbService().getIp());
            masterService = new Socket(serverConfig.getMaster().getIp(),
                    Integer.parseInt(serverConfig.getMaster().getPort()), address,
                    Integer.parseInt(serverConfig.getDbService().getPort()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        resourceFactory = ResourceFactory.instance();
        messageReceiver = new NodeMessageReceiver(unhandledMessages, masterService, this);

        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(HAccountDataSet.class);

        this.sessionFactory = createSessionConfig(configuration);

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        System.out.append(transaction.getStatus().toString()).append(String.valueOf('\n'));
        session.close();

        //noinspection InfiniteLoopStatement
        NodeMessageSender.sendMessage(masterService, new MRegister(this.address, this, serverConfig.getDbService().getIp(), serverConfig.getDbService().getPort()));

    }

    public void setHbm2dll(String hbm2dll) {
        this.hbm2dll = hbm2dll;
    }

    @Override
    public Socket getMasterService() {
        return masterService;
    }

    @Override
    public Long getAccountId(String userName, String pass, Long sessionId) {
        HAccountDataSet dataSet;
        HAccountsDAO dao = new HAccountsDAO(sessionFactory);
        dataSet = dao.get(userName);
        if (Objects.equals(dataSet, null)) {
            NodeMessageSender.sendMessage(masterService, new FWrongLoginData(address, sessionId));
            return null;
        }
        if (!userName.equals(dataSet.getLogin()) || !pass.equals(dataSet.getPass())) {
            NodeMessageSender.sendMessage(masterService, new FWrongLoginData(address, sessionId));
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
        TickSleeper tickSleeper = new TickSleeper();
        tickSleeper.setTickTimeMs(100L);
        //noinspection InfiniteLoopStatement
        while (true) {
            tickSleeper.tickStart();
            execNodeMessages();
            tickSleeper.tickEnd();
        }
    }

    private void execNodeMessages() {
        while (!unhandledMessages.isEmpty()) {
            unhandledMessages.poll().exec(this);
        }
    }

    public SessionFactory createSessionConfig(Configuration configuration) {

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
