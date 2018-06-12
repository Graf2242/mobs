package main;

import base.frontend.Frontend;
import base.frontend.FrontendUserSession;
import base.frontend.UserSessionStatus;
import base.masterService.Connector;
import base.masterService.nodes.Address;
import base.utils.Message;
import org.apache.logging.log4j.Logger;
import utils.MessageSystem.NodeMessageReceiver;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.GameMechanics.GMSendUserSockets;
import utils.MessageSystem.messages.Metrics.MetricsIncrement;
import utils.MessageSystem.messages.clientMessages.toClient.CConnectViaUDP;
import utils.MessageSystem.messages.clientMessages.toClient.CLoginSuccess;
import utils.MessageSystem.messages.clientMessages.toClient.CUpdateSession;
import utils.MessageSystem.messages.clientMessages.toClient.CUpdateSessionStatus;
import utils.MessageSystem.messages.masterService.MRegister;
import utils.ResourceSystem.ResourceFactory;
import utils.ResourceSystem.Resources.configs.ServerConfig;
import utils.ServerSocketUtils.ConnectorImpl;
import utils.ServerSocketUtils.MessageExecutor;
import utils.logger.LoggerImpl;
import utils.logger.UncaughtExceptionLog4j2Handler;
import utils.tickSleeper.TickSleeper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;


public class FrontendImpl implements Frontend {
    private final Address address = new Address();
    private ServerConfig serverConfig;
    private final Map<Long, FrontendUserSession> sessions = new HashMap<>();
    final private Queue<Message> unsortedMessagesFromClients = new LinkedBlockingQueue<>();
    private static Logger log;
    private Queue<Message> unhandledMessages = new LinkedBlockingQueue<>();
    private ServerSocket serverSocket;
    private Connector connector;
    private String configPath;
    private ResourceFactory resourceFactory;
    private Socket masterService;
    private boolean masterIsReady;

    public FrontendImpl(String configPath) {
        this.configPath = configPath;
        this.resourceFactory = ResourceFactory.instance();
        log = LoggerImpl.getLogger("Frontend");
        serverConfig = (ServerConfig) resourceFactory.getResource(configPath);
        InetAddress address;
        try {
            address = InetAddress.getByName(serverConfig.getFrontend().getIp());
            masterService = new Socket(serverConfig.getMaster().getIp(),
                    Integer.parseInt(serverConfig.getMaster().getMasterPort()), address,
                    Integer.parseInt(serverConfig.getFrontend().getMasterPort()));
        } catch (IOException e) {
            log.error(e);
        }

        try {
            startFrontend();
        } catch (IOException e) {
            log.fatal("Frontend does not started");
            log.error(e);
        }
        new NodeMessageReceiver(unhandledMessages, masterService);
        NodeMessageSender.sendMessage(masterService, new MRegister(this.address, Frontend.class, serverConfig.getFrontend().getIp(), serverConfig.getFrontend().getMasterPort()));
    }

    public static void main(String[] args) {
        String arg = null;
        try {
            arg = args[0];
        } catch (Exception ignored) {
        }
        String configPath = Objects.equals(arg, null) ? "config.xml" : arg;
        Frontend frontend = new FrontendImpl(configPath);
        Thread frontendThread = new Thread(frontend);
        frontendThread.setName("frontend");
        frontendThread.setUncaughtExceptionHandler(new UncaughtExceptionLog4j2Handler(log));
        frontendThread.start();
    }


    public Logger getLog() {
        return log;
    }

    @Override
    public Map<Long, FrontendUserSession> getSessions() {
        return sessions;
    }

    @Override
    public Socket getMasterService() {
        return masterService;
    }

    @Override
    public void run() {

        TickSleeper tickSleeper = new TickSleeper();
        while (!masterIsReady) {
            tickSleeper.tickStart();
            execNodeMessages();
            tickSleeper.tickEnd();
        }
        //noinspection InfiniteLoopStatement
        while (true) {
            tickSleeper.tickStart();
            execNodeMessages();
            sendUpdatesToUsers();
            tickSleeper.tickEnd();
        }
    }

