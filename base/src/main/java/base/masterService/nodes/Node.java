package base.masterService.nodes;

import java.io.Serializable;
import java.net.Socket;

public interface Node extends Serializable {
    Address getAddress();

    void setMasterIsReady(boolean masterIsReady);

    Socket getMasterService();
}
