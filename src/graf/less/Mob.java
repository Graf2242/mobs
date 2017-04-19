package graf.less;

import java.util.Random;

abstract class Mob {

    private String name;
    private int health;
    private int damage;
    private int criticalDamageChance;
    private int attackArea;
    private int defendArea;
    private boolean isDoCriticalDamage;
    private Mob target;
    private Random random;

    String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getCriticalDamageChance() {
        return criticalDamageChance;
    }

    public void setCriticalDamageChance(int criticalDamageChance) {
        this.criticalDamageChance = criticalDamageChance;
    }

    public int getAttackArea() {
        return attackArea;
    }

    public void setAttackArea(int attackArea) {
        this.attackArea = attackArea;
    }

    public int getDefendArea() {
        return defendArea;
    }

    public void setDefendArea(int defendArea) {
        this.defendArea = defendArea;
    }

    public boolean isDoCriticalDamage() {
        return isDoCriticalDamage;
    }

    public void setDoCriticalDamage(boolean doCriticalDamage) {
        isDoCriticalDamage = doCriticalDamage;
    }

    public Mob getTarget() {
        return target;
    }

    public void setTarget(Mob target) {
        this.target = target;
    }

    Mob(String name, int health, int damage, int criticalDamageChance, Random random) {
        this.setName(name);
        this.setHealth(health);
        this.setDamage(damage);
        this.setCriticalDamageChance(criticalDamageChance);
        this.random = random;
    }

    void takeDamage(int damage, int damagedArea) {
        this.setHealth((damagedArea == getDefendArea()) ? this.getHealth() - damage / 2 : this.getHealth() - damage);
    }

    void randomizeCrit() {
        isDoCriticalDamage = (getCriticalDamageChance() - random.nextInt(100) > 0);
    }

    boolean isDead() {
        return (!(getHealth() > 0));
    }

    abstract void defineAreas(Mob[] targets);
    abstract void defineTarget(Mob[] targets);
}
