package main;

import MessageSystem.NodeMessageReceiver;
import MessageSystem.NodeMessageSender;
import MessageSystem.messages.DBService.DBFindUserIdMessage;
import MessageSystem.messages.Lobby.LAddUser;
import MessageSystem.messages.masterService.MRegister;
import ResourceSystem.ResourceFactory;
import ResourceSystem.Resources.configs.ServerConfig;
import frontend.Frontend;
import frontend.FrontendUserSession;
import frontend.UserSessionStatus;
import masterService.Message;
import masterService.nodes.Address;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import tickSleeper.TickSleeper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class FrontendImpl extends AbstractHandler implements Frontend {
    private final Address address = new Address();
    private final ServerConfig serverConfig;
    private final Map<Long, FrontendUserSession> sessions = new HashMap<>();
    Queue<Message> unhandledMessages = new LinkedBlockingQueue<>();
    private String configPath;
    private ResourceFactory resourceFactory;
    private Socket masterService;

    public FrontendImpl(String configPath) {
        this.configPath = configPath;
        this.resourceFactory = ResourceFactory.instance();

        serverConfig = (ServerConfig) resourceFactory.getResource(configPath);
        InetAddress address;
        try {
            address = InetAddress.getByName(serverConfig.getFrontend().getIp());
            masterService = new Socket(serverConfig.getMaster().getIp(),
                    Integer.parseInt(serverConfig.getMaster().getPort()), address,
                    Integer.parseInt(serverConfig.getFrontend().getPort()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        startFrontend();
        new NodeMessageReceiver(unhandledMessages, masterService, this);
        NodeMessageSender.sendMessage(masterService, new MRegister(this.address, this, serverConfig.getFrontend().getIp(), serverConfig.getFrontend().getPort()));

    }

    @Override
    public Map<Long, FrontendUserSession> getSessions() {
        return sessions;
    }

    private static String getUserDateFull(Long time) {
        if (Objects.equals(time, null)) {
            time = 0L;
        }
        Date date = new Date(time);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(date);
    }

    @Override
    public Socket getMasterService() {
        return masterService;
    }

    @Override
    public void run() {
        TickSleeper tickSleeper = new TickSleeper();
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

    private void startFrontend() {
        ServerConfig config = (ServerConfig) resourceFactory.getResource(configPath);
        Server server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setHost(config.getFrontend().getIp());
        connector.setPort(Integer.parseInt(config.getFrontend().getFrontendPort()));
        server.addConnector(connector);
        server.setHandler(this);

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Test
        FrontendUserSessionImpl userSession;
        userSession = new FrontendUserSessionImpl();
        sessions.put(userSession.getSessionId(), userSession);

        //
    }

    @Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        Long id = 0L;
        String userName = "Graf";
        String pass = "123";

        FrontendUserSession userSession;

        //noinspection ConstantConditions
        if (id < 0) {
            userSession = new FrontendUserSessionImpl();
            sessions.put(userSession.getSessionId(), userSession);
            userSession.setUserName(userName);
        } else {
            userSession = sessions.get(id);
        }
        if (userSession.getStatus().equals(UserSessionStatus.WRONG_LOGIN_INFO)) {
            response.getWriter().println("<h1>Wrong login info</h1>");
            return;
        }

        Long userId = userSession.getUserId();

        //CreateUser
        //   NodeMessageSender.sendMessage(new DBCreateUser(address, userName, pass, userSession.getSessionId()));

        //Login
        if (userId == null && userSession.getStatus().equals(UserSessionStatus.CONNECTED)) {
            userSession.setStatus(UserSessionStatus.IN_LOGIN);
            NodeMessageSender.sendMessage(masterService, new DBFindUserIdMessage(address, userName, pass, userSession.getSessionId()));
            response.getWriter().println("<h1>Wait for authorization</h1><meta http-equiv=\"refresh\" content=\"1\">");
        } else if (userSession.getStatus().equals(UserSessionStatus.FIGHT)) {
            response.getWriter().println("<h1>User name: " + userSession.getUserName() + " Id: " + userSession.getUserId() + " , sessionTime = " + getUserDateFull(userSession.getSessionTime()) + "</h1><meta http-equiv=\"refresh\" content=\"1\">");
        } else {
            response.getWriter().println("<h1>Fight loading</h1><meta http-equiv=\"refresh\" content=\"1\">");
        }
    }

    @Override
    public void updateUserId(Long sessionId, Long userId) {
        if (sessions.get(sessionId).getUserId() == null) {
            sessions.get(sessionId).setUserId(userId);
            NodeMessageSender.sendMessage(masterService, new LAddUser(address, userId, sessions.get(sessionId).getUserName()));
        }
    }

    @Override
    public void updateSessionStatus(Long sessionId, UserSessionStatus status) {
        FrontendUserSession frontendUserSession = sessions.get(sessionId);
        frontendUserSession.setStatus(status);
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
}
