package utils.MessageSystem;

import base.utils.Message;
import utils.Serialization.SerializerHelper;
import utils.logger.LoggerImpl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class NodeMessageSender {

    public static void sendMessage(Socket socket, Message message) {

        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(SerializerHelper.serializeToString(message));
        } catch (IOException e) {
            LoggerImpl.getLogger().error(e);
        }
        if (dos != null) {
            try {
                dos.flush();
            } catch (IOException e) {
                LoggerImpl.getLogger().error(e);
            }
        }
    }

    public static void sendUDPMessage(String ip, int port, Message message) throws IOException {
        byte[] buf;
        buf = SerializerHelper.serializeToString(message).getBytes();
        InetAddress inetAddress = InetAddress.getByName(ip);
        DatagramSocket socket = new DatagramSocket();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, inetAddress, port);
        socket.send(packet);
    }

    public void sendMessages(Socket socket, List<Message> messages) {
        PrintStream ps = null;
        try {
            ps = new PrintStream(socket.getOutputStream());
            for (Message message : messages) {
                ps.println(message);
            }
        } catch (IOException e) {
            LoggerImpl.getLogger().error(e);
        }
        if (ps != null) {
            ps.flush();
            ps.close();
        }
    }
}
