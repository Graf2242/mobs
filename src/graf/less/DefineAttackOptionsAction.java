package graf.less;

public class DefineAttackOptionsAction implements Action {
    private Mob mob;

    DefineAttackOptionsAction(Mob mob) {
        this.mob = mob;
    }

    @Override
    public void step() {
        if (mob.isDead()) {
            return;
        }
        mob.defineTarget(mob.enemyTeam);
        mob.defineAreas();
    }
}
