package graf.less;

public class PrintStateAction implements Action {

    private final Mob mob;

    public PrintStateAction(Mob mob) {
        this.mob = mob;
    }

    @Override
    public void step() {
        if (mob.isDead()) {
            return;
        }
        int damage = mob.getDamage();
        if (mob.getAttackArea() == mob.getTarget().getDefendArea()) {
            damage /= mob.getTarget().getSuccessfulBlockDamageScaler();
        }
        if (mob.isLastAttackCrit()) {
            damage *= mob.getCriticalDamageMultiplier();
        }
        String textIfCritical = mob.isLastAttackCrit() ? " критическим ударом на " + damage : " на " + damage;
        System.out.println((mob.getTeam().getName() + " " + mob.getName() + " атакует " + mob.getTarget().getTeam().getName() + " " + mob.getTarget().getName() + textIfCritical + " в " + mob.getAttackArea() + " (противник защищает " + mob.getTarget().getDefendArea() + "). У " + mob.getTarget().getTeam().getName() + " " + mob.getTarget().getName() + " остается " + mob.getTarget().getHealth() + " HP."));

    }
}
