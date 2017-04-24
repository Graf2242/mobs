package graf.less;

import java.util.Random;

class Npc extends Mob {
    private Random random;

    Npc(String name, int health, int damage, int criticalDamageChance, Mob[] enemyTeam, Random random) {
        super(name, health, damage, criticalDamageChance, enemyTeam, random);
        this.random = random;
    }

    @Override
    void defineAreas() {
        setAttackArea(selectArea());
        setDefendArea(selectArea());
    }

    private MobBodyAreas selectArea() {
        int option = random.nextInt(3);
        MobBodyAreas[] areas = MobBodyAreas.values();
        return areas[option];
    }

    @Override
    void defineTarget(Mob[] targets) {
        setTarget(targets[random.nextInt(targets.length)]);
    }

    @Override
    public void step() {
        if (isDead()) {
            return;
        }
        randomizeCrit();
        defineTarget(enemyTeam);
        while (target.isDead()) {
            defineTarget(enemyTeam);
        }
        defineAreas();
        int damage = isDoCriticalDamage() ? getDamage() * 2 : getDamage();
    }
}
