package AgeOfWar.Logic;

import AgeOfWar.Characters.*;
import java.util.List;
import java.util.Random;

public class Attack {
    private static final long ATTACK_INTERVAL = 500; // Milliseconds between attacks
    private static final Random RANDOM = new Random();
    private boolean healthBoostApplied = false;

    public Attack() {
        // Initialization if needed
    }

    public void performAttack(BaseCharacterStats attacker, BaseCharacterStats target, List<? extends BaseCharacterStats> team1, List<? extends BaseCharacterStats> team2, MainLogic mainLogic) {
        long currentTime = System.currentTimeMillis();

        if (attacker.isAlive() && target.isAlive() && currentTime - attacker.getLastAttackTime() >= ATTACK_INTERVAL) {
            int damageDealt = getDamageWithCritical(attacker);
            target.takeDamage(damageDealt);

            int targetDamage = getDamageWithCritical(target);
            attacker.takeDamage(targetDamage);

            attacker.setLastAttackTime(currentTime);
            target.setLastAttackTime(currentTime);

            if (!healthBoostApplied && attacker.getHealth() <= 100 && target.getHealth() <= 100) {
                applyHealthBoost(attacker, target);
            }

            handleDefeat(attacker, team1, mainLogic);
            handleDefeat(target, team2, mainLogic);
        }
    }

    private int getDamageWithCritical(BaseCharacterStats character) {
        int damage = character.getDamage();
        if (character instanceof Knight && ((Knight) character).isCriticalHit()) {
            damage += ((Knight) character).getCritical();
            System.out.println(character.getClass().getSimpleName() + " landed a critical hit!" +getDamageWithCritical(character));
        }
        return damage;
    }

    private void applyHealthBoost(BaseCharacterStats attacker, BaseCharacterStats target) {
        if (RANDOM.nextBoolean()) {
            attacker.setHealth(attacker.getHealth() + 25);
            System.out.println(attacker.getClass().getSimpleName() + " narrowly avoids defeat!");
        } else {
            target.setHealth(target.getHealth() + 25);
            System.out.println(target.getClass().getSimpleName() + " narrowly avoids defeat!");
        }
        healthBoostApplied = true;
    }

    private void handleDefeat(BaseCharacterStats character, List<? extends BaseCharacterStats> team, MainLogic mainLogic) {
        if (character.getHealth() <= 0) {
            mainLogic.awardGoldForKill(character);
            team.remove(character);
            System.out.println(character.getClass().getSimpleName() + " defeated!");
        } else {
            character.setMoving(true);
        }
    }

    public void performRangedAttack(Archer archer, BaseCharacterStats target) {
        long currentTime = System.currentTimeMillis();

        if (archer.isAlive() && target.isAlive() && currentTime - archer.getLastAttackTime() >= ATTACK_INTERVAL) {
            int damageDealt = getDamageWithCritical(archer);
            target.takeDamage(damageDealt);
            archer.setLastAttackTime(currentTime);

            System.out.println(archer.getClass().getSimpleName() + " attacks " + target.getClass().getSimpleName() + " from range!");

            if (target.getHealth() <= 0) {
                System.out.println(target.getClass().getSimpleName() + " defeated by ranged attack!");
                target.setDefeated(true);
            }
        }
    }

    public void performAttackOnCastle(BaseCharacterStats character, Castle castle) {
        long currentTime = System.currentTimeMillis();
        if (character.isAlive() && currentTime - character.getLastAttackTime() >= ATTACK_INTERVAL) {
            castle.takeDamage(character.getDamage());
            character.setLastAttackTime(currentTime);

            // Stop character movement when attacking the castle
            character.setMoving(false);

            System.out.println(character.getClass().getSimpleName() + " is attacking the castle!");
        }
    }
}