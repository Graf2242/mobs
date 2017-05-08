package graf.less;

import java.util.ArrayList;

public class Team {
    private ArrayList<Mob> participants = new ArrayList<>();
    private String name;

    public Team(String name, ArrayList<Mob> participants) {
        this.name = name;
        this.participants = participants;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Mob> getParticipants() {
        return participants;
    }

}
