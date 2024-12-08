package AgeOfWar.Characters;

import AgeOfWar.Logic.MainLogic;

import java.util.List;

public interface CombatType {

    public void performAttack(BaseCharacterStats attacker, BaseCharacterStats target,
                              List<? extends BaseCharacterStats> team1, List<? extends BaseCharacterStats> team2, MainLogic mainLogic);

    public void performAttackOnCastle(BaseCharacterStats character, Castle castle);

}
