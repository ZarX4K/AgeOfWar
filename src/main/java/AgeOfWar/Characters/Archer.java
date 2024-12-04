package AgeOfWar.Characters;

public class Archer extends Knight implements CombatType {
    private int range;

    public Archer(int x, int y, int width, int height, String standImagePath, String walkImagePath, String attackImagePath, int health, int damage, int priceBuy, int moveSpeed, boolean isMoving, boolean isEnemy, boolean isInCombat, boolean defeated, int critical, int range) {
        super(x, y, width, height, standImagePath, walkImagePath, attackImagePath, health, damage, priceBuy, moveSpeed, isMoving, isEnemy, isInCombat, defeated, critical);
        this.range = range;
    }



    // Implementing methods from CombatType
    @Override
    public void performCloseCombatAttack(BaseCharacterStats target) {
        // Add close combat attack logic here
        System.out.println("Archer performs close combat attack on " + target.getClass().getSimpleName());
    }

    @Override
    public void performRangedAttack(BaseCharacterStats target) {
        // Add ranged attack logic here
        System.out.println("Archer performs ranged attack on " + target.getClass().getSimpleName());
    }

    @Override
    public void stopRangedAttack() {
        // Logic to stop ranged attack
        System.out.println("Archer stops ranged attack.");
    }

    @Override
    public void stopCloseCombat() {
        // Logic to stop close combat
        System.out.println("Archer stops close combat.");
    }

    @Override
    public int getCloseCombatRange() {
        return 50; // Example close combat range for Archer
    }

    @Override
    public int getRangedAttackRange() {
        return range; // Use the range property for the ranged attack range
    }
}