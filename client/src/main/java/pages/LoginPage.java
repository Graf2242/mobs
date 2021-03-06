package pages;

import base.frontend.UserSessionStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;
import main.ClientImpl;
import org.apache.logging.log4j.Logger;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.clientMessages.fromClient.FCreateAccount;
import utils.MessageSystem.messages.clientMessages.fromClient.FLogin;
import utils.logger.LoggerImpl;
import utils.logger.UncaughtExceptionLog4j2Handler;

import java.io.IOException;

public class LoginPage implements PageTemplate {
    @FXML
    private Button loginBtn;
    @FXML
    private Button createBtn;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField login;
    @FXML
    private TextField serverAddress;
    private ClientImpl main;
    private Logger log;

    public LoginPage() {
        log = LoggerImpl.getLogger();
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionLog4j2Handler(log));
    }

    @FXML
    private void createBtnPressed() {
        connect();
        NodeMessageSender.sendMessage(main.getFrontendSocket(), new FCreateAccount(login.getText(), passwordField.getText()));
        main.setStatus(UserSessionStatus.IN_LOGIN);
        main.waitForStatusUpdate();
        if (main.getClientStatus().getStatus().equals(UserSessionStatus.WRONG_LOGIN_INFO)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.DECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong login info");
            alert.show();
        } else if (main.getClientStatus().getStatus().equals(UserSessionStatus.CONNECTED)) {
            main.connectSuccessful();
        }
    }

    @FXML
    private void loginBtnPressed() {
        connect();
        NodeMessageSender.sendMessage(main.getFrontendSocket(), new FLogin(login.getText(), passwordField.getText()));
        main.setStatus(UserSessionStatus.IN_LOGIN);
        main.waitForStatusUpdate();
        if (main.getClientStatus().getStatus().equals(UserSessionStatus.WRONG_LOGIN_INFO)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.DECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong login info");
            alert.show();
        } else if (main.getClientStatus().getStatus().equals(UserSessionStatus.CONNECTED)) {
            main.connectSuccessful();
        }
    }

    private void connect() {
        try {
            String text = serverAddress.getText();
            main.connectToServer(text);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.TRANSPARENT);
            alert.setTitle("Error");
            alert.setHeaderText("Can't connect to the server");
            alert.show();
            log.error(e);
        }
    }

    @Override
    public void setMain(ClientImpl main) {
        this.main = main;
    }
}


