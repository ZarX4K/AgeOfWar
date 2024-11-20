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

        // Check for collisions between allies and enemies
        for (BaseCharacterStats ally : new ArrayList<>(allies)) {
            // Check collision with enemy castle
            if (hitboxes.collides(ally, mainLogic.getEnemyCastle())) {
                attack.performAttackOnCastle(ally, mainLogic.getEnemyCastle());
                if (!ally.isAlive()) {
                    defeatedAllies.add(ally);
                }
            } else {
                BaseCharacterStats nearestEnemy = findNearestEnemy(ally, enemies);
                if (nearestEnemy != null && hitboxes.collides(ally, nearestEnemy)) {
                    moving.stopCharacter(ally);
                    moving.stopCharacter(nearestEnemy);
                    attack.performAttack(ally, nearestEnemy, allies, enemies, mainLogic);
                    if (!ally.isAlive()) {
                        defeatedAllies.add(ally);
                    }
                    if (!nearestEnemy.isAlive()) {
                        defeatedEnemies.add(nearestEnemy);
                    }
                }
            }
        }

        // Check for collisions between enemies and player castle and attacking characters
        for (BaseCharacterStats enemy : new ArrayList<>(enemies)) {
            if (hitboxes.collides(enemy, mainLogic.getPlayerCastle())) {
                attack.performAttackOnCastle(enemy, mainLogic.getPlayerCastle());
                if (!enemy.isAlive()) {
                    defeatedEnemies.add(enemy);
                }
            } else {
                BaseCharacterStats nearestAlly = findNearestAlly(enemy, allies);
                if (nearestAlly != null && hitboxes.collides(enemy, nearestAlly)) {
                    moving.stopCharacter(enemy);
                    moving.stopCharacter(nearestAlly);
                    attack.performAttack(enemy, nearestAlly, enemies, allies, mainLogic);
                    if (!enemy.isAlive()) {
                        defeatedEnemies.add(enemy);
                    }
                    if (!nearestAlly.isAlive()) {
                        defeatedAllies.add(nearestAlly);
                    }
                }
            }
        }

        allies.removeAll(defeatedAllies);
        enemies.removeAll(defeatedEnemies);
    }

    private BaseCharacterStats findNearestAlly(BaseCharacterStats character, List<? extends BaseCharacterStats> allies) {
        return findNearestEnemy(character, allies);  // Reuse the existing method to find nearest ally
    }

    private BaseCharacterStats findNearestEnemy(BaseCharacterStats character, List<? extends BaseCharacterStats> enemies) {
        BaseCharacterStats nearestEnemy = null;
        int nearestDistance = Integer.MAX_VALUE;

        for (BaseCharacterStats enemy : enemies) {
            int distance = Math.abs(character.getX() - enemy.getX());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestEnemy = enemy;
            }
        }
        return nearestEnemy;
    }
}