    private void sendUpdatesToUsers() {
        sessions.forEach((aLong, frontendUserSession) -> {
            if (frontendUserSession.isNeedUpdate()) {
                NodeMessageSender.sendMessage(frontendUserSession.getUserSocket(), new CUpdateSession(frontendUserSession));
                frontendUserSession.setNeedUpdate(false);
            }
        });
    }

    @Override
    public void redirectUsersToMechanics(Set<Long> userIDs, String ip, int port) {
        for (Long userID : userIDs) {
            NodeMessageSender.sendMessage(getSessionByUserId(userID).getUserSocket(), new CConnectViaUDP(ip, port));
            NodeMessageSender.sendMessage(getMasterService(), new GMSendUserSockets(getAddress(), sessions));
        }
    }

    private void execNodeMessages() {
        while (!unsortedMessagesFromClients.isEmpty()) {
            unsortedMessagesFromClients.poll().exec(this);
        }
        while (!unhandledMessages.isEmpty()) {
            unhandledMessages.poll().exec(this);
        }
    }

    @Override
    public Long getSessionId(String userName) {
        final FrontendUserSession[] userSession = new FrontendUserSessionImpl[1];
        sessions.forEach((aLong, frontendUserSession) -> {
            String sessionUserName = frontendUserSession.getUserName();
            if (Objects.equals(sessionUserName, null)) {
                return;
            }
            if (sessionUserName.equals(userName)) {
                userSession[0] = frontendUserSession;
            }
        });
        return userSession[0].getSessionId();
    }

    @Override
    public Long getSessionId(Long userId) {
        final FrontendUserSession[] userSession = new FrontendUserSessionImpl[1];
        sessions.forEach((aLong, frontendUserSession) -> {
            Long sessionUserID = frontendUserSession.getUserId();
            if (Objects.equals(sessionUserID, null)) {
                return;
            }
            if (sessionUserID.equals(userId)) {
                userSession[0] = frontendUserSession;
            }
        });
        return userSession[0].getSessionId();
    }

    private void startFrontend() throws IOException {
        List<Socket> sockets = new CopyOnWriteArrayList<>();
        if (serverConfig == null) log.fatal(new RuntimeException("Server config is null"));
        connector = new ConnectorImpl(serverConfig.getFrontend().getIp(), serverConfig.getFrontend().getSecondPort(), sockets, log);
        new MessageExecutor(unsortedMessagesFromClients, sockets, log);
        log.fatal("Frontend started!");
        /* Jetty
        ServerConfig config = (ServerConfig) resourceFactory.getResource(configPath);
        Server server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setHost(config.getFrontend().getIp());
        connector.setMasterPort(Integer.parseInt(config.getFrontend().getSecondPort()));
        server.addConnector(connector);
        server.setHandler(this);

        try {
            server.start();
        } catch (Exception e) {
            log.error(e);
        }*/
    }

//    JEtty too
//    @Override
//    public void handle(String target,
//                       Request baseRequest,
//                       HttpServletRequest request,
//                       HttpServletResponse response)
//            throws IOException, ServletException {
//        response.setContentType("text/html;charset=utf-8");
//        response.setStatus(HttpServletResponse.SC_OK);
//        baseRequest.setHandled(true);
//
//        Long id = 0L;
//        String userName = "Graf";
//        String pass = "123";
//
//        FrontendUserSession userSession;
//
//        //noinspection ConstantConditions
//        if (id < 0) {
//            userSession = new FrontendUserSessionImpl();
//            sessions.put(userSession.getSessionId(), userSession);
//            userSession.setUserName(userName);
//        } else {
//            userSession = sessions.get(id);
//        }
//        if (userSession.getStatus().equals(UserSessionStatus.WRONG_LOGIN_INFO)) {
//            response.getWriter().println("<h1>Wrong login info</h1>");
//            return;
//        }
//
//        Long userId = userSession.getUserId();
//
//        //CreateUser
//        //   NodeMessageSender.sendMessage(new DBCreateUser(address, userName, pass, userSession.getSessionId()));
//
//        //Login
//        if (userId == null && userSession.getStatus().equals(UserSessionStatus.CONNECTED)) {
//            userSession.setStatus(UserSessionStatus.IN_LOGIN);
//            NodeMessageSender.sendMessage(masterService, new DBFindUserIdMessage(address, userName, pass, userSession.getSessionId()));
//            response.getWriter().println("<h1>Wait for authorization</h1><meta http-equiv=\"refresh\" content=\"1\">");
//        } else if (userSession.getStatus().equals(UserSessionStatus.FIGHT)) {
//            response.getWriter().println("<h1>User name: " + userSession.getUserName() + " Id: " + userSession.getUserId() + " , sessionTime = " + getUserDateFull(userSession.getSessionTime()) + "</h1><meta http-equiv=\"refresh\" content=\"1\">");
//        } else {
//            response.getWriter().println("<h1>Fight loading</h1><meta http-equiv=\"refresh\" content=\"1\">");
//        }
//    }

