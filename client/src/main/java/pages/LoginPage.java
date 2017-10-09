package pages;

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
        main.waitForStatusUpdate();

    }

    public void setMain(ClientImpl main) {
        this.main = main;
    }
}


