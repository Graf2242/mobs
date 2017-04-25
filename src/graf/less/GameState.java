package graf.less;

class GameState {

    private Mob[] team1;
    private Mob[] team2;

    Mob[] getTeam1() {
        return team1;
    }

    void setTeam1(Mob[] team1) {
        this.team1 = team1;
    }

    Mob[] getTeam2() {
        return team2;
    }

    void setTeam2(Mob[] team2) {
        this.team2 = team2;
    }
}
