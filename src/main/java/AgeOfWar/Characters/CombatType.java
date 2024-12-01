package AgeOfWar.Characters;

public interface CombatType {//
    void performCloseCombatAttack(BaseCharacterStats target);
    void performRangedAttack(BaseCharacterStats target);
}
