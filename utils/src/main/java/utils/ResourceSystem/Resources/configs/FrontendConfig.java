package utils.ResourceSystem.Resources.configs;

import java.io.Serializable;

public class FrontendConfig implements Serializable {
    private String frontendPort;
    private String ip;
    private String port;

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

    public String getFrontendPort() {
        return frontendPort;
    }

    public void setFrontendPort(String frontendPort) {
        this.frontendPort = frontendPort;
    }
}
