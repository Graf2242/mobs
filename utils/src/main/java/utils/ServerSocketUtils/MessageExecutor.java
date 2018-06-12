package utils.ServerSocketUtils;

import base.utils.Message;
import org.apache.logging.log4j.Logger;
import utils.Serialization.SerializerHelper;
import utils.logger.LoggerImpl;
import utils.logger.UncaughtExceptionLog4j2Handler;
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
    Logger log;


    public MessageExecutor(Queue<Message> unsortedMessages, List<Socket> sockets, Logger log) {
        this.unsortedMessages = unsortedMessages;
        this.sockets = sockets;
        Thread mExecutor = new Thread(this);
        mExecutor.setName("MessageExecutor");
        mExecutor.setUncaughtExceptionHandler(new UncaughtExceptionLog4j2Handler(log));
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
                        Message e = SerializerHelper.deserializeString(msg);
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
