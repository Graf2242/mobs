package utils.MessageSystem;

import base.masterService.Message;
import utils.Serialization.Serializator;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;

public class NodeMessageSender {

    public static void sendMessage(Socket socket, Message message) {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(Serializator.serializeToString(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (dos != null) {
            try {
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessages(Socket socket, List<Message> messages) {
        PrintStream ps = null;
        try {
            ps = new PrintStream(socket.getOutputStream());
            for (Message message : messages) {
                ps.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (ps != null) {
            ps.flush();
            ps.close();
        }
    }
}
