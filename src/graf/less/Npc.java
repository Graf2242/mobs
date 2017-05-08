package graf.less;

import java.util.ArrayList;

class Npc extends Mob {

    Npc(String name, int health, int damage, float criticalDamageChance, int criticalDamageMultiplier, int successfulBlockDamageScaler, Fight fight, Team team) {
        super(name, health, damage, criticalDamageChance, criticalDamageMultiplier, successfulBlockDamageScaler, fight, team);
    }

    @Override
    public void defineDefendArea() {
        setDefendArea(selectArea());
    }

    @Override
    public void defineAttackArea() {
        setAttackArea(selectArea());
    }

    private MobBodyAreas selectArea() {
        int option = fight.gameState.getRandom().nextInt(MobBodyAreas.values().length);
        MobBodyAreas[] areas = MobBodyAreas.values();
        return areas[option];
    }

    @Override
    public void defineTarget(ArrayList<Mob> targets) {
        setTarget(targets.get(fight.gameState.getRandom().nextInt(targets.size())));
        while (getTarget().isDead()) {
            setTarget(targets.get(fight.gameState.getRandom().nextInt(targets.size())));
        }
    }

    @Override
    public void step() {
        defineDefendArea();
    }
}
