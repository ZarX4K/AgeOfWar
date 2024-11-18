package AgeOfWar.Logic;

import AgeOfWar.Characters.BaseCharacterStats;

import java.util.List;
import java.util.Random;

public class Attack {
    private static final long ATTACK_INTERVAL = 500;
    private static final Random RANDOM = new Random();
    private Moving moving;
    private boolean healthBoostApplied = false;

    public Attack(Moving moving) {
        this.moving = moving;
    }

    public void performAttack(BaseCharacterStats attacker, BaseCharacterStats target, List<? extends BaseCharacterStats> team1, List<? extends BaseCharacterStats> team2, MainLogic mainLogic) {
        long currentTime = System.currentTimeMillis();

        if (attacker.isAlive() && target.isAlive() && currentTime - attacker.getLastAttackTime() >= ATTACK_INTERVAL) {
            // Exchange damage
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

            // Handle defeated characters
            if (attacker.getHealth() <= 0) {
                team1.remove(attacker);
                mainLogic.awardGoldForKill(attacker);
                System.out.println(attacker.getClass().getSimpleName() + " defeated!");
            } else {
                attacker.setMoving(true);
            }

            if (target.getHealth() <= 0) {
                team2.remove(target);
                mainLogic.awardGoldForKill(target);
                System.out.println(target.getClass().getSimpleName() + " defeated!");
            } else {
                target.setMoving(true);
            }
        }
    }

}
