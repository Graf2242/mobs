package graf.less;

import java.util.Random;
import java.util.Scanner;

public class Avatar extends Mob {


    Avatar(String name, int health, int damage, int criticalDamageChance, Mob[] enemyTeam, Random random) {
        super(name, health, damage, criticalDamageChance, enemyTeam, random);
    }

    //TODO
    @Override
    void defineAreas(Mob[] targets) {
        System.out.println(("Куда бить? 1 - Голова, 2 - Тело, 3 - Ноги."));
        setAttackArea(selectArea());
        System.out.println(("Что защищать? 1 - Голова, 2 - Тело, 3 - Ноги."));
        setDefendArea(selectArea());
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
    void defineTarget(Mob[] targets) {
        setTarget(targets[0]);
    }

    //TODO
    @Override
    public void step() {

    }
}
