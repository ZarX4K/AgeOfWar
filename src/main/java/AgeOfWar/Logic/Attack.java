package AgeOfWar.Logic;

import AgeOfWar.Characters.Knight;
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

    public void performAttack(Knight knight, Knight enemyKnight, List<Knight> knights, List<Knight> enemyKnights, MainLogic mainLogic) {
        long currentTime = System.currentTimeMillis();

        if (knight.isAlive() && enemyKnight.isAlive() && currentTime - knight.getLastAttackTime() >= ATTACK_INTERVAL) {
            knight.takeDamage(enemyKnight.getDamage());
            enemyKnight.takeDamage(knight.getDamage());
            knight.setLastAttackTime(currentTime);
            enemyKnight.setLastAttackTime(currentTime);

            if (!healthBoostApplied && knight.getHealth() <= 100 && enemyKnight.getHealth() <= 100) {
                if (RANDOM.nextBoolean()) {
                    knight.setHealth(knight.getHealth() + 25);
                    System.out.println("Player knight narrowly avoids dearth!");
                } else {
                    enemyKnight.setHealth(enemyKnight.getHealth() + 25);
                    System.out.println("Enemy knight narrowly avoids death!!");
                }
                healthBoostApplied = true;
            }

            // Handle defeated knights
            if (knight.getHealth() <= 0) {
                knights.remove(knight);
                mainLogic.awardGoldForKill(knight);
                System.out.println("Player knight defeated!");
            } else {
                knight.setMoving(true);
            }

            if (enemyKnight.getHealth() <= 0) {
                enemyKnights.remove(enemyKnight);
                mainLogic.awardGoldForKill(enemyKnight);
                System.out.println("Enemy knight defeated!");
            } else {
                enemyKnight.setMoving(true);
            }
        }
    }
}