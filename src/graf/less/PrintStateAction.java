package graf.less;

public class PrintStateAction implements Action {

    private Mob mob;

    public PrintStateAction(Mob mob) {
        this.mob = mob;
    }

    @Override
    public void step() {
        if (mob.isDead()) {
            return;
        }
        String textIfCritical = mob.isDoCriticalDamage() ? " критическим ударом на " + mob.getDamage() * 2 : " на " + mob.getDamage();
        System.out.println((mob.getName() + " атакует " + mob.getTarget().getName() + textIfCritical + ". У " + mob.getTarget().getName() + " остается " + mob.getTarget().getHealth() + " HP."));

    }
}
