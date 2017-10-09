package base.masterService;

import java.net.Socket;
import java.util.List;

public interface Connector {
    List<Socket> getSockets();
}
