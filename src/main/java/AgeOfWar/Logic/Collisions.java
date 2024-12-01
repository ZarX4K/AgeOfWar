package AgeOfWar.Logic;

import AgeOfWar.Characters.BaseCharacterStats;
import AgeOfWar.Characters.Castle;
import java.util.ArrayList;
import java.util.List;

public class Collisions {
    private final Attack attack;
    private final Hitboxes hitboxes;
    private final Moving moving;
    private final MainLogic mainLogic;

    public Collisions(Moving moving, Hitboxes hitboxes, Attack attack, MainLogic mainLogic) {
        this.moving = moving;
        this.hitboxes = hitboxes;
        this.attack = attack;
        this.mainLogic = mainLogic;
    }

    public void checkCollisions(List<? extends BaseCharacterStats> allies, List<? extends BaseCharacterStats> enemies) {
        List<BaseCharacterStats> defeatedAllies = new ArrayList<>();
        List<BaseCharacterStats> defeatedEnemies = new ArrayList<>();

        // Check for collisions and handle attacks directly on the castle
        checkCastleCollisions(allies, defeatedAllies, mainLogic.getEnemyCastle(), enemies);
        checkCastleCollisions(enemies, defeatedEnemies, mainLogic.getPlayerCastle(), allies);

        // Check for character vs. character collisions and attacks
        checkCharacterCollisions(allies, enemies, defeatedAllies, defeatedEnemies);
        checkCharacterCollisions(enemies, allies, defeatedEnemies, defeatedAllies);

        // Remove defeated characters from the lists after the iteration to avoid ConcurrentModificationException
        allies.removeAll(defeatedAllies);
        enemies.removeAll(defeatedEnemies);
    }


    private void checkCastleCollisions(List<? extends BaseCharacterStats> characters, List<BaseCharacterStats> defeatedCharacters, Castle castle, List<? extends BaseCharacterStats> opponentCharacters) {
        for (BaseCharacterStats character : new ArrayList<>(characters)) {
            if (hitboxes.collides(character, castle)) {
                // Directly call the attack method for attacking the castle
                attack.performAttackOnCastle(character, castle);
                if (!character.isAlive()) {
                    defeatedCharacters.add(character);
                }
            }
        }
    }

    private void checkCharacterCollisions(List<? extends BaseCharacterStats> characters, List<? extends BaseCharacterStats> opponentCharacters, List<BaseCharacterStats> defeatedCharacters, List<BaseCharacterStats> opponentDefeatedCharacters) {
        for (BaseCharacterStats character : new ArrayList<>(characters)) {
            BaseCharacterStats nearestOpponent = findNearestOpponent(character, opponentCharacters);
            if (nearestOpponent != null && hitboxes.collides(character, nearestOpponent)) {
                moving.stopCharacter(character);
                moving.stopCharacter(nearestOpponent);
                attack.performAttack(character, nearestOpponent, characters, opponentCharacters, mainLogic);

                if (!character.isAlive()) {
                    defeatedCharacters.add(character);
                }
                if (!nearestOpponent.isAlive()) {
                    opponentDefeatedCharacters.add(nearestOpponent);
                }
            }
        }
    }

    private BaseCharacterStats findNearestOpponent(BaseCharacterStats character, List<? extends BaseCharacterStats> opponents) {
        BaseCharacterStats nearestOpponent = null;
        int nearestDistance = Integer.MAX_VALUE;

        for (BaseCharacterStats opponent : opponents) {
            int distance = Math.abs(character.getX() - opponent.getX());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestOpponent = opponent;
            }
        }
        return nearestOpponent;
    }


}
