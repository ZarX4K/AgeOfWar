package AgeOfWar.Characters;

public class Archer extends Knight implements CombatType{
    public int range;

    public Archer(int x, int y, int width, int height, String standImagePath, String walkImagePath, String attackImagePath, int health, int damage, int priceBuy, int moveSpeed, boolean isMoving, boolean isEnemy, boolean isInCombat, boolean defeated, int critical, int range) {
        super(x, y, width, height, standImagePath, walkImagePath, attackImagePath, health, damage, priceBuy, moveSpeed, isMoving, isEnemy, isInCombat, defeated, critical);
        this.range = range;
    }

    public int getRange() {
        return range;
    }
    @Override
    public void performCloseCombatAttack(BaseCharacterStats target) {
        // Archers are not designed for close combat; implement a default behavior
        System.out.println("Archer avoids close combat!");
    }

    @Override
    public void performRangedAttack(BaseCharacterStats target) {
        int damage = this.getDamage();
        target.takeDamage(damage);
        System.out.println("Archer attacks from range for " + damage + " damage!");
    }
}
