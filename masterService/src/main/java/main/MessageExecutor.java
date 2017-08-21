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
import java.util.Queue;

public class MessageExecutor implements Runnable {
    final private Map<Class<? extends Node>, List<Socket>> connections;
    private final TickSleeper tickSleeper = new TickSleeper();
    private Queue<Message> unsortedMessages;
    private List<Socket> sockets;
    private MasterServiceImpl masterService;


    public MessageExecutor(Map<Class<? extends Node>, List<Socket>> connections, Queue<Message> unsortedMessages, List<Socket> sockets, MasterServiceImpl masterService) {
        this.connections = connections;
        this.unsortedMessages = unsortedMessages;
        this.sockets = sockets;
        this.masterService = masterService;
        Thread mExecutor = new Thread(this);
        mExecutor.setName("MessageExecutor");
        mExecutor.start();
    }


    @Override
    public void run() {
        System.out.println("MessageExecutor started!");
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
                        if (e == null) {
                            System.out.println(msg);
                        }
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
