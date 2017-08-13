package MessageSystem;

import Serialization.Serializator;
import masterService.Message;
import masterService.nodes.Node;
import tickSleeper.TickSleeper;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;

public class NodeMessageReceiver implements Runnable {
    final Queue<Message> messages;
    private final Node node;
    private DataInputStream dis;

    public NodeMessageReceiver(Queue<Message> messages, Socket socket, Node node) {
        this.messages = messages;
        this.node = node;

        try {
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread messageReceiver = new Thread(this);
        messageReceiver.setName("MessageReceiver");
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
                    Message message = Serializator.deserializeString(messageText);
                    this.messages.add(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            tickSleeper.tickEnd();
        }
    }
}
