// File: AgeOfWar.Logic.Moving.java
package AgeOfWar.Logic;

import AgeOfWar.Characters.Knight;
import java.util.List;

public class Moving {
    private static final int MIN_DISTANCE_BETWEEN_KNIGHTS = 150; // Adjust based on knight size and desired gap

    public void moveKnight(Knight knight, List<Knight> allKnights) {
        if (knight == null || !knight.isMoving()) return;

        // Check if there's another knight in front within the minimum distance
        if (isKnightInFront(knight, allKnights)) {
            return; // Stop moving if there's an ally or enemy knight directly in front
        }

        int moveSpeed = knight.getMoveSpeed();
        if (knight.isEnemy()) {
            knight.setX(knight.getX() - moveSpeed);
        } else {
            knight.setX(knight.getX() + moveSpeed);
        }
    }

    private boolean isKnightInFront(Knight knight, List<Knight> allKnights) {
        for (Knight otherKnight : allKnights) {
            if (otherKnight != knight && otherKnight.isAlive()) {
                boolean isSameTeam = knight.isEnemy() == otherKnight.isEnemy();

                // Check if another knight (enemy or ally) is in front
                if (isSameTeam && ((knight.isEnemy() && otherKnight.getX() < knight.getX()) ||
                        (!knight.isEnemy() && otherKnight.getX() > knight.getX()))) {

                    if (Math.abs(otherKnight.getX() - knight.getX()) < MIN_DISTANCE_BETWEEN_KNIGHTS) {
                        return true; // Another knight is in front within the minimum distance
                    }
                }
            }
        }
        return false;
    }

    public void stopKnight(Knight knight) {
        knight.setMoving(false);
    }
}
