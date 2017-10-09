package utils.logger;


import java.io.IOException;


public class LoggerImpl {
    private String logName = "default";
    private String filePath = "../log" + logName + ".txt";


    public LoggerImpl(String logName) throws IOException {
        this.logName = logName;
//        Logger.
    }
}
