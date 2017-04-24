package graf.less;

public class AttackAction implements Action {
    private Mob mob;

    @Override
    public void step() {
        int damage = mob.isDoCriticalDamage() ? mob.getDamage() * 2 : mob.getDamage();
        mob.target.takeDamage(damage, mob.getAttackArea());
    }

    AttackAction(Mob mob) {
        this.mob = mob;
    }
}
