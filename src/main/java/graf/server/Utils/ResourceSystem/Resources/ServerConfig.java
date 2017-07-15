package graf.server.Utils.ResourceSystem.Resources;

import graf.server.Base.Resource;

public class ServerConfig implements Resource {
    String ip;
    String port;

    public ServerConfig() {
    }

    public ServerConfig(String ip, String port) {

        this.ip = ip;
        this.port = port;
    }

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
