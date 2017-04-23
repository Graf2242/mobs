package graf.less;

import java.util.Random;
import java.util.Scanner;

class Npc extends Mob {
    private Random random;

    Npc(String name, int health, int damage, int criticalDamageChance, Mob[] enemyTeam, Random random) {
        super(name, health, damage, criticalDamageChance, enemyTeam, random);
        this.random = random;
    }

    @Override
    void defineAreas(Mob[] targets) {
        defineTarget(targets);
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

    //TODO
    @Override
    public void step() {
        if (isDead()) {
            return;
        }
        randomizeCrit();
        defineAreas(enemyTeam);
        while (target.isDead()) {
            defineTarget(enemyTeam);
        }
        int damage = isDoCriticalDamage() ? getDamage() * 2 : getDamage();
        target.takeDamage(damage, getAttackArea());

    }
}
