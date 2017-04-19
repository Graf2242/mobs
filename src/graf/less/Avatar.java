package graf.less;

import java.util.Random;
import java.util.Scanner;

public class Avatar extends Mob {
    Avatar(String name, int health, int damage, int criticalDamageChance, Random random) {
        super(name, health, damage, criticalDamageChance, random);
    }

    //TODO
    @Override
    void defineAreas(Mob[] targets) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(("Куда бить? 1 - Голова, 2 - Тело, 3 - Ноги."));
        setAttackArea(scanner.nextInt()-1);
        System.out.println(("Что защищать? 1 - Голова, 2 - Тело, 3 - Ноги."));
        setDefendArea(scanner.nextInt()-1);
    }

    @Override
    void defineTarget(Mob[] targets) {
        setTarget(targets[0]);
    }
}
