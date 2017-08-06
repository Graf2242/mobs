package Mechanics;

import java.util.ArrayList;

abstract class Mob implements Action {

    protected final Fight fight;
    private final int criticalDamageMultiplier;
    private final int successfulBlockDamageScaler;
    private final float criticalDamageChance;
    private final String name;
    private final int damage;
    private final Team team;
    private int health;
    private MobBodyAreas attackArea;
    private MobBodyAreas defendArea;
    private boolean isLastAttackCrit;
    private Mob target;

    public Mob(String name, int health, int damage, float criticalDamageChance, int criticalDamageMultiplier, int successfulBlockDamageScaler, Fight fight, Team team) {
        this.name = name;
        this.health = health;
        this.damage = damage;
        this.criticalDamageChance = criticalDamageChance;
        this.fight = fight;
        this.team = team;
        this.criticalDamageMultiplier = criticalDamageMultiplier;
        this.successfulBlockDamageScaler = successfulBlockDamageScaler;
    }

    public int getCriticalDamageMultiplier() {
        return criticalDamageMultiplier;
    }

    public int getSuccessfulBlockDamageScaler() {
        return successfulBlockDamageScaler;
    }

    public Team getTeam() {
        return team;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    private void setHealth(int health) {
        this.health = health;
    }

    public boolean isLastAttackCrit() {
        return isLastAttackCrit;
    }

    public int getDamage() {
        return damage;
    }

    public float getCriticalDamageChance() {
        return criticalDamageChance;
    }

    public MobBodyAreas getAttackArea() {
        return attackArea;
    }

    public void setAttackArea(MobBodyAreas attackArea) {
        this.attackArea = attackArea;
    }

    public MobBodyAreas getDefendArea() {
        return defendArea;
    }

    public void setDefendArea(MobBodyAreas defendArea) {
        this.defendArea = defendArea;
    }

    public Mob getTarget() {
        return target;
    }

    public void setTarget(Mob target) {
        this.target = target;
    }

    public void takeDamage(Mob attacker, int damage, MobBodyAreas damagedArea) {
        this.setHealth((damagedArea == getDefendArea()) ? this.getHealth() - damage / successfulBlockDamageScaler : this.getHealth() - damage);
    }

    public boolean isDoCriticalDamage() {
        isLastAttackCrit = getCriticalDamageChance() - fight.gameState.getRandom().nextFloat() > 0;
        return (isLastAttackCrit);
    }

    public boolean isDead() {
        return (health <= 0);
    }

    abstract public void defineDefendArea();

    abstract public void defineAttackArea();

    abstract public void defineTarget(ArrayList<Mob> targets);
}
