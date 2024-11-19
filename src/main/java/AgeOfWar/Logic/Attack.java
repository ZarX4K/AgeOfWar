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
                mainLogic.awardGoldForKill(attacker);
                team1.remove(attacker);
                System.out.println(attacker.getClass().getSimpleName() + " defeated!");
            } else {
                attacker.setMoving(true);  // If alive, resume movement
            }

            if (target.getHealth() <= 0) {
                mainLogic.awardGoldForKill(target);
                team2.remove(target);
                System.out.println(target.getClass().getSimpleName() + " defeated!");
            } else {
                target.setMoving(true);  // If alive, resume movement
            }
        }
    }


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
