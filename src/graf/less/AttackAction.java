package graf.less;

public class AttackAction implements Action {
    private Mob mob;

    @Override
    public void step() {
        mob.target.takeDamage(mob.getDamage(), mob.getAttackArea());
    }

    AttackAction(Mob mob) {
        this.mob = mob;
    }
}
