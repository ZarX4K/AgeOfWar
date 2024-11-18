package AgeOfWar.Logic;

import AgeOfWar.Characters.BaseCharacterStats;
import java.util.List;

public class Moving {
    private static final int MIN_DISTANCE_BETWEEN_CHARACTERS = 150; // Adjust based on character size and desired gap

    public void moveCharacter(BaseCharacterStats character, List<? extends BaseCharacterStats> allCharacters) {
        if (character == null || !character.isMoving()) return;

        // Check if there's another character in front within the minimum distance
        if (isCharacterInFront(character, allCharacters)) {
            return; // Stop moving if there's an ally or enemy character directly in front
        }

        int moveSpeed = character.getMoveSpeed();
        if (character.isEnemy()) {
            character.setX(character.getX() - moveSpeed);
        } else {
            character.setX(character.getX() + moveSpeed);
        }
    }

    private boolean isCharacterInFront(BaseCharacterStats character, List<? extends BaseCharacterStats> allCharacters) {
        for (BaseCharacterStats otherCharacter : allCharacters) {
            if (otherCharacter != character && otherCharacter.isAlive()) {
                boolean isSameTeam = character.isEnemy() == otherCharacter.isEnemy();

                // Check if another character (enemy or ally) is in front
                if (isSameTeam && ((character.isEnemy() && otherCharacter.getX() < character.getX()) ||
                        (!character.isEnemy() && otherCharacter.getX() > character.getX()))) {
                    if (Math.abs(otherCharacter.getX() - character.getX()) < MIN_DISTANCE_BETWEEN_CHARACTERS) {
                        return true; // Another character is in front within the minimum distance
                    }
                }
            }
        }
        return false;
    }

    public void stopCharacter(BaseCharacterStats character) {
        character.setMoving(false);
    }
}
