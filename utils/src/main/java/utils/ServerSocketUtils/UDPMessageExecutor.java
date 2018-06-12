package utils.ServerSocketUtils;

import base.gameMechanics.GameMechanics;
import base.utils.Message;
import utils.Serialization.SerializerHelper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.Queue;

public class UDPMessageExecutor implements Runnable {
    private Queue<Message> unsortedMessages;
    private DatagramSocket socket;
    private GameMechanics gameMechanics;

    public UDPMessageExecutor(Queue<Message> unsortedMessages, DatagramSocket socket, GameMechanics gameMechanics) {
        this.unsortedMessages = unsortedMessages;
        this.socket = socket;
        this.gameMechanics = gameMechanics;
    }

    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String message = Arrays.toString(packet.getData());
            Message messageObj = SerializerHelper.deserializeString(message);
            unsortedMessages.add(messageObj);
            System.out.print("UDP reseived");
        }
    }
}
