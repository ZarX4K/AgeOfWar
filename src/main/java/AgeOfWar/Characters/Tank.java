package AgeOfWar.Characters;

public class Tank extends Archer {
private int armor;

    public Tank(int x, int y, int width, int height, String standImagePath, String walkImagePath, String attackImagePath, int health, int damage, int priceBuy, int moveSpeed, boolean isMoving, boolean isEnemy, boolean isInCombat, boolean defeated, int critical, int range, int armor) {
        super(x, y, width, height, standImagePath, walkImagePath, attackImagePath, health, damage, priceBuy, moveSpeed, isMoving, isEnemy, isInCombat, defeated, critical, range);
        this.armor = armor;
    }

    public int getArmor() {
        return armor;
    }
}
