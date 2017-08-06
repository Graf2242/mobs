package graf.server.Frontend;

import ResourceSystem.ResourceFactory;
import ResourceSystem.Resources.ServerConfig;
import frontend.Frontend;
import graf.server.Base.Address;
import graf.server.Base.MasterService;
import graf.server.Utils.TickSleeper;
import messages.DBService.DBFindUserIdMessage;
import messages.Lobby.LAddUser;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FrontendImpl extends AbstractHandler implements Frontend {
    private final Address address = new Address();
    private final MasterService masterService;
    private final Map<Long, FrontendUserSessionImpl> sessions = new HashMap<>();
    private String configPath;
    private ResourceFactory resourceFactory;

    public FrontendImpl(MasterService MasterService, String configPath) {
        this.masterService = MasterService;
        this.configPath = configPath;
        this.resourceFactory = ResourceFactory.instance();
    }

    @Override
    public Map<Long, FrontendUserSessionImpl> getSessions() {
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
    public MasterService getMasterService() {
        return masterService;
    }

    @Override
    public void run() {
        getMasterService().register(this);
        startFrontend();
        TickSleeper tickSleeper = new TickSleeper();
        //noinspection InfiniteLoopStatement
        while (true) {
            tickSleeper.tickStart();
            masterService.execNodeMessages(this);
            tickSleeper.tickEnd();
        }
    }

    @Override
    public Long getSessionId(String userName) {
        final FrontendUserSessionImpl[] userSession = new FrontendUserSessionImpl[1];
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
        connector.setHost(config.getIp());
        connector.setPort(Integer.parseInt(config.getPort()));
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

        FrontendUserSessionImpl userSession;

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
        //   masterService.addMessage(new DBCreateUser(address, userName, pass, userSession.getSessionId()));

        //Login
        if (userId == null) {
            masterService.addMessage(new DBFindUserIdMessage(address, userName, pass, userSession.getSessionId()));
            response.getWriter().println("<h1>Wait for authorization</h1><meta http-equiv=\"refresh\" content=\"1\">");
        } else if (userSession.getStatus().equals(UserSessionStatus.FIGHT)) {
            response.getWriter().println("<h1>User name: " + userSession.getUserName() + " Id: " + userSession.getUserId() + " , sessionTime = " + getUserDateFull(userSession.getSessionTime()) + "</h1><meta http-equiv=\"refresh\" content=\"1\">");
        } else {
            response.getWriter().println("<h1>Fight loading</h1><meta http-equiv=\"refresh\" content=\"1\">");
        }
    }

    @Override
    public boolean updateUserId(Long sessionId, Long userId) {
        if (sessions.get(sessionId).getUserId() == null) {
            sessions.get(sessionId).setUserId(userId);
            masterService.addMessage(new LAddUser(address, userId, sessions.get(sessionId).getUserName()));
            return true;
        }
        return false;
    }

    @Override
    public void updateSessionStatus(Long sessionId, UserSessionStatus status) {
        FrontendUserSessionImpl frontendUserSession = sessions.get(sessionId);
        frontendUserSession.setStatus(status);
    }

    @Override
    public FrontendUserSessionImpl getSessionByUserId(Long userId) {
        final FrontendUserSessionImpl[] session = new FrontendUserSessionImpl[1];
        sessions.forEach((s, frontendUserSession) -> {
            if (Objects.equals(frontendUserSession.getUserId(), userId) && !(frontendUserSession.getStatus().equals(UserSessionStatus.INACTIVE))) {
                session[0] = frontendUserSession;
            }
        });
        return session[0];
    }

    @Override
    public FrontendUserSessionImpl getSessionBySessionId(Long sessionId) {
        final FrontendUserSessionImpl[] session = new FrontendUserSessionImpl[1];
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
