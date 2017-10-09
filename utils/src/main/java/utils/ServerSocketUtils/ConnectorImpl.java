package utils.ServerSocketUtils;

import base.masterService.Connector;
import utils.tickSleeper.TickSleeper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ConnectorImpl implements Runnable, Connector {
    final ServerSocket serverSocket;
    final List<Socket> sockets;
    private final TickSleeper tickSleeper = new TickSleeper();

    public ConnectorImpl(ServerSocket serverSocket, List<Socket> sockets) {
        this.serverSocket = serverSocket;
        Thread tConnector = new Thread(this);
        tConnector.setName("ServerSocketUtils");
        tConnector.start();
        this.sockets = sockets;
    }

    @Override
    public List<Socket> getSockets() {
        return sockets;
    }

    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            tickSleeper.tickStart();
            try {
                Socket s = serverSocket.accept();
                sockets.add(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
            tickSleeper.tickEnd();
        }
    }
}
