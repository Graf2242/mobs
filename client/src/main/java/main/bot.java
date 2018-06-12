package main;

import base.frontend.UserSessionStatus;
import utils.MessageSystem.NodeMessageSender;
import utils.MessageSystem.messages.clientMessages.fromClient.FConnectLobby;
import utils.MessageSystem.messages.clientMessages.fromClient.FCreateAccount;
import utils.MessageSystem.messages.clientMessages.fromClient.FLogin;

import java.io.IOException;
import java.util.Random;

public class bot extends ClientImpl implements Runnable {

    private static Random random;
    private static int login = 0;

    public static void main(String[] args) {
        try {
            random = new Random();
//            new bot().createAccounts(1000);
            makeHardLoad(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void makeHardLoad(int count) throws InterruptedException {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            sleep();
            bot bot = new bot();
            Thread thread = new Thread(bot);
            thread.start();
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(random.nextInt(10) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void sleep(int time) {
        try {
            Thread.sleep(random.nextInt(time) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void createAccounts(int count) throws InterruptedException {
        try {
            connectToServer("localhost:9093");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        InetAddress localHost = InetAddress.getLocalHost();
//        Socket frontendSocket = new Socket("localhost", 9093);
        for (int i = 0; i < count; i++) {
            String login = "bot" + bot.login++;
            NodeMessageSender.sendMessage(frontendSocket, new FCreateAccount(login, login));
            Thread.sleep(100);
        }
    }

    @Override
    public void run() {
//        Socket frontendSocket = null;
//        try {
//            InetAddress localHost = InetAddress.getLocalHost();
//            frontendSocket = new Socket("localhost", 9093);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            connectToServer("localhost:9093");
        } catch (IOException e) {
            e.printStackTrace();
        }
        sleep();
        String login = "bot" + bot.login++;
        NodeMessageSender.sendMessage(frontendSocket, new FLogin(login, login));
        System.out.println("Run bot " + login);
        setStatus(UserSessionStatus.IN_LOGIN);
        sleep();
        waitForStatusUpdate();
        NodeMessageSender.sendMessage(frontendSocket, new FConnectLobby(getSessionId()));
        System.out.println("Connect lobby message " + login);
        try {
            if (frontendSocket != null) {
                frontendSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Bot ends " + login);
    }
}
