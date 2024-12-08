package AgeOfWar.Characters;

public class Archer extends Knight {
    private int range;
    private long lastShotTime = 0; // Store the time of the last shot
    private boolean canAttack = true;  // Example flag to track if the archer can attack


    public Archer(int x, int y, int width, int height, String standImagePath, String walkImagePath, String attackImagePath, int health, int damage, int priceBuy, int moveSpeed, boolean isMoving, boolean isEnemy, boolean isInCombat, boolean defeated, int critical, int range) {
        super(x, y, width, height, standImagePath, walkImagePath, attackImagePath, health, damage, priceBuy, moveSpeed, isMoving, isEnemy, isInCombat, defeated, critical);
        this.range = range;
    }


    public int getRange() {
        return range;
    }

    public void resumeRangedAttack() {
        this.canAttack = true;
    }

    public boolean canAttack() {
        return this.canAttack;
    }

    public long getLastShotTime() {
        return lastShotTime;
    }

    public void setLastShotTime(long lastShotTime) {
        this.lastShotTime = lastShotTime;
    }
}
