package AgeOfWar.Logic;

import AgeOfWar.Characters.BaseCharacterStats;

import java.awt.Rectangle;
import java.util.List;

public class Moving {
    private static final int MIN_DISTANCE_BETWEEN_KNIGHTS = 150; // To avoid friendly blocking
    private Hitboxes hitboxes;  // We'll use this for collision detection

    public Moving(Hitboxes hitboxes) {
        this.hitboxes = hitboxes;
    }

    // Method to check if a character is engaged in combat by checking collisions
    public boolean isColliding(BaseCharacterStats character, List<BaseCharacterStats> allCharacters) {
        for (BaseCharacterStats otherCharacter : allCharacters) {
            if (otherCharacter != character && otherCharacter.isAlive() && otherCharacter.isEnemy() != character.isEnemy()) {
                // Check for collision between the character and enemy
                if (hitboxes.collides(character, otherCharacter)) {
                    return true;  // They are colliding, meaning combat starts
                }
            }
        }
        return false;  // No collision, no combat
    }

    // Move character only if not colliding with an enemy
    public void moveCharacter(BaseCharacterStats character, List<BaseCharacterStats> allCharacters) {
        if (character == null || !character.isMoving()) return;

        // Check if the character is colliding with an enemy
        if (isColliding(character, allCharacters)) {
            return; // Stop movement if a collision occurs (engagement in combat)
        }

        // Check if the character's path is blocked by a friendly character
        if (isCharacterInFront(character, allCharacters)) {
            return; // Character cannot move if blocked by a friendly character
        }

        int moveSpeed = character.getMoveSpeed();
        // Move left or right based on whether the character is an enemy or not
        character.setX(character.isEnemy() ? character.getX() - moveSpeed : character.getX() + moveSpeed);
    }

    private boolean isCharacterInFront(BaseCharacterStats character, List<BaseCharacterStats> allCharacters) {
        for (BaseCharacterStats otherCharacter : allCharacters) {
            if (otherCharacter != character && otherCharacter.isAlive()) {
                boolean isSameTeam = character.isEnemy() == otherCharacter.isEnemy();
                if (isSameTeam &&
                        Math.abs(otherCharacter.getX() - character.getX()) < MIN_DISTANCE_BETWEEN_KNIGHTS) { // Prevent friendly blocking
                    if (character.isEnemy()) {
                        // Enemy characters are moving left, check if they're blocked on the left
                        if (otherCharacter.getX() < character.getX()) {
                            return true;
                        }
                    } else {
                        // Non-enemy characters are moving right, check if they're blocked on the right
                        if (otherCharacter.getX() > character.getX()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
