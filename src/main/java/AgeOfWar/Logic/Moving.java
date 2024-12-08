package AgeOfWar.Logic;

import AgeOfWar.Characters.BaseCharacterStats;
import java.util.List;

public class Moving {//
    private static final int MIN_DISTANCE_BETWEEN_CHARACTERS = 150; // Adjust based on character size and desired gap

    public void moveCharacter(BaseCharacterStats character, List<? extends BaseCharacterStats> allCharacters) {
        if (shouldNotMove(character, allCharacters)) return;

        int moveSpeed = character.getMoveSpeed();
        if (character.isEnemy()) {
            moveCharacterLeft(character, moveSpeed);
        } else {
            moveCharacterRight(character, moveSpeed);
        }
    }


    private boolean shouldNotMove(BaseCharacterStats character, List<? extends BaseCharacterStats> allCharacters) {
        return character == null || !character.isMoving() || isCharacterInFront(character, allCharacters);
    }

    private boolean isCharacterInFront(BaseCharacterStats character, List<? extends BaseCharacterStats> allCharacters) {
        for (BaseCharacterStats otherCharacter : allCharacters) {
            if (shouldStopMoving(character, otherCharacter)) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldStopMoving(BaseCharacterStats character, BaseCharacterStats otherCharacter) {
        if (otherCharacter == character || !otherCharacter.isAlive()) return false;

        boolean isSameTeam = character.isEnemy() == otherCharacter.isEnemy();
        boolean isInFront = isCharacterInFrontOf(character, otherCharacter);

        return isSameTeam && isInFront && isTooClose(character, otherCharacter);
    }

    private boolean isCharacterInFrontOf(BaseCharacterStats character, BaseCharacterStats otherCharacter) {
        return (character.isEnemy() && otherCharacter.getX() < character.getX()) ||
                (!character.isEnemy() && otherCharacter.getX() > character.getX());
    }

    private boolean isTooClose(BaseCharacterStats character, BaseCharacterStats otherCharacter) {
        return Math.abs(otherCharacter.getX() - character.getX()) < MIN_DISTANCE_BETWEEN_CHARACTERS;
    }

    private void moveCharacterLeft(BaseCharacterStats character, int moveSpeed) {
        character.setX(character.getX() - moveSpeed);
    }

    private void moveCharacterRight(BaseCharacterStats character, int moveSpeed) {
        character.setX(character.getX() + moveSpeed);
    }

    public void stopCharacter(BaseCharacterStats character) {
        character.setMoving(false);
    }
}
