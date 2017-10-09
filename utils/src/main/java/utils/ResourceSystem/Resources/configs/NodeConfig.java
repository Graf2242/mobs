package utils.ResourceSystem.Resources.configs;

import java.io.Serializable;

public class NodeConfig implements Serializable {
    protected String ip;
    protected String port;

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
