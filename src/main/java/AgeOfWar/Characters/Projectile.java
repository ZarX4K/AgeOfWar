package AgeOfWar.Characters;

import java.awt.*;
import java.awt.image.ImageObserver;

public class Projectile {
    private double x, y; // Position
    private double velocityX, velocityY; // Direction and speed
    private double damage; // Damage dealt to enemy
    private BaseCharacterStats shooter; // The entity that shot the projectile
    private Image image; // The image representation of the projectile
    private Rectangle bounds; // The bounding rectangle of the projectile
    private boolean active; // Whether the projectile is still active in the game

    public Projectile(double startX, double startY, double velocityX, double velocityY, double damage, BaseCharacterStats shooter, Image image) {
        this.x = startX;
        this.y = startY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.damage = damage;
        this.shooter = shooter;
        this.image = image;
        this.active = true;
        this.bounds = new Rectangle((int) startX, (int) startY, image.getWidth(null), image.getHeight(null));
    }

    // Update projectile position and bounds
    public void update() {
        if (!active) return;

        x += velocityX;
        y += velocityY;
        bounds.setLocation((int) x, (int) y);
    }

    // Handle collision with an enemy
    public void collide(BaseCharacterStats enemy) {
        if (!active || enemy == shooter) return;

        if (bounds.intersects(new Rectangle(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight()))) {
            enemy.takeDamage(5);
            disappear();
        }
    }

    // Remove the projectile from the game
    private void disappear() {
        active = false;
    }

    // Draw the projectile
    public void draw(Graphics g, ImageObserver observer) {
        if (active) {
            g.drawImage(image, (int) x, (int) y, observer);
        }
    }

    // Check if the projectile is still active
    public boolean isActive() {
        return active;
    }

    // Get bounds for collision detection
    public Rectangle getBounds() {
        return bounds;
    }
}
