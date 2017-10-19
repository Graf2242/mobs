package pages;

import base.frontend.UserSessionStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.ClientImpl;
import utils.tickSleeper.TickSleeper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class MainPage implements PageTemplate, Runnable {
    private ClientImpl main;
    @FXML
    private TextField mainField;

    private static String getUserDateFull(Long time) {
        if (Objects.equals(time, null)) {
            time = 0L;
        }
        Date date = new Date(time);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
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
        mainField.setText(String.format("MatchTime is %s", getUserDateFull(fightTime)));
    }

    @Override
    public void run() {
        TickSleeper sleeper = new TickSleeper();
        sleeper.setTickTimeMs(1000L);
        //noinspection InfiniteLoopStatement
        while (true) {
            sleeper.tickStart();
            main.execMessage();
            sleeper.tickEnd();
        }
    }
}
