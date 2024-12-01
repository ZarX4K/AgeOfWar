package AgeOfWar.Logic;

import AgeOfWar.Characters.BaseCharacterStats;
import AgeOfWar.Characters.Castle;
import AgeOfWar.Characters.Projectile;

import java.awt.Rectangle;

public class Hitboxes {//
    public boolean collidesCharacters(BaseCharacterStats character1, BaseCharacterStats character2) {
        Rectangle hitbox1 = new Rectangle(character1.getX(), character1.getY(), character1.getWidth(), character1.getHeight());
        Rectangle hitbox2 = new Rectangle(character2.getX(), character2.getY(), character2.getWidth(), character2.getHeight());
        return hitbox1.intersects(hitbox2);
    }

    public boolean collidesCastle(BaseCharacterStats character, Castle castle) {
        Rectangle characterHitbox = new Rectangle(character.getX(), character.getY(), character.getWidth(), character.getHeight());
        Rectangle castleHitbox = new Rectangle(castle.getX(), castle.getY(), castle.getWidth(), castle.getHeight());
        return characterHitbox.intersects(castleHitbox);
    }

    public boolean collidesArrowCharacter(Projectile projectile, BaseCharacterStats character) {
        Rectangle projectileHitbox = new Rectangle((int) projectile.getX(), (int) projectile.getY(), projectile.getWidth(), projectile.getHeight());
        Rectangle characterHitbox = new Rectangle((int) character.getX(), (int) character.getY(), (int) character.getWidth(), (int) character.getHeight());
        return projectileHitbox.intersects(characterHitbox);
    }

    public boolean collidesArrowCastle(Projectile projectile, Castle castle) {
        Rectangle projectileHitbox = new Rectangle((int) projectile.getX(), (int) projectile.getY(), projectile.getWidth(), projectile.getHeight());
        Rectangle castleHitbox = new Rectangle((int) castle.getX(), (int) castle.getY(), (int) castle.getWidth(), (int) castle.getHeight());
        return projectileHitbox.intersects(castleHitbox);
    }



}