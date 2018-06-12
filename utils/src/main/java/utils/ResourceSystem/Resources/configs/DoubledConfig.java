package utils.ResourceSystem.Resources.configs;

import java.io.Serializable;

public class DoubledConfig implements Serializable {
    private String secondPort;
    private String ip;
    private String masterPort;

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

    public String getSecondPort() {
        return secondPort;
    }

    public void setSecondPort(String secondPort) {
        this.secondPort = secondPort;
    }
}
