package AgeOfWar.Logic;

import AgeOfWar.Characters.*;
import java.util.List;
import java.util.Random;

public class Attack {//
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
            if (attacker instanceof CombatType) {
                CombatType combatant = (CombatType) attacker;
                int distance = Math.abs(attacker.getX() - target.getX());

                if (distance <= 50) { // Example threshold for close combat
                    combatant.performCloseCombatAttack(target);
                } else {
                    combatant.performRangedAttack(target);
                }
            } else {
                int damageDealt = calculateDamage(attacker);
                DAMAGESTATES damageState = determineDamageState(attacker, target, damageDealt);
                applyDamage(target, damageDealt, damageState);
            }
            if (attacker instanceof Archer) {
                Archer archer = (Archer) attacker;
                int distance = Math.abs(attacker.getX() - target.getX());
                if (distance <= archer.getRange() * 3) { // 3x normal range
                    System.out.println(attacker.getClass().getSimpleName() + " shoots an arrow!");
                }
            }

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

        if (criticalHit && attacker instanceof Knight) {
            Knight knight = (Knight) attacker;
            int criticalDamage = knight.getCritical();
            System.out.println(knight.getClass().getSimpleName() + " landed a critical hit! Damage: " + criticalDamage);
            return criticalDamage;
        }

        return baseDamage;
    }

    // Determine the damage state
    private DAMAGESTATES determineDamageState(BaseCharacterStats attacker, BaseCharacterStats target, int damage) {
        if (target instanceof Tank && RANDOM.nextInt(100) < 10) {
            return DAMAGESTATES.ARMOR_ACTIVATED;
        } else if (attacker instanceof Knight && damage > attacker.getDamage()) {
            return DAMAGESTATES.CRITICAL_ACTIVATED;
        }
        return DAMAGESTATES.NORMAL;
    }

    // Apply damage to a target based on damage state
    private void applyDamage(BaseCharacterStats target, int damage, DAMAGESTATES damageState) {
        int finalDamage = damage;

        switch (damageState) {
            case ARMOR_ACTIVATED:
                if (target instanceof Tank) {
                    Tank tank = (Tank) target;
                    finalDamage = Math.max(0, damage - tank.getArmor());
                }
                System.out.println("Armor activated! Reduced damage to " + finalDamage + ".");
                break;

            case CRITICAL_ACTIVATED:
                System.out.println("Critical hit! Damage dealt: " + finalDamage);
                break;

            case NORMAL:
                System.out.println("Normal damage: " + finalDamage);
                break;
        }

        target.takeDamage(finalDamage);
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
            } else {
                target.setHealth(target.getHealth() + 25);
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