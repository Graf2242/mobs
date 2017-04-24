package graf.less;

import java.util.Random;

abstract class Mob implements Action {

    private String name;
    private int health;
    private int damage;
    private int criticalDamageChance;
    private MobBodyAreas attackArea;
    private MobBodyAreas defendArea;
    private boolean isDoCriticalDamage;
    Mob target;
    private Random random;
    Mob[] enemyTeam;

    Mob(String name, int health, int damage, int criticalDamageChance, Mob[] enemyTeam, Random random) {
        this.setName(name);
        this.setHealth(health);
        this.setDamage(damage);
        this.setCriticalDamageChance(criticalDamageChance);
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

    int getCriticalDamageChance() {
        return criticalDamageChance;
    }

    private void setCriticalDamageChance(int criticalDamageChance) {
        this.criticalDamageChance = criticalDamageChance;
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

    boolean isDoCriticalDamage() {
        return isDoCriticalDamage;
    }

    Mob getTarget() {
        return target;
    }

    void setTarget(Mob target) {
        this.target = target;
    }

    void takeDamage(int damage, MobBodyAreas damagedArea) {
        this.setHealth((damagedArea == getDefendArea()) ? this.getHealth() - damage / 2 : this.getHealth() - damage);
    }

    void randomizeCrit() {
        isDoCriticalDamage = (getCriticalDamageChance() - random.nextInt(100) > 0);
    }

    boolean isDead() {
        return (!(getHealth() > 0));
    }

    abstract void defineAreas();

    abstract void defineTarget(Mob[] targets);
}
