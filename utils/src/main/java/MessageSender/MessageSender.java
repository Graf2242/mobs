package MessageSender;

import java.util.Objects;

public class MessageSender {
    private MessageSender messageSender;

    private MessageSender() {
    }

    public MessageSender instance() {
        if (Objects.equals(messageSender, null)) {
            messageSender = new MessageSender();
        }
        return messageSender;
    }

    public void sendMessage() {

    }

}
