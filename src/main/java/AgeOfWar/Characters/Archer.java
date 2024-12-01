package AgeOfWar.Characters;

public class Archer extends Knight {
    private int range;

    public Archer(int x, int y, int width, int height, String standImagePath, String walkImagePath, String attackImagePath, int health, int damage, int priceBuy, int moveSpeed, boolean isMoving, boolean isEnemy, boolean isInCombat, boolean defeated, int critical, int range) {
        super(x, y, width, height, standImagePath, walkImagePath, attackImagePath, health, damage, priceBuy, moveSpeed, isMoving, isEnemy, isInCombat, defeated, critical);
        this.range = range;
    }

    public int getRange() {
        return range;
    }
}
