package graf.less;

public class AttackAction implements Action {
    private Mob mob;

    @Override
    public void step() {
        if (mob.isDead()) {
            return;
        }
        int damage = mob.isDoCriticalDamage() ? mob.getDamage() * 2 : mob.getDamage();
        mob.target.takeDamage(damage, mob.getAttackArea());
    }

    AttackAction(Mob mob) {
        this.mob = mob;
    }
}
