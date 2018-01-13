package main;

import base.Client.Client;
import base.frontend.UserSessionStatus;
import base.masterService.Message;
import base.masterService.nodes.Address;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;
import pages.MainPage;
import pages.PageTemplate;
import utils.MessageSystem.NodeMessageReceiver;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.clientMessages.fromClient.FDisconnect;
import utils.logger.LoggerImpl;
import utils.logger.UncaughtExceptionLog4j2Handler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientImpl extends Application implements Client {
    private static String configPath;
    private static Logger log;
    private Stage primaryStage;
    private Pane rootLayout;
    private int outPort;
    private ClientStatus clientStatus = new ClientStatus();
    private PageTemplate page;
    private Socket frontendSocket;

    private Queue<Message> messages = new LinkedBlockingQueue<>();

    public ClientImpl() {
    }

    public static void main(String[] args) {
        String arg = null;
        try {
            arg = args[0];
        } catch (Exception ignored) {
        }
        configPath = Objects.equals(arg, null) ? "config.xml" : arg;
        log = LoggerImpl.createLogger("Client");

        launch(args);
    }

    @Override
    public Logger getLog() {
        return log;
    }

    @Override
    public Socket getFrontendSocket() {
        return frontendSocket;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Login");

        showLayout("pageResources/loginPage.fxml");

    }

    private void showLayout(String name) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = ClassLoader.getSystemResource(name);
            loader.setLocation(resource);
            try {
                rootLayout = loader.load();
            } catch (IOException e) {
                log.error(e);
            }
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            page = loader.getController();
            page.setMain(this);
            primaryStage.show();
        } catch (Exception e) {
            log.fatal(e);
        }
    }

    public void quit() {
        closeWindow();
        NodeMessageSender.sendMessage(getFrontendSocket(), new FDisconnect(clientStatus.getUserId()));
    }

    public void connectSuccessful() {
        closeWindow();
        showLayout("pageResources/mainPage.fxml");
        Thread mainPageThread = new Thread((MainPage) page);
        mainPageThread.setName("Main page");
        mainPageThread.setUncaughtExceptionHandler(new UncaughtExceptionLog4j2Handler(log));
        mainPageThread.start();
    }

    private void closeWindow() {
        primaryStage.close();
    }

    public void connectToServer(String address) throws IOException {
        String[] s = address.split(":");
        outPort = 0;
        this.frontendSocket = new Socket(s[0], Integer.parseInt(s[1]), InetAddress.getByName("localhost"), outPort);
        new NodeMessageReceiver(messages, frontendSocket);
    }

    public void execMessage() {
        if (!messages.isEmpty()) {
            messages.poll().exec(this);
        }
    }

    @Override
    public void waitForStatusUpdate() {
        while (clientStatus.getStatus().equals(UserSessionStatus.IN_LOGIN)) {
            execMessage();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error(e);
            }
        }
    }

    public ClientStatus getClientStatus() {
        return clientStatus;
    }

    @Override
    public void updateUserSession(Long userID, String userName, UserSessionStatus status, Long fightTime) {
        MainPage mainPage = (MainPage) page;
        mainPage.updatePage(userID, userName, status, fightTime);

    }

    @Override
    public void setStatus(UserSessionStatus status) {
        clientStatus.setStatus(status);
    }

    @Override
    public void setUserId(Long userId) {
        clientStatus.setUserId(userId);
    }

    @Override
    public Address getAddress() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMasterIsReady(boolean masterIsReady) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Socket getMasterService() {
        return null;
    }

    public Long getSessionId() {
        return clientStatus.getUserSessionId();
    }

    @Override
    public void setSessionId(Long userSessionId) {
        clientStatus.setUserSessionId(userSessionId);
    }
}
