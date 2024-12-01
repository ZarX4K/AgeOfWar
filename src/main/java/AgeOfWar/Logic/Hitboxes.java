package AgeOfWar.Logic;

import AgeOfWar.Characters.BaseCharacterStats;
import AgeOfWar.Characters.Castle;

import java.awt.Rectangle;

public class Hitboxes {//
    public boolean collides(BaseCharacterStats character1, BaseCharacterStats character2) {
        Rectangle hitbox1 = new Rectangle(character1.getX(), character1.getY(), character1.getWidth(), character1.getHeight());
        Rectangle hitbox2 = new Rectangle(character2.getX(), character2.getY(), character2.getWidth(), character2.getHeight());
        return hitbox1.intersects(hitbox2);
    }

    public boolean collides(BaseCharacterStats character, Castle castle) {
        Rectangle characterHitbox = new Rectangle(character.getX(), character.getY(), character.getWidth(), character.getHeight());
        Rectangle castleHitbox = new Rectangle(castle.getX(), castle.getY(), castle.getWidth(), castle.getHeight());
        return characterHitbox.intersects(castleHitbox);
    }
}