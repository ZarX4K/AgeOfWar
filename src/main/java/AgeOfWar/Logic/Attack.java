package AgeOfWar.Logic;

import AgeOfWar.Characters.Archer;
import AgeOfWar.Characters.BaseCharacterStats;
import java.util.List;
import java.util.Random;

public class Attack {
    private static final long ATTACK_INTERVAL = 500; // Milliseconds between attacks
    private static final Random RANDOM = new Random();

    private boolean healthBoostApplied = false;

    public Attack() {
        // Initialization if needed
    }

    /**
     * Perform a melee attack between two characters (one attacker and one target).
     *
     * @param attacker  the attacking character
     * @param target    the target character
     * @param team1     the team to which the attacker belongs
     * @param team2     the team to which the target belongs
     * @param mainLogic the main logic of the game (used for gold awarding)
     */
    public void performAttack(BaseCharacterStats attacker, BaseCharacterStats target, List<? extends BaseCharacterStats> team1, List<? extends BaseCharacterStats> team2, MainLogic mainLogic) {
        long currentTime = System.currentTimeMillis();

        if (attacker.isAlive() && target.isAlive() && currentTime - attacker.getLastAttackTime() >= ATTACK_INTERVAL) {
            // Exchange damage between attacker and target
            attacker.takeDamage(target.getDamage());
            target.takeDamage(attacker.getDamage());
            attacker.setLastAttackTime(currentTime);
            target.setLastAttackTime(currentTime);

            // Random health boost when both characters are close to death
            if (!healthBoostApplied && attacker.getHealth() <= 100 && target.getHealth() <= 100) {
                if (RANDOM.nextBoolean()) {
                    attacker.setHealth(attacker.getHealth() + 25);
                    System.out.println(attacker.getClass().getSimpleName() + " narrowly avoids defeat!");
                } else {
                    target.setHealth(target.getHealth() + 25);
                    System.out.println(target.getClass().getSimpleName() + " narrowly avoids defeat!");
                }
                healthBoostApplied = true;
            }

            // Handle defeated characters and remove them from their respective teams
            if (attacker.getHealth() <= 0) {
                team1.remove(attacker);
                mainLogic.awardGoldForKill(attacker);
                System.out.println(attacker.getClass().getSimpleName() + " defeated!");
            } else {
                attacker.setMoving(true);  // If alive, resume movement
            }

            if (target.getHealth() <= 0) {
                team2.remove(target);
                mainLogic.awardGoldForKill(target);
                System.out.println(target.getClass().getSimpleName() + " defeated!");
            } else {
                target.setMoving(true);  // If alive, resume movement
            }
        }
    }

    /**
     * Perform a ranged attack by an archer on a target.
     *
     * @param archer the attacking archer character
     * @param target the target character
     */
    public void performRangedAttack(Archer archer, BaseCharacterStats target) {
        long currentTime = System.currentTimeMillis();

        // Perform attack if enough time has passed since the last ranged attack
        if (archer.isAlive() && target.isAlive() && currentTime - archer.getLastAttackTime() >= ATTACK_INTERVAL) {
            // Ranged damage exchange (archer attacks, target takes damage)
            target.takeDamage(archer.getDamage());
            archer.setLastAttackTime(currentTime);

            System.out.println(archer.getClass().getSimpleName() + " attacks " + target.getClass().getSimpleName() + " from range!");

            // Handle defeated target
            if (target.getHealth() <= 0) {
                System.out.println(target.getClass().getSimpleName() + " defeated by ranged attack!");
                target.setDefeated(true); // Mark as defeated
            }
        }
    }
}
