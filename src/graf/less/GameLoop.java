package graf.less;

import java.util.Random;
import java.util.Scanner;

public class GameLoop {


    public static void main(String[] args) {
        Random random = new Random();

        createFight(random);
    }

    private static void createFight(Random random) {
        System.out.println("Введите количество участников для одной команды:");
        Scanner scanner = new Scanner(System.in);
        int participantNumberEachTeam = scanner.nextInt();
        int firstTeamPlayers;
        int teamsCount;

        System.out.println("Введите количество команд:");
        teamsCount = scanner.nextInt();
        do {
            System.out.println("Введите количество игроков в 1й команде:");
            firstTeamPlayers = scanner.nextInt();
        }
        while (firstTeamPlayers > participantNumberEachTeam);

        new Fight(teamsCount, participantNumberEachTeam, firstTeamPlayers, random);
    }


}
