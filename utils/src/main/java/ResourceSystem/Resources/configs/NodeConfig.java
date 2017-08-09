package ResourceSystem.Resources.configs;

import java.io.Serializable;

public class NodeConfig implements Serializable {
    String ip;
    String port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
