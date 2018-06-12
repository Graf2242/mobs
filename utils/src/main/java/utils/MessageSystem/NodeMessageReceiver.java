package utils.MessageSystem;

import base.utils.Message;
import org.apache.logging.log4j.Logger;
import utils.Serialization.SerializerHelper;
import utils.logger.LoggerImpl;
import utils.logger.UncaughtExceptionLog4j2Handler;
import utils.tickSleeper.TickSleeper;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;

public class NodeMessageReceiver implements Runnable {
    private final Queue<Message> messages;
    private DataInputStream dis;
    private Logger log;

    public NodeMessageReceiver(Queue<Message> messages, Socket socket) {
        this.messages = messages;
        log = LoggerImpl.getLogger();

        try {
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            log.error(e);
        }
        Thread messageReceiver = new Thread(this);
        messageReceiver.setName("MessageReceiver");
        messageReceiver.setUncaughtExceptionHandler(new UncaughtExceptionLog4j2Handler(log));
        messageReceiver.start();
    }

    @Override
    public void run() {
        TickSleeper tickSleeper = new TickSleeper();
        //noinspection InfiniteLoopStatement
        while (true) {
            tickSleeper.tickStart();
            String messageText;
            try {
                while (dis.available() > 0) {
                    messageText = dis.readUTF();
                    Message message = SerializerHelper.deserializeString(messageText);
                    this.messages.add(message);
                }
            } catch (IOException e) {
//                log.error(e);
            }
            tickSleeper.tickEnd();
        }
    }
}
