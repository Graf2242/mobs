package Mechanics;

public class DefineAttackOptionsAction implements Action {
    private final Mob mob;

    DefineAttackOptionsAction(Mob mob) {
        this.mob = mob;
    }

    @Override
    public void step() {
        if (mob.isDead()) {
            return;
        }
        mob.defineTarget(mob.fight.gameState.getAliveEnemies(mob));
        mob.defineAttackArea();
    }
}
