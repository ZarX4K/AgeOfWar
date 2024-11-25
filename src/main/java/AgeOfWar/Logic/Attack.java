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

    // Perform an attack between characters
    public void performAttack(BaseCharacterStats attacker, BaseCharacterStats target,
                              List<? extends BaseCharacterStats> team1, List<? extends BaseCharacterStats> team2, MainLogic mainLogic) {
        long currentTime = System.currentTimeMillis();

        if (canAttack(attacker, target, currentTime)) {
            int damageDealt = calculateDamage(attacker);
            applyDamage(target, damageDealt); // Apply damage without checking if it's greater than 0
            updateLastAttackTime(attacker, currentTime);
            applyHealthBoostIfNeeded(attacker, target);
            handleDefeat(attacker, team1, mainLogic);
            handleDefeat(target, team2, mainLogic);
        }
    }


    // Check if the attacker can attack
    private boolean canAttack(BaseCharacterStats attacker, BaseCharacterStats target, long currentTime) {
        return attacker.isAlive() && target.isAlive() &&
                currentTime - attacker.getLastAttackTime() >= ATTACK_INTERVAL;
    }

    // Calculate damage with random critical hit logic
    private int calculateDamage(BaseCharacterStats attacker) {
        int baseDamage = attacker.getDamage();

        boolean criticalHit = RANDOM.nextInt(100) < 20;

        // Check if the attacker is a Knight (or subclass of Knight)
        if (criticalHit && attacker instanceof Knight) {
            Knight knight = (Knight) attacker;
            int criticalDamage = knight.getCritical();
            System.out.println(knight.getClass().getSimpleName() + " landed a critical hit! Damage: " + criticalDamage);
            return criticalDamage;
        }

        return baseDamage;
    }



    // Apply damage to a target
    private void applyDamage(BaseCharacterStats target, int damage) {
        if (target instanceof Tank) {
            Tank tank = (Tank) target;
            int finalDamage = Math.max(0, damage - tank.getArmor());
            target.takeDamage(finalDamage);
            System.out.println("Tank received " + finalDamage + " damage (reduced by armor).");
        } else {
            target.takeDamage(damage);
            System.out.println(target.getClass().getSimpleName() + " received " + damage + " damage.");
        }
    }

    // Update the last attack time for the attacker
    private void updateLastAttackTime(BaseCharacterStats attacker, long currentTime) {
        attacker.setLastAttackTime(currentTime);
    }

    // Apply health boost if needed
    private void applyHealthBoostIfNeeded(BaseCharacterStats attacker, BaseCharacterStats target) {
        if (!healthBoostApplied && attacker.getClass() == target.getClass() &&
                attacker.getHealth() <= 100 && target.getHealth() <= 100) {
            if (RANDOM.nextBoolean()) {
                attacker.setHealth(attacker.getHealth() + 25);
                System.out.println(attacker.getClass().getSimpleName() + " narrowly avoided defeat with a health boost!");
            } else {
                target.setHealth(target.getHealth() + 25);
                System.out.println(target.getClass().getSimpleName() + " narrowly avoided defeat with a health boost!");
            }
            healthBoostApplied = true;
        }
    }

    // Handle character defeat
    private void handleDefeat(BaseCharacterStats character, List<? extends BaseCharacterStats> team, MainLogic mainLogic) {
        if (character.getHealth() <= 0) {
            mainLogic.awardGoldForKill(character);
            team.remove(character);
        } else {
            character.setMoving(true);
        }
    }

    // Perform an attack on a castle
    public void performAttackOnCastle(BaseCharacterStats character, Castle castle) {
        long currentTime = System.currentTimeMillis();

        if (character.isAlive() && currentTime - character.getLastAttackTime() >= ATTACK_INTERVAL) {
            int damage = character.getDamage();
            castle.takeDamage(damage);
            character.setLastAttackTime(currentTime);
            character.setMoving(false);
        }
    }
}
