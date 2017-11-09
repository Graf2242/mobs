package utils.ServerSocketUtils;

import base.masterService.Message;
import utils.Serialization.Serializator;
import utils.logger.LoggerImpl;
import utils.tickSleeper.TickSleeper;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Queue;

public class MessageExecutor implements Runnable {
    private final TickSleeper tickSleeper = new TickSleeper();
    private Queue<Message> unsortedMessages;
    private List<Socket> sockets;


    public MessageExecutor(Queue<Message> unsortedMessages, List<Socket> sockets) {
        this.unsortedMessages = unsortedMessages;
        this.sockets = sockets;
        Thread mExecutor = new Thread(this);
        mExecutor.setName("MessageExecutor");
        mExecutor.start();
    }


    @Override
    public void run() {
        LoggerImpl.getLogger().info("MessageExecutor started!");
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
                            LoggerImpl.getLogger().error(msg);
                        } else {
                            e.setOutSocket(socket);
                            unsortedMessages.add(e);
                        }
                    }
                } catch (IOException e) {
                    LoggerImpl.getLogger().error(e);
                }
            }
            tickSleeper.tickEnd();
        }
    }
}
