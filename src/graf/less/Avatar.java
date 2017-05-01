package graf.less;

import java.util.Random;
import java.util.Scanner;

public class Avatar extends Mob {


    Avatar(String name, int health, int damage, Mob[] enemyTeam, Random random) {
        super(name, health, damage, enemyTeam, random);
    }

    @Override
    void defineDefendArea() {
        System.out.println((getName() + ": Что защищать? 1 - Голова, 2 - Тело, 3 - Ноги."));
        setDefendArea(selectArea());
    }

    @Override
    void defineAttackArea() {
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
    void defineTarget(Mob[] targets) {
        do {
            System.out.println(getName() + ": Выберите цель:");
            for (int i = 0; i < enemyTeam.length; i++) {
                int j = i + 1;
                System.out.println((j + ". " + enemyTeam[i].getName() + ". HP: " + enemyTeam[i].getHealth()));
            }
            Scanner scanner = new Scanner(System.in);
            target = enemyTeam[scanner.nextInt() - 1];
            if (target.isDead()) {
                System.out.println("Выбранная цель мертва");
            }
        } while (target.isDead());
    }

    @Override
    public void step() {
        defineDefendArea();
    }
}
