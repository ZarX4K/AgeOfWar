package AgeOfWar.Characters;

public interface CombatType {
    void performCloseCombatAttack(BaseCharacterStats target);
    void performRangedAttack(BaseCharacterStats target);

    void stopRangedAttack();  // Add this method
    void stopCloseCombat();   // Add this method
}
