package graf.less;

import java.util.ArrayList;
import java.util.Random;

public class Fight {

    public GameState gameState;

    private ArrayList<Action> actions = new ArrayList<>();


    public Fight(int teamsCount, int participantNumberEachTeam, int playersCount, Random random) {
        gameState = new GameState(random);
        createTeams(teamsCount, participantNumberEachTeam, playersCount);
        startFight();
    }

    private void step() {
        fillStepActions();

        for (Action action : actions) {
            if (isGameOver(false)) {
                return;
            }
            action.step();
        }
        System.out.println("----------------");
    }

    private void fillStepActions() {
        actions.clear();
        for (Team team : gameState.getTeams()) {
            actions.addAll(team.getParticipants());
        }

        ArrayList<Mob> stepParticipantsQueue = getRandomizedQueue(gameState.getParticipants());

        for (Mob mob : stepParticipantsQueue) {
            actions.add(new DefineAttackOptionsAction(mob));
            actions.add(new AttackAction(mob));
        }
    }

    private ArrayList<Mob> getRandomizedQueue(ArrayList<Mob> queue) {
        ArrayList<Integer> queueNumbers = new ArrayList<>();
        ArrayList<Mob> resultQueue = new ArrayList<>();

        for (int i = 0; i < queue.size(); i++) {
            queueNumbers.add(i);
        }

        while (!queueNumbers.isEmpty()) {
            int position = gameState.getRandom().nextInt(queueNumbers.size());
            resultQueue.add(queue.get(queueNumbers.get(position)));
            queueNumbers.remove(position);
        }
        return resultQueue;
    }

    private boolean teamIsLive(Team team) {
        for (Mob mob : team.getParticipants()) {
            if (!mob.isDead()) return true;
        }
        return false;
    }

    private void startFight() {
        printFullMobsState();
        while (!isGameOver(true)) {
            step();
        }
    }

    private int aliveTeamCount() {
        int result = 0;
        for (Team team : gameState.getTeams()) {
            if (teamIsLive(team)) {
                result++;
            }
        }
        return result;
    }

    private boolean isGameOver(boolean needPrint) {
        if (aliveTeamCount() < 2) {
            for (Team team : gameState.getTeams()) {
                if (teamIsLive(team)) {
                    if (needPrint) {
                        System.out.println("Победила " + team.getName());
                    }
                    return true;
                }
            }
            if (needPrint) {
                System.out.println("Все команды мертвы");
            }
            return true;
        }
        return false;
    }

    private void createTeams(int teamsCount, int participantNumberEachTeam, int players) {

        for (int i = 0; i < teamsCount; i++) {
            Team team = new Team("Team" + gameState.getTeams().size(), new ArrayList<>());
            gameState.getTeams().add(team);
            for (int j = 0; j < participantNumberEachTeam; j++) {
                if (players > 0) {
                    team.getParticipants().add(new Avatar("Avatar" + j, RandomMobStats.makeDefaultHealth(gameState.getRandom()), RandomMobStats.makeDefaultDamage(gameState.getRandom()), gameState.getRandom().nextFloat(), 2, 2, this, team));
                    players--;
                    continue;
                }
                team.getParticipants().add(new Npc("Mob" + j, RandomMobStats.makeDefaultHealth(gameState.getRandom()), RandomMobStats.makeDefaultDamage(gameState.getRandom()), gameState.getRandom().nextFloat(), 2, 2, this, team));

            }
        }
    }

    private void printFullMobsState() {
        for (Team team : gameState.getTeams()) {
            System.out.println(team.getName());
            for (Mob mob : team.getParticipants()) {
                System.out.println(mob.getName() + " HP: " + mob.getHealth() + " damage: " + mob.getDamage() + " critChance: " + mob.getCriticalDamageChance());
            }
        }
    }

}
