package main;

import base.gameMechanics.GameMechanics;
import base.gameMechanics.GameMechanicsSession;
import org.apache.logging.log4j.Logger;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.Frontend.FLetUserConnectDirectly;
import utils.MessageSystem.messages.clientMessages.toClient.CUpdateSession;
import utils.ServerSocketUtils.UDPMessageExecutor;
import utils.logger.LoggerImpl;
import utils.logger.UncaughtExceptionLog4j2Handler;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class UDPGameMechanicsImpl extends GameMechanicsImpl {
    private static Logger log;
    private final UDPMessageExecutor udpMessageExecutor;
    private int port;
    private Map<Long, String> users = new HashMap<>();
    private DatagramSocket socket;

    public UDPGameMechanicsImpl(String configPath) {
        super(configPath);
        //TODO Протянуть порт нормально
        try {
            port = 7777;
            socket = new DatagramSocket(port);
        } catch (SocketException e) {

        }
        udpMessageExecutor = new UDPMessageExecutor(unhandledMessages, socket, this);
    }


    public static void main(String[] args) {
        String arg = null;
        try {
            arg = args[0];
        } catch (Exception ignored) {
        }
        String configPath = Objects.equals(arg, null) ? "config.xml" : arg;
        log = LoggerImpl.getLogger("GameMechanics");

        GameMechanics gameMechanics = new UDPGameMechanicsImpl(configPath);
        Thread gameMechanicsThread = new Thread(gameMechanics);
        gameMechanicsThread.setName("GameMechanics");
        gameMechanicsThread.setUncaughtExceptionHandler(new UncaughtExceptionLog4j2Handler(log));
        gameMechanicsThread.start();
    }

    @Override
    public void run() {
        setTickTimeMs(10L);
        super.run();
    }

    @Override
    public void connectClient(String ip, int port, Long userId) {
        users.put(userId, ip + ':' + port);
    }

    @Override
    public void registerGMSession(Set<Long> userIDs) throws UnknownHostException {
        NodeMessageSender.sendMessage(getMasterService(), new FLetUserConnectDirectly(getAddress(), userIDs, InetAddress.getLocalHost().getHostAddress(), port));
        super.registerGMSession(userIDs);
    }

    @Override
    protected void sendUpdates() {
        for (GameMechanicsSession gameMechanicsSession : getSessions()) {
            for (Long userId : gameMechanicsSession.getUserIds()) {
                String s = users.get(userId);
                if (s == null) continue;
                String[] split = s.split(":");
                try {
                    NodeMessageSender.sendUDPMessage(split[0], Integer.parseInt(split[1]), new CUpdateSession(gameMechanicsSession, userId));
                    log.info("UDP sent");
                } catch (IOException e) {

                }
            }
        }
    }
}
