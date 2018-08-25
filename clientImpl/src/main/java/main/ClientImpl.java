package main;

import base.Client.Client;
import utils.MessageSystem.NodeMessageReceiver;
import base.frontend.UserSessionStatus;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import base.masterService.Message;
import pages.LoginPage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientImpl extends Application implements Client{
    private Stage primaryStage;
    private AnchorPane rootLayout;
    private static String configPath;
    private int outPort;
    private String outHost;
    private Long userId = null;

    @Override
    public void setStatus(UserSessionStatus status) {
        this.status = status;
        switch (status){
            case FIGHT:{

                break;
            }
            case WRONG_LOGIN_INFO:{

            }
        }
    }

    private UserSessionStatus status;

    @Override
    public Socket getFrontendSocket() {
        return frontendSocket;
    }

    private Socket frontendSocket;

    public static void main(String[] args) {
        String arg = null;
        try {
            arg = args[0];
        } catch (Exception ignored) {
        }
        configPath = Objects.equals(arg, null) ? "config.xml" : arg;

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Login");
        
        initRootLayout();
    }

    private void initRootLayout() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("../pages/loginPage.fxml"));
        try {
            rootLayout = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        LoginPage page = loader.getController();
        page.setMain(this);
        primaryStage.show();
    }

    private Queue<Message> messages = new LinkedBlockingQueue<>();

    public void connectToServer(String address) throws IOException {
        String[] s = address.split(":");
        outPort = 8080;
        this.frontendSocket = new Socket(s[0], Integer.parseInt(s[1]), InetAddress.getLocalHost(), outPort);
        NodeMessageReceiver receiver = new NodeMessageReceiver(messages, frontendSocket);
    }

    @Override
    public void waitForStatusUpdate() {

    }
}
