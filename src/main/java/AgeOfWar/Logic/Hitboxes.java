package AgeOfWar.Logic;

import AgeOfWar.Characters.Knight;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Hitboxes {
    public List<Rectangle> getAllKnightHitboxes(List<Knight> knights) {
        List<Rectangle> hitboxes = new ArrayList<>();
        for (Knight knight : knights) {
            hitboxes.add(new Rectangle(knight.getX(), knight.getY(), knight.getWidth(), knight.getHeight()));
        }
        return hitboxes;
    }

    public boolean knightsCollide(Knight knight, Knight enemyKnight) {
        Rectangle hitbox1 = new Rectangle(knight.getX(), knight.getY(), knight.getWidth(), knight.getHeight());
        Rectangle hitbox2 = new Rectangle(enemyKnight.getX(), enemyKnight.getY(), enemyKnight.getWidth(), enemyKnight.getHeight());
        return hitbox1.intersects(hitbox2);
    }
}
