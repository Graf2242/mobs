package graf.less;

import java.util.Random;

public class Npc extends Mob {
    private Random random;

    Npc(String name, int health, int damage, int criticalDamageChance, Random random) {
        super(name, health, damage, criticalDamageChance, random);
        this.random = random;
    }

    @Override
    void defineAreas(Mob[] targets) {
        defineTarget(targets);
        setAttackArea(random.nextInt(3));
        setDefendArea(random.nextInt(3));
    }

    @Override
    void defineTarget(Mob[] targets) {
        setTarget(targets[random.nextInt(targets.length)]);
    }
}
