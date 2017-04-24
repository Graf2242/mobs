package graf.less;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class GameLoop {

    private static Mob[] team1;
    private static Mob[] team2;

    public static void main(String[] args) {

        createTeams();
        printFullMobsState();

        ArrayList<Action> actions = new ArrayList<>();

        mainLoop(actions);
    }

    private static void createTeams() {
        Random random = new Random();

        System.out.println("Введите количество участников для одной команды:");
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        System.out.println("Введите количество игроков в 1й команде:");
        int firstTeamPlayers = scanner.nextInt();
        System.out.println("Введите количество игроков во 2й команде:");
        int secondTeamPlayers = scanner.nextInt();

        team1 = new Mob[num];
        team2 = new Mob[num];

        for (int i = 0; i < num; i++) {
            if (firstTeamPlayers > 0) {
                team1[i] = new Avatar("t1.Avatar" + i, makeDefaultHealth(random), makeDefaultDamage(random), getDefaultCriticalDamageChance(random), team2, random);
                firstTeamPlayers--;
            } else {
                team1[i] = new Npc("t1.NPC" + i, makeDefaultHealth(random), makeDefaultDamage(random), getDefaultCriticalDamageChance(random), team2, random);
            }
            if (secondTeamPlayers > 0) {
                team2[i] = new Avatar("t2.Avatar" + i, makeDefaultHealth(random), makeDefaultDamage(random), getDefaultCriticalDamageChance(random), team1, random);
                secondTeamPlayers--;
            } else {
                team2[i] = new Npc("t2.NPC" + i, makeDefaultHealth(random), makeDefaultDamage(random), getDefaultCriticalDamageChance(random), team1, random);
            }
        }


    }

    private static void mainLoop(ArrayList<Action> actions) {
        Collections.addAll(actions, team1);
        Collections.addAll(actions, team2);

        for (Mob mob : team1) {
            actions.add(new AttackAction(mob));
            actions.add(new PrintStateAction(mob));
        }
        for (Mob mob : team2) {
            actions.add(new AttackAction(mob));
            actions.add(new PrintStateAction(mob));
        }

        while (!isGameOver()) {
            for (Action action : actions) {
                if (teamIsDead(team1) || teamIsDead(team2)) {
                    printFullMobsState();
                    break;
                }
                action.step();
            }
        }
    }

    private static boolean isGameOver() {
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


    private static void printFullMobsState() {
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
        return 100 + random.nextInt(100);
    }

    private static int makeDefaultDamage(Random random) {
        return 10 + random.nextInt(10);
    }

    private static int getDefaultCriticalDamageChance(Random random) {
        return random.nextInt(100);
    }
}
