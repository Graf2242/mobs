package pages;

import base.frontend.UserSessionStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import main.ClientImpl;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.clientMessages.fromClient.FConnectLobby;
import utils.logger.LoggerImpl;
import utils.tickSleeper.TickSleeper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class MainPage implements PageTemplate, Runnable {
    private ClientImpl main;
    @FXML
    private TextArea mainField;
    private String GMip;
    private int GMport;

    public void connectUDP(String ip, int port) {
        this.GMip = ip;
        this.GMport = port;
    }

    private static String getUserDateFull(Long time) {
        if (Objects.equals(time, null)) {
            time = 0L;
        }
        Date date = new Date(time);
        DateFormat formatter = new SimpleDateFormat("mm:ss.SSS");
        return formatter.format(date);
    }

    @Override
    public void setMain(ClientImpl main) {
        this.main = main;
    }

    @FXML
    private void onCloseBtnPressed(ActionEvent actionEvent) {
        main.quit();
    }

    public void updatePage(Long userID, String userName, UserSessionStatus status, Long fightTime) {
        writeText(String.format("MatchTime is %s", getUserDateFull(fightTime)));
    }


    private void writeText(String text) {
        mainField.appendText(text);
        mainField.appendText(System.lineSeparator());
    }

    @Override
    public void run() {
        writeText("Loading");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            LoggerImpl.getLogger().fatal(e);
        }
        TickSleeper sleeper = new TickSleeper();
        sleeper.setTickTimeMs(1000L);
        //noinspection InfiniteLoopStatement
        while (true) {
            sleeper.tickStart();
            main.execMessage();
            sleeper.tickEnd();
        }
    }

    @FXML
    private void OnLoginToLobbyPressed(ActionEvent actionEvent) {
        try {
            NodeMessageSender.sendMessage(main.getFrontendSocket(), new FConnectLobby(main.getSessionId()));
            main.setStatus(UserSessionStatus.SEARCH);
        } catch (Exception ex) {
            main.getLog().fatal(ex);
        }
    }
}
