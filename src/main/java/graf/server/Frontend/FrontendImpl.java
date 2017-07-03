package graf.server.Frontend;

import graf.server.Base.Address;
import graf.server.Base.Frontend;
import graf.server.Base.MasterService;
import graf.server.MasterService.messages.AccountServer.ASFindUserIdMessage;
import graf.server.Utils.TickSleeper;
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

public class FrontendImpl extends AbstractHandler implements Frontend {
    private final Address address = new Address();
    private final MasterService masterService;
    private final Map<String, FrontendUserSession> sessions = new HashMap<>();

    public FrontendImpl(MasterService MasterService) {
        this.masterService = MasterService;
    }

    private static String getUserDateFull(Long time) {
        Date date = new Date(time);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(date);
    }

    public MasterService getMasterService() {
        return masterService;
    }

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

    public Integer getSessionId(String userName) {
        return sessions.get(userName).getSessionId();
    }

    private void startFrontend() {
        Server server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.addConnector(connector);
        server.setHandler(this);

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        String name = "Graf";
        sessions.computeIfAbsent(name, FrontendUserSession::new);
        FrontendUserSession session = sessions.get(name);
        Integer userId = session.getUserId();
        if (userId == null) {
            masterService.addMessage(new ASFindUserIdMessage(address, name));
            response.getWriter().println("<h1>Wait for authorization</h1>");
        } else {
            response.getWriter().println("<h1>User name: " + session.getUserName() + " Id: " + session.getUserId() + " , sessionTime = " + getUserDateFull(session.getSessionTime()) + "</h1>");
        }
    }

    public boolean updateUserId(String userName, Integer userId) {
        if (sessions.get(userName).getUserId() == null) {
            sessions.get(userName).setUserId(userId);
            return true;
        }
        return false;
    }

    @Override
    public FrontendUserSession getSessionByUserId(Integer userId) {
        final FrontendUserSession[] session = new FrontendUserSession[1];
        sessions.forEach((s, frontendUserSession) -> {
            if (frontendUserSession.getUserId().equals(userId) && !(frontendUserSession.getStatus().equals(UserSessionStatus.Inactive))) {
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
