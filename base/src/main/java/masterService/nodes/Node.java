package masterService.nodes;

import java.io.Serializable;
import java.net.Socket;

public interface Node extends Serializable {
    Address getAddress();

    Socket getMasterService();
}
