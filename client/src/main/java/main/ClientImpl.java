package main;

import base.Client.Client;
import base.frontend.FrontendUserSession;
import base.frontend.UserSessionStatus;
import base.masterService.Message;
import base.masterService.nodes.Address;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;
import pages.MainPage;
import pages.PageTemplate;
import utils.MessageSystem.NodeMessageReceiver;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.clientMessages.fromClient.FDisconnect;
import utils.logger.LoggerImpl;

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
    private AnchorPane rootLayout;
    private int outPort;
    private String outHost;
    private ClientStatus clientStatus;
    private PageTemplate page;
    private FrontendUserSession frontendUserSession;
    private Long userId = null;
    private UserSessionStatus status;
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
        LoggerImpl.createLogger("Client");
        log = LoggerImpl.getLogger();


        launch(args);
    }

    @Override
    public FrontendUserSession getFrontendUserSession() {
        return frontendUserSession;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public Socket getFrontendSocket() {
        return frontendSocket;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Login");

        showLayout("/pageResources/loginPage.fxml", ClientStatus.STARTED);

    }

    private void showLayout(String name, ClientStatus status) {
        FXMLLoader loader = new FXMLLoader();
        URL resource = this.getClass().getResource(name);
        loader.setLocation(resource);
        try {
            rootLayout = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        page = loader.getController();
        page.setMain(this);
        primaryStage.show();
        this.clientStatus = status;
    }

    public void quit() {
        closeWindow();
        NodeMessageSender.sendMessage(getFrontendSocket(), new FDisconnect(userId));
    }

    public void connectSuccessful() {
        closeWindow();
        showLayout("/pageResources/MainPage.fxml", ClientStatus.IN_FIGHT);
        Thread mainPageThread = new Thread((MainPage) page);
        mainPageThread.setName("Main page");
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

    public UserSessionStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(UserSessionStatus status) {
        this.status = status;
    }

    @Override
    public void waitForStatusUpdate() {
        while (status.equals(UserSessionStatus.IN_LOGIN)) {
            execMessage();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateUserSession(Long userID, String userName, UserSessionStatus status, Long fightTime) {
        if (clientStatus.equals(ClientStatus.IN_FIGHT)) {
            //TODO
            MainPage mainPage = (MainPage) page;
            mainPage.updatePage(userID, userName, status, fightTime);
        }
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
}
