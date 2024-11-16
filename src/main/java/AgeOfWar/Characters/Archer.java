package AgeOfWar.Characters;

public class Archer extends Knight{
    public int range;

    public Archer(int x, int y, int width, int height, String standImagePath, String walkImagePath, String attackImagePath, int health, int damage, int priceBuy, int moveSpeed, boolean isMoving, boolean isEnemy, int critical, int range) {
        super(x, y, width, height, standImagePath, walkImagePath, attackImagePath, health, damage, priceBuy, moveSpeed, isMoving, isEnemy, critical);
        this.range = range;
    }
}
