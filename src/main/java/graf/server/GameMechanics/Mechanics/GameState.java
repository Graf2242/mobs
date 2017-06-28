package graf.server.GameMechanics.Mechanics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameState {
    final private ArrayList<Team> teams = new ArrayList<>();
    final private Random random;

    public GameState(Random random) {
        this.random = random;

    }

    public Random getRandom() {
        return random;
    }

    public ArrayList<Mob> getParticipants() {
        ArrayList<Mob> participants = new ArrayList<>();
        participants.clear();
        for (Team team : teams) {
            participants.addAll(team.getParticipants());
        }
        return participants;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public ArrayList<Mob> getAliveEnemies(Mob mob) {
        ArrayList<Mob> enemies = new ArrayList<>();
        ArrayList<Team> enemyTeams = new ArrayList<>();
        enemyTeams.addAll(teams);
        for (Team team : teams) {
            if (team.getParticipants().contains(mob)) {
                enemyTeams.remove(team);
                for (Team enemyTeam : enemyTeams) {
                    List<Mob> participants = enemyTeam.getParticipants();
                    participants.removeIf(enemy -> enemy.getHealth() < 1);
                    enemies.addAll(participants);
                }
            }
        }
        return enemies;
    }
}

