package utils.ServerSocketUtils;

import base.masterService.Connector;
import org.apache.logging.log4j.Logger;
import utils.tickSleeper.TickSleeper;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ConnectorImpl implements Runnable, Connector {
    private final List<Socket> sockets;
    private final TickSleeper tickSleeper = new TickSleeper();
    private final String ip;
    private final String port;
    private Logger log;

    public ConnectorImpl(String ip, String port, List<Socket> sockets, Logger log) {
        this.ip = ip;
        this.port = port;
        this.log = log;
        this.sockets = sockets;
        Thread tConnector = new Thread(this);
        tConnector.setName("HTTPConnector");
        tConnector.start();
    }

    @Override
    public List<Socket> getSockets() {
        return sockets;
    }

    @Override
    public void run() {
        InetAddress inetAddress;
        ServerSocket serverSocket = null;
        try {
            inetAddress = Inet4Address.getByName(ip);
            serverSocket = new ServerSocket(Integer.parseInt(port), 10, inetAddress);
        } catch (IOException e) {
            log.fatal(e);
        }
        //noinspection InfiniteLoopStatement
        while (true) {
            tickSleeper.tickStart();
            try {
                Socket s = serverSocket != null ? serverSocket.accept() : null;
                sockets.add(s);
            } catch (IOException e) {
                log.error(e);
            }
            tickSleeper.tickEnd();
        }
    }
}
