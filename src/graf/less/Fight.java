package graf.less;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

class Fight {

    private Mob[] team1;
    private Mob[] team2;
    private ArrayList<Mob> participants = new ArrayList<>();
    private ArrayList<Action> actions = new ArrayList<>();

    private Random random;


    Fight(Random random) {
        this.random = random;
        createTeams();
        startFight();
    }

    private void step() {
        fillStepActions();

        for (Action action : actions) {
            if (isGameOver()) {
                return;
            }
            action.step();
        }
        System.out.println("----------------");
    }

    private void fillStepActions() {
        actions.clear();
        Collections.addAll(actions, team1);
        Collections.addAll(actions, team2);

        ArrayList<Mob> stepParticipantsQueue = getRandomizedQueue(participants);

        for (Mob mob : stepParticipantsQueue) {
            actions.add(new DefineAttackOptionsAction(mob));
            actions.add(new AttackAction(mob));
            actions.add(new PrintStateAction(mob));
        }
    }

    //Берет список участников и возвращает в случайном порядке
    private ArrayList<Mob> getRandomizedQueue(ArrayList<Mob> queue) {
        ArrayList<Integer> queueNumbers = new ArrayList<>();
        ArrayList<Mob> resultQueue = new ArrayList<>();

        for (int i = 0; i < queue.size(); i++) {
            queueNumbers.add(i);
        }

        while (!queueNumbers.isEmpty()) {
            int position = random.nextInt(queueNumbers.size());
            resultQueue.add(queue.get(queueNumbers.get(position)));
            queueNumbers.remove(position);
        }
        return resultQueue;
    }

    private boolean teamIsDead(Mob[] team) {
        for (Mob mob : team) {
            if (!mob.isDead()) return false;
        }
        return true;
    }

    private void startFight() {
        Collections.addAll(participants, team1);
        Collections.addAll(participants, team2);

        printFullMobsState();
        while (!isGameOver()) {
            step();
        }
    }

    private boolean isGameOver() {

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

    private void createTeams() {
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
        team1 = new Mob[participantNumberEachTeam];
        team2 = new Mob[participantNumberEachTeam];

        for (int i = 0; i < firstTeamPlayers; i++) {
            team1[i] = new Avatar("t1.Avatar" + i, RandomizedDefaults.makeDefaultHealth(random), RandomizedDefaults.makeDefaultDamage(random), team2, random);
        }
        for (int i = firstTeamPlayers; i < participantNumberEachTeam; i++) {
            team1[i] = new Npc("t1.NPC" + i, RandomizedDefaults.makeDefaultHealth(random), RandomizedDefaults.makeDefaultDamage(random), team2, random);
        }
        for (int i = 0; i < secondTeamPlayers; i++) {
            team2[i] = new Avatar("t2.Avatar" + i, RandomizedDefaults.makeDefaultHealth(random), RandomizedDefaults.makeDefaultDamage(random), team1, random);
        }
        for (int i = secondTeamPlayers; i < participantNumberEachTeam; i++) {
            team2[i] = new Npc("t2.NPC" + i, RandomizedDefaults.makeDefaultHealth(random), RandomizedDefaults.makeDefaultDamage(random), team1, random);
        }
    }

    private void printFullMobsState() {

        System.out.println("Команда 1:");
        for (Mob mob : team1) {
            System.out.println(mob.getName() + " HP: " + mob.getHealth() + " damage: " + mob.getDamage() + " critChance: " + mob.getCriticalDamageChance());
        }
        System.out.println("Команда 2:");
        for (Mob mob : team2) {
            System.out.println(mob.getName() + " HP: " + mob.getHealth() + " damage: " + mob.getDamage() + " critChance: " + mob.getCriticalDamageChance());
        }
    }

}
