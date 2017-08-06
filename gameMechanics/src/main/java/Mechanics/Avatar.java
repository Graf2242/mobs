package Mechanics;

import java.util.ArrayList;
import java.util.Scanner;

public class Avatar extends Mob {

    public Avatar(String name, int health, int damage, float criticalDamageChance, int criticalDamageMultiplier, int successfulBlockDamageScaler, FightImpl fight, Team team) {
        super(name, health, damage, criticalDamageChance, criticalDamageMultiplier, successfulBlockDamageScaler, fight, team);
    }

    @Override
    public void defineDefendArea() {
        System.out.println((getName() + ": Что защищать? 1 - Голова, 2 - Тело, 3 - Ноги."));
        setDefendArea(selectArea());
    }

    @Override
    public void defineAttackArea() {
        System.out.println((getName() + ": Куда бить? 1 - Голова, 2 - Тело, 3 - Ноги."));
        setAttackArea(selectArea());
    }

    private MobBodyAreas selectArea() {
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt() - 1;
        MobBodyAreas[] areas = MobBodyAreas.values();
        if (option > 0 && option < areas.length) {
            return areas[option];
        }
        return areas[0];
    }

    @Override
    public void defineTarget(ArrayList<Mob> targets) {
        do {
            System.out.println(getName() + ": Выберите цель:");
            int i = 0;
            for (Mob mob : fight.gameState.getAliveEnemies(this)) {
                System.out.println((++i + ". " + mob.getName() + ". HP: " + mob.getHealth()));
                Scanner scanner = new Scanner(System.in);
                setTarget(fight.gameState.getAliveEnemies(this).get(scanner.nextInt() - 1));
                if (getTarget().isDead()) {
                    System.out.println("Выбранная цель мертва");
                }
            }
        } while (getTarget().isDead());
    }

    @Override
    public void step() {
        defineDefendArea();
    }
}
