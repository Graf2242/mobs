package main;

import base.Client.Client;
import base.frontend.UserSessionStatus;
import base.masterService.Message;
import base.masterService.nodes.Address;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pages.LoginPage;
import utils.MessageSystem.NodeMessageReceiver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientImpl extends Application implements Client {
    private static String configPath;
    private Stage primaryStage;
    private AnchorPane rootLayout;
    private int outPort;
    private String outHost;

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private Long userId = null;
    private UserSessionStatus status;
    private Socket frontendSocket;
    private Queue<Message> messages = new LinkedBlockingQueue<>();

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
    public void setStatus(UserSessionStatus status) {
        this.status = status;
        switch (status) {
            case FIGHT: {

                break;
            }
            case WRONG_LOGIN_INFO: {

            }
        }
    }

    @Override
    public Socket getFrontendSocket() {
        return frontendSocket;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Login");

        initRootLayout();
    }

    private void initRootLayout() {
        FXMLLoader loader = new FXMLLoader();
//        VFS vfs = new VFSImpl("");
//        File file = new File("../");
//        for (File file1 : file.listFiles()) {
//            System.out.println(file1);
//        }
        URL resource = this.getClass().getResource("/pageResources/loginPage.fxml");
        loader.setLocation(resource);
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

    public void connectToServer(String address) throws IOException {
        String[] s = address.split(":");
        outPort = 0;
        this.frontendSocket = new Socket(s[0], Integer.parseInt(s[1]), InetAddress.getByName("localhost"), outPort);
        new NodeMessageReceiver(messages, frontendSocket);
    }

    private void execMessage() {
        if (!messages.isEmpty()) {
            messages.poll().exec(this);
        }
    }

    public UserSessionStatus getStatus() {
        return status;
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
