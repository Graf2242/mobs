package graf.less;

import java.util.List;

public class Team {
    private List<Mob> participants;
    private String name;

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
