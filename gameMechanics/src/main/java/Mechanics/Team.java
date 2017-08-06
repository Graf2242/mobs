package Mechanics;

import java.util.List;

public class Team {
    private final List<Mob> participants;
    private final String name;

    public Team(String name, List<Mob> participants) {
        this.name = name;
        this.participants = participants;

    }

    public String getName() {
        return name;
    }

    public List<Mob> getParticipants() {
        return participants;
    }

}