    @Override
    public Long addUser(String login, String pass, Socket clientSocket) {
        FrontendUserSession userSession = new FrontendUserSessionImpl();
        sessions.put(userSession.getSessionId(), userSession);
        userSession.setUserName(login);
        userSession.setUserSocket(clientSocket);
        userSession.setStatus(UserSessionStatus.IN_LOGIN);
        log.info("User connected");
        NodeMessageSender.sendMessage(masterService, new MetricsIncrement(this.getAddress(), "CCUFrontend", "Count of Connected Users on Frontend"));
        return userSession.getSessionId();
    }

    @Override
    public void updateUserId(Long sessionId, Long userId) {
        FrontendUserSession userSession = sessions.get(sessionId);
//        if (userSession.getUserId() == null) {
//            //TODO add error
//        }
        userSession.setUserId(userId);
        UserSessionStatus status = UserSessionStatus.CONNECTED;
        userSession.setStatus(status);
        NodeMessageSender.sendMessage(userSession.getUserSocket(), new CLoginSuccess(userId, sessionId, status));
    }

    @Override
    public void updateSessionStatus(Long sessionId, UserSessionStatus status) {
        FrontendUserSession frontendUserSession = sessions.get(sessionId);
        frontendUserSession.setStatus(status);
        NodeMessageSender.sendMessage(frontendUserSession.getUserSocket(), new CUpdateSessionStatus(status));
    }

    @Override
    public FrontendUserSession getSessionByUserId(Long userId) {
        final FrontendUserSession[] session = new FrontendUserSessionImpl[1];
        sessions.forEach((s, frontendUserSession) -> {
            if (Objects.equals(frontendUserSession.getUserId(), userId) && !(frontendUserSession.getStatus().equals(UserSessionStatus.INACTIVE))) {
                session[0] = frontendUserSession;
            }
        });
        return session[0];
    }

    @Override
    public FrontendUserSession getSessionBySessionId(Long sessionId) {
        final FrontendUserSession[] session = new FrontendUserSessionImpl[1];
        sessions.forEach((s, frontendUserSession) -> {
            if (Objects.equals(frontendUserSession.getSessionId(), sessionId) && !(frontendUserSession.getStatus().equals(UserSessionStatus.INACTIVE))) {
                session[0] = frontendUserSession;
            }
        });
        return session[0];
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void disconnectUser(Long userId) {
        sessions.remove(getSessionId(userId));
    }

    @Override
    public void setMasterIsReady(boolean masterIsReady) {
        this.masterIsReady = true;
    }
}
