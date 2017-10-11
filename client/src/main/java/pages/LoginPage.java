package pages;

import base.frontend.UserSessionStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;
import main.ClientImpl;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.clientMessages.fromClient.FLogin;

import java.io.IOException;

public class LoginPage {
    @FXML
    private Button loginBtn;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField login;
    @FXML
    private TextField serverAddress;
    private ClientImpl main;

    public LoginPage() {
    }

    @FXML
    private void loginBtnPressed() {
        try {
            String text = serverAddress.getText();
            main.connectToServer(text);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.TRANSPARENT);
            alert.setTitle("Error");
            alert.setHeaderText("Can't connect to the server");
            alert.show();
            e.printStackTrace();
        }
        NodeMessageSender.sendMessage(main.getFrontendSocket(), new FLogin(login.getText(), passwordField.getText()));
        main.setStatus(UserSessionStatus.IN_LOGIN);
        main.waitForStatusUpdate();
        if (main.getStatus().equals(UserSessionStatus.WRONG_LOGIN_INFO)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.DECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong login info");
            alert.show();
        } else if (main.getStatus().equals(UserSessionStatus.CONNECTED)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.DECORATED);
            alert.setTitle("Error");
            alert.setHeaderText("Connected!");
            alert.show();
        }
    }

    public void setMain(ClientImpl main) {
        this.main = main;
    }
}


