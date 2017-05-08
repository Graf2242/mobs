package graf.less;

public class AttackAction implements Action {
    private final Mob mob;

    public AttackAction(Mob mob) {
        this.mob = mob;
    }

    @Override
    public void step() {
        if (mob.isDead()) {
            return;
        }
        int damage = mob.isDoCriticalDamage() ? mob.getDamage() * mob.getCriticalDamageMultiplier() : mob.getDamage();
        mob.getTarget().takeDamage(damage, mob.getAttackArea());
        new PrintStateAction(mob).step();
    }
}
