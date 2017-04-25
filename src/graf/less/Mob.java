package graf.less;

import java.util.Random;

abstract class Mob implements Action {

    private String name;
    private int health;
    private int damage;
    private final float criticalDamageChance;
    private MobBodyAreas attackArea;
    private MobBodyAreas defendArea;
    Mob target;
    private Random random;
    Mob[] enemyTeam;

    Mob(String name, int health, int damage,  Mob[] enemyTeam, Random random) {
        this.setName(name);
        this.setHealth(health);
        this.setDamage(damage);
        criticalDamageChance = random.nextFloat();
        this.random = random;
        this.enemyTeam = enemyTeam;
    }

    String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    int getHealth() {
        return health;
    }

    private void setHealth(int health) {
        this.health = health;
    }

    int getDamage() {
        return damage;
    }

    private void setDamage(int damage) {
        this.damage = damage;
    }

    float getCriticalDamageChance() {
        return criticalDamageChance;
    }

    MobBodyAreas getAttackArea() {
        return attackArea;
    }

    void setAttackArea(MobBodyAreas attackArea) {
        this.attackArea = attackArea;
    }

    MobBodyAreas getDefendArea() {
        return defendArea;
    }

    void setDefendArea(MobBodyAreas defendArea) {
        this.defendArea = defendArea;
    }

    Mob getTarget() {
        return target;
    }

    void setTarget(Mob target) {
        this.target = target;
    }

    void takeDamage(int damage, MobBodyAreas damagedArea) {
        final int successfulBlockDamageScaler = 2;
        this.setHealth((damagedArea == getDefendArea()) ? this.getHealth() - damage / successfulBlockDamageScaler : this.getHealth() - damage);
    }

    boolean isDoCriticalDamage() {
        return (getCriticalDamageChance() - random.nextFloat() > 0);
    }

    boolean isDead() {
        return (health <=0);
    }

    abstract void defineAreas();

    abstract void defineTarget(Mob[] targets);
}
