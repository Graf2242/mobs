package graf.less;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class GameLoop {

    public static void main(String[] args) {
        GameState gameState = new GameState();
        createTeams(gameState);
        printFullMobsState(gameState);

        ArrayList<Action> actions = new ArrayList<>();

        mainLoop(gameState, actions);
    }

    private static void createTeams(GameState gameState) {
        Random random = new Random();

        System.out.println("Введите количество участников для одной команды:");
        Scanner scanner = new Scanner(System.in);
        int participantNumberEachTeam = scanner.nextInt();
        int firstTeamPlayers;
        int secondTeamPlayers;

        do {
            System.out.println("Введите количество игроков в 1й команде:");
            firstTeamPlayers = scanner.nextInt();
        }
        while (firstTeamPlayers > participantNumberEachTeam);

        do {
            System.out.println("Введите количество игроков во 2й команде:");
            secondTeamPlayers = scanner.nextInt();
        } while (secondTeamPlayers > participantNumberEachTeam);
        Mob[] team1 = new Mob[participantNumberEachTeam];
        Mob[] team2 = new Mob[participantNumberEachTeam];
        gameState.setTeam1(team1);
        gameState.setTeam2(team2);

        for (int i = 0; i < firstTeamPlayers; i++) {
            team1[i] = new Avatar("t1.Avatar" + i, makeDefaultHealth(random), makeDefaultDamage(random), team2, random);
        }
        for (int i = firstTeamPlayers; i < participantNumberEachTeam; i++) {
            team1[i] = new Npc("t1.NPC" + i, makeDefaultHealth(random), makeDefaultDamage(random), team2, random);
        }
        for (int i = 0; i < secondTeamPlayers; i++) {
            team2[i] = new Avatar("t2.Avatar" + i, makeDefaultHealth(random), makeDefaultDamage(random), team1, random);
        }
        for (int i = secondTeamPlayers; i < participantNumberEachTeam; i++) {
            team2[i] = new Npc("t2.NPC" + i, makeDefaultHealth(random), makeDefaultDamage(random), team1, random);
        }
    }

    private static void mainLoop(GameState gameState, ArrayList<Action> actions) {
        Collections.addAll(actions, gameState.getTeam1());
        Collections.addAll(actions, gameState.getTeam2());

        Mob[] team1 = gameState.getTeam1();
        Mob[] team2 = gameState.getTeam2();

        for (Mob mob : team1) {
            actions.add(new DefineAttackOptionsAction(mob));
            actions.add(new AttackAction(mob));
            actions.add(new PrintStateAction(mob));
        }
        for (Mob mob : team2) {
            actions.add(new DefineAttackOptionsAction(mob));
            actions.add(new AttackAction(mob));
            actions.add(new PrintStateAction(mob));
        }

        while (!isGameOver(gameState)) {
            for (Action action : actions) {
                if (teamIsDead(team1) || teamIsDead(team2)) {
                    printFullMobsState(gameState);
                    break;
                }
                action.step();
            }
            System.out.println("----------------");
        }
    }

    private static boolean isGameOver(GameState gameState) {
        Mob[] team1 = gameState.getTeam1();
        Mob[] team2 = gameState.getTeam2();

        if (teamIsDead(team1) && teamIsDead(team2)) {
            System.out.println("Обе команды мертвы");
            return true;
        }
        if (teamIsDead(team1)) {
            System.out.println(("Первая команда мертва"));
            return true;
        }
        if (teamIsDead(team2)) {
            System.out.println(("Вторая команда мертва"));
            return true;
        }
        return false;
    }

    private static boolean teamIsDead(Mob[] team) {
        for (Mob mob : team) {
            if (!mob.isDead()) return false;
        }
        return true;
    }


    private static void printFullMobsState(GameState gameState) {
        Mob[] team1 = gameState.getTeam1();
        Mob[] team2 = gameState.getTeam2();

        System.out.println("Команда 1:");
        for (Mob mob : team1) {
            System.out.println(mob.getName() + " HP: " + mob.getHealth() + " damage: " + mob.getDamage() + " critChance: " + mob.getCriticalDamageChance());
        }
        System.out.println("Команда 2:");
        for (Mob mob : team2) {
            System.out.println(mob.getName() + " HP: " + mob.getHealth() + " damage: " + mob.getDamage() + " critChance: " + mob.getCriticalDamageChance());
        }
    }


    private static int makeDefaultHealth(Random random) {
        final int basicHealth = 100;
        final int additionalHealth = 100;
        return basicHealth + random.nextInt(additionalHealth);
    }

    private static int makeDefaultDamage(Random random) {
        final int basicDamage = 10;
        final int additionalDamage = 10;
        return basicDamage + random.nextInt(additionalDamage);
    }
}
