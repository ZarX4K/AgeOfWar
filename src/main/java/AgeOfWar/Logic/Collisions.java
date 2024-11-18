package AgeOfWar.Logic;

import AgeOfWar.Characters.BaseCharacterStats;
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

        for (BaseCharacterStats ally : new ArrayList<>(allies)) {
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

        allies.removeAll(defeatedAllies);
        enemies.removeAll(defeatedEnemies);

        // Award gold for defeated enemies
        for (BaseCharacterStats defeatedEnemy : defeatedEnemies) {
            mainLogic.awardGoldForKill(defeatedEnemy);
            System.out.println(defeatedEnemy.getClass().getSimpleName() + " has been removed from the game.");
        }
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
