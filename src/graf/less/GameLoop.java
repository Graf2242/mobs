package graf.less;

import java.util.Random;
import java.util.Scanner;

public class GameLoop {

    private static Mob[] team1;
    private static Mob[] team2;

    public static void main(String[] args) {

        createTeams();
        printFullMobsState();

        do {
            nextRound();
        } while (!isGameOver());

    }

    private static void createTeams() {
        Random random = new Random();
        System.out.println("Введите количество участников для одной команды:");
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        team1 = new Mob[num];
        team2 = new Mob[num];
        for (int i = 0; i < num; i++) {
            team1[i] = new Npc("Avatar" + i, makeDefaultHealth(random), makeDefaultDamage(random), getDefaultCriticalDamageChance(random), random);
            team2[i] = new Npc("NPC" + i, makeDefaultHealth(random), makeDefaultDamage(random), getDefaultCriticalDamageChance(random), random);
        }
    }

    private static void nextRound() {
        System.out.println("------------------");
        for (Mob mob : team1) {
            mob.randomizeCrit();
            mob.defineAreas(team2);
        }
        for (Mob mob : team2) {
            mob.randomizeCrit();
            mob.defineAreas(team1);
        }
        //TODO Подобное для team2 и вывод printFightLog перенести в цикл
        for (Mob damager : team1) {
            Mob target = damager.getTarget();
            while (target.isDead()) {
                damager.defineTarget(team2);
                target = damager.getTarget();
            }
            int damage = damager.isDoCriticalDamage() ? damager.getDamage() * 2 : damager.getDamage();
            target.takeDamage(damage, damager.getAttackArea());
        }
        for (Mob damager : team2) {
            Mob target = damager.getTarget();
            while (target.isDead()) {
                damager.defineTarget(team1);
                target = damager.getTarget();
            }
            int damage = damager.isDoCriticalDamage() ? damager.getDamage() * 2 : damager.getDamage();
            target.takeDamage(damage, damager.getAttackArea());
        }

        printFightLog(team1);
        printFightLog(team2);
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

    //TODO
    private static void printFightLog(Mob[] participants) {
        for (Mob mob : participants) {
            String textIfCritical = mob.isDoCriticalDamage() ? " критическим ударом на " + mob.getDamage() * 2 : " на " + mob.getDamage();
            System.out.println((mob.getName() + " атакует " + mob.getTarget().getName() + textIfCritical + ". У " + mob.getTarget().getName() + " остается " + mob.getTarget().getHealth() + " HP."));
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
