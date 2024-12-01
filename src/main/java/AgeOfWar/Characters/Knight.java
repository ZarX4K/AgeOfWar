package AgeOfWar.Characters;

import java.util.Random;

public class Knight extends BaseCharacterStats {
    public int critical;
    private static final double CRITICAL_CHANCE = 0.1; // 10% chance


    public Knight(int x, int y, int width, int height, String standImagePath, String walkImagePath, String attackImagePath, int health, int damage, int priceBuy, int moveSpeed, boolean isMoving, boolean isEnemy, boolean isInCombat, boolean defeated, int critical) {
            super(x, y, width, height, standImagePath, walkImagePath, attackImagePath, health, damage, priceBuy, moveSpeed, isMoving, isEnemy, isInCombat, defeated);
            this.critical = critical;
    }
    public int getCritical() {
        return critical;
    }

    public boolean isCriticalHit() {
        return new Random().nextDouble() <= CRITICAL_CHANCE;
    }
}
