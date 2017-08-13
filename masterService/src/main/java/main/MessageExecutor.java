package main;

import Serialization.Serializator;
import masterService.Message;
import masterService.nodes.Node;
import tickSleeper.TickSleeper;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class MessageExecutor implements Runnable {
    final private Map<Class<? extends Node>, List<Socket>> connections;
    private final TickSleeper tickSleeper = new TickSleeper();
    private List<Message> unsortedMessages;
    private List<Socket> sockets;


    public MessageExecutor(Map<Class<? extends Node>, List<Socket>> connections, List<Message> unsortedMessages, List<Socket> sockets) {
        this.connections = connections;
        this.unsortedMessages = unsortedMessages;
        this.sockets = sockets;
        Thread mExecutor = new Thread(this);
        mExecutor.setName("MessageExecutor");
        mExecutor.start();
    }


    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            tickSleeper.tickStart();
            for (Socket socket : sockets) {
                try {
                    DataInputStream din = new DataInputStream(socket.getInputStream());
                    String msg;
                    while (din.available() > 0) {
                        msg = din.readUTF();
                        Message e = Serializator.deserializeString(msg);
                        unsortedMessages.add(e);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            tickSleeper.tickEnd();
        }
    }
}
