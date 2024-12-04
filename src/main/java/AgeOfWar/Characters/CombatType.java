package AgeOfWar.Characters;

public interface CombatType {
    void performCloseCombatAttack(BaseCharacterStats target);
    void performRangedAttack(BaseCharacterStats target);

    void stopRangedAttack();
    void stopCloseCombat();

    // Define methods to get the combat ranges
    int getCloseCombatRange();
    int getRangedAttackRange();
}
