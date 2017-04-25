package graf.less;

import java.util.Random;

class Npc extends Mob {
    private Random random;

    Npc(String name, int health, int damage, Mob[] enemyTeam, Random random) {
        super(name, health, damage, enemyTeam, random);
        this.random = random;
    }

    @Override
    void defineAreas() {
        setAttackArea(selectArea());
        setDefendArea(selectArea());
    }

    private MobBodyAreas selectArea() {
        int option = random.nextInt(MobBodyAreas.values().length);
        MobBodyAreas[] areas = MobBodyAreas.values();
        return areas[option];
    }

    @Override
    void defineTarget(Mob[] targets) {
        setTarget(targets[random.nextInt(targets.length)]);
        while (target.isDead()) {
            setTarget(targets[random.nextInt(targets.length)]);
        }
    }

    @Override
    public void step() {
        defineTarget(enemyTeam);
    }
}
