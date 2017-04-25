package graf.less;

public class PrintStateAction implements Action {

    private Mob mob;

    PrintStateAction(Mob mob) {
        this.mob = mob;
    }

    @Override
    public void step() {
        if (mob.isDead()) {
            return;
        }
        String textIfCritical = mob.isLastAttackCrit() ? " критическим ударом на " + mob.getDamage() * 2 : " на " + mob.getDamage();
        System.out.println((mob.getName() + " атакует " + mob.getTarget().getName() + textIfCritical+ " в "+ mob.getAttackArea()+ " (противник защищает "+ mob.target.getDefendArea() + "). У " + mob.getTarget().getName() + " остается " + mob.getTarget().getHealth() + " HP."));

    }
}
