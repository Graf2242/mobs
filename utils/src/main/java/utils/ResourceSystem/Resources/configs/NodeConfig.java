package utils.ResourceSystem.Resources.configs;

import java.io.Serializable;

public class NodeConfig implements Serializable {
    protected String ip;
    protected String masterPort;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMasterPort() {
        return masterPort;
    }

    public void setMasterPort(String masterPort) {
        this.masterPort = masterPort;
    }
}
