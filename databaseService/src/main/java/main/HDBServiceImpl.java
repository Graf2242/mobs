package main;

import Account.HAccountDataSet;
import Account.HAccountsDAO;
import base.databaseService.DBService;
import base.masterService.nodes.Address;
import base.utils.Message;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import utils.MessageSystem.NodeMessageReceiver;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.Frontend.FWrongLoginData;
import utils.MessageSystem.messages.Metrics.MetricsIncrement;
import utils.MessageSystem.messages.masterService.MRegister;
import utils.ResourceSystem.ResourceFactory;
import utils.ResourceSystem.Resources.configs.ServerConfig;
import utils.logger.LoggerImpl;
import utils.logger.UncaughtExceptionLog4j2Handler;
import utils.tickSleeper.TickSleeper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class HDBServiceImpl implements DBService {
    private static Logger log;
    final private Address address = new Address();
    private final NodeMessageReceiver messageReceiver;
    private SessionFactory sessionFactory;
    private ResourceFactory resourceFactory;
    private ServerConfig serverConfig;
    private String hbm2dll = "update";
    private Socket masterService;
    private Queue<Message> unhandledMessages = new LinkedBlockingQueue<>();
    private boolean masterIsReady;

    public HDBServiceImpl(String configPath) {
        resourceFactory = ResourceFactory.instance();
        serverConfig = (ServerConfig) resourceFactory.getResource(configPath);
        InetAddress address;
        try {
            address = InetAddress.getByName(serverConfig.getDbService().getIp());
            masterService = new Socket(serverConfig.getMaster().getIp(),
                    Integer.parseInt(serverConfig.getMaster().getMasterPort()), address,
                    Integer.parseInt(serverConfig.getDbService().getMasterPort()));
        } catch (IOException e) {
            log.error(e);
        }
        resourceFactory = ResourceFactory.instance();
        messageReceiver = new NodeMessageReceiver(unhandledMessages, masterService);


        //noinspection InfiniteLoopStatement
        NodeMessageSender.sendMessage(masterService, new MRegister(this.address, DBService.class, serverConfig.getDbService().getIp(), serverConfig.getDbService().getMasterPort()));

    }

    public static void main(String[] args) {
        String arg = null;
        try {
            arg = args[0];
        } catch (Exception ignored) {
        }
        String configPath = Objects.equals(arg, null) ? "config.xml" : arg;
        log = LoggerImpl.getLogger("DatabaseService");

        DBService dbService = new HDBServiceImpl(configPath);
        Thread dbServiceThread = new Thread(dbService);
        dbServiceThread.setName("DBService");
        dbServiceThread.setUncaughtExceptionHandler(new UncaughtExceptionLog4j2Handler(log));
        dbServiceThread.start();
    }

    @Override
    public void setMasterIsReady(boolean masterIsReady) {
        this.masterIsReady = masterIsReady;
    }

    public Logger getLog() {
        return log;
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
        NodeMessageSender.sendMessage(getMasterService(), new MetricsIncrement(getAddress(), "DBRequests", "Executed DB requests"));
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
        NodeMessageSender.sendMessage(getMasterService(), new MetricsIncrement(getAddress(), "DBRequests", "Executed DB requests"));
        HAccountDataSet dataSet = null;
        HAccountsDAO dao = new HAccountsDAO(sessionFactory);
        try {
            dataSet = dao.writeAccount(userName, pass);
        } catch (SQLException e) {
            log.error(e);
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


        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(HAccountDataSet.class);
        this.sessionFactory = createSessionConfig(configuration);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        System.out.append(transaction.getStatus().toString()).append(String.valueOf('\n'));
        session.close();


        TickSleeper tickSleeper = new TickSleeper();
        tickSleeper.setTickTimeMs(100L);

        while (!masterIsReady) {
            tickSleeper.tickStart();
            execNodeMessages();
            tickSleeper.tickEnd();
        }

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

    private SessionFactory createSessionConfig(Configuration configuration) {

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", serverConfig.getDatabasePath());
        configuration.setProperty("hibernate.connection.username", serverConfig.getDbLogin());
        configuration.setProperty("hibernate.connection.password", serverConfig.getDbPass());
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", hbm2dll);

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        StandardServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
