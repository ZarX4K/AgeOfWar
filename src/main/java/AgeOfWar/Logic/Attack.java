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

    public void performAttack(BaseCharacterStats attacker, BaseCharacterStats target,
                              List<? extends BaseCharacterStats> team1, List<? extends BaseCharacterStats> team2, MainLogic mainLogic) {
        long currentTime = System.currentTimeMillis();

        if (canAttack(attacker, target, currentTime)) {
            executeDamageExchange(attacker, target);
            updateLastAttackTime(attacker, target, currentTime);
            applyHealthBoostIfNeeded(attacker, target);
            handleDefeat(attacker, team1, mainLogic);
            handleDefeat(target, team2, mainLogic);
        }
    }

    private boolean canAttack(BaseCharacterStats attacker, BaseCharacterStats target, long currentTime) {
        return attacker.isAlive() && target.isAlive() && (currentTime - attacker.getLastAttackTime() >= ATTACK_INTERVAL);
    }

    private void executeDamageExchange(BaseCharacterStats attacker, BaseCharacterStats target) {
        int attackerDamage = getDamageWithCritical(attacker);
        int targetDamage = getDamageWithCritical(target);

        // Check if the target is a Tank and apply armor reduction with a 10% chance
        if (target instanceof Tank) {
            Tank tank = (Tank) target;

            // Using the DamageReductionMode enum to determine the reduction mode
            DamageReductionMode reductionMode = getDamageReductionMode(tank, attacker);

            if (reductionMode == DamageReductionMode.ARMOR_ACTIVATED) {
                int reducedDamage = Math.max(0, targetDamage - tank.getArmor()); // Armor blocks damage
                System.out.println("Tank's armor activated! Reduced damage by " + tank.getArmor() + ". Damage taken: " + reducedDamage);
                target.takeDamage(reducedDamage);
            } else if (reductionMode == DamageReductionMode.CRITICAL_ACTIVATED) {
                System.out.println("Critical hit activated! Damage doubled.");
                target.takeDamage(targetDamage * 2); // Critical hit, double damage
            } else {
                target.takeDamage(targetDamage);
            }
        } else {
            target.takeDamage(targetDamage);
        }

        attacker.takeDamage(attackerDamage);
    }

    private DamageReductionMode getDamageReductionMode(Tank tank, BaseCharacterStats attacker) {
        // Randomly decide if armor or critical hit should be activated
        int random = RANDOM.nextInt(10);
        if (random == 0) {
            return DamageReductionMode.ARMOR_ACTIVATED;  // 10% chance for armor activation
        } else if (attacker instanceof Knight && ((Knight) attacker).isCriticalHit()) {
            return DamageReductionMode.CRITICAL_ACTIVATED; // Critical hit activated
        }
        return DamageReductionMode.NORMAL; // Normal damage if no other mode is triggered
    }



    private void updateLastAttackTime(BaseCharacterStats attacker, BaseCharacterStats target, long currentTime) {
        attacker.setLastAttackTime(currentTime);
        target.setLastAttackTime(currentTime);
    }

    private void applyHealthBoostIfNeeded(BaseCharacterStats attacker, BaseCharacterStats target) {
        if (!healthBoostApplied && attacker.getHealth() <= 100 && target.getHealth() <= 100) {
            applyHealthBoost(attacker, target);
        }
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

    private int getDamageWithCritical(BaseCharacterStats character) {
        int damage = character.getDamage();
        if (character instanceof Knight) {
            Knight knight = (Knight) character;
            if (knight.isCriticalHit()) {
                damage += knight.getCritical();
                System.out.println(knight.getClass().getSimpleName() + " landed a critical hit! Damage: " + damage);
            }
        }
        return damage;
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

        if (canAttack(archer, target, currentTime)) {
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
            character.setMoving(false);  // Stop character movement when attacking the castle

            System.out.println(character.getClass().getSimpleName() + " is attacking the castle!!");
        }
    }

}
