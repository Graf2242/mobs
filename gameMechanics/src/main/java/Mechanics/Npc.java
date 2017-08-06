package Mechanics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class Npc extends Mob {
    final private Map<Mob, Integer> agreList = new HashMap<>();
    final private int agreReduce;

    Npc(String name, int health, int damage, float criticalDamageChance, int criticalDamageMultiplier, int successfulBlockDamageScaler, FightImpl fight, Team team) {
        super(name, health, damage, criticalDamageChance, criticalDamageMultiplier, successfulBlockDamageScaler, fight, team);
        agreReduce = -10;
    }

    @Override
    public void takeDamage(Mob attacker, int damage, MobBodyAreas damagedArea) {
        updateAgreList(attacker, damage);
        super.takeDamage(attacker, damage, damagedArea);
    }

    private void updateAgreList(Mob attacker, int damage) {
        int defaultAgre = agreList.getOrDefault(attacker, 0);
        agreList.put(attacker, Math.max(defaultAgre + damage, 0));
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
        Mob newTarget = null;
        int maxAgre = 0;
        Iterator<Mob> mobIterator = agreList.keySet().iterator();
        while (mobIterator.hasNext()) {
            Mob enemy = mobIterator.next();
            if (targets.contains(enemy)) {
                if (agreList.get(enemy) > maxAgre) {
                    maxAgre = agreList.get(enemy);
                    newTarget = enemy;
                }
            } else mobIterator.remove();
        }
        for (Mob mob : agreList.keySet()) {
            if (targets.contains(mob)) {
                if (agreList.get(mob) > maxAgre) {
                    maxAgre = agreList.get(mob);
                    newTarget = mob;
                }
            } else agreList.remove(mob);
        }
        if (newTarget == null) {
            setTarget(targets.get(fight.gameState.getRandom().nextInt(targets.size())));
            while (getTarget().isDead()) {
                setTarget(targets.get(fight.gameState.getRandom().nextInt(targets.size())));
            }
        } else {
            setTarget(newTarget);
            updateAgreList(newTarget, agreReduce);
        }
    }

    @Override
    public void step() {
        defineDefendArea();
    }
}
