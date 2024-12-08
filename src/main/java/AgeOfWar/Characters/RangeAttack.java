package AgeOfWar.Characters;

import AgeOfWar.Logic.MainLogic;

import java.util.List;

public class RangeAttack implements CombatType {
    @Override
    public void performAttack(BaseCharacterStats attacker, BaseCharacterStats target, List<? extends BaseCharacterStats> team1, List<? extends BaseCharacterStats> team2, MainLogic mainLogic) {
        Archer archer = (Archer) attacker;
        int distance = Math.abs(attacker.getX() - target.getX());
     //   System.out.println("Distance between " + attacker.getClass().getSimpleName() + " and opponent: " + distance);

        if (distance <= 150) {
            // Engage in close combat if too close for ranged attacks
            System.out.println("Archer is too close and switches to close combat.");
        } else if (distance <= archer.getRange()) {
            // Perform ranged attack within valid range
            long currentTime = System.currentTimeMillis();
            if (currentTime - archer.getLastShotTime() >= 800) {
                mainLogic.spawnProjectile(attacker); // Spawn projectile
                archer.setLastShotTime(currentTime);
            }
        }
    }

    @Override
    public void performAttackOnCastle(BaseCharacterStats character, Castle castle) {

    }
}
