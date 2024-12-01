package AgeOfWar.Characters;

import java.awt.*;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;

public class Projectile {
    private double x, y; // Position of the projectile
    private double velocityX, velocityY; // Movement direction and speed
    private double damage; // Damage dealt upon collision
    private BaseCharacterStats shooter; // The entity that fired the projectile
    private Image image; // Visual representation of the projectile
    private boolean active; // Whether the projectile is active in the game

    public Projectile(BaseCharacterStats shooter, double velocityMultiplier, String imageName) {
        this.shooter = shooter;
        this.damage = shooter.getDamage();
        this.active = true;

        // Spawn position slightly ahead of the shooter
        this.x = shooter.getX() + (!shooter.isEnemy ? shooter.getWidth() : -20);
        this.y = shooter.getY() + shooter.getHeight() / 2;

        // Velocity based on the shooter's facing direction
        this.velocityX = (shooter.isEnemy ? 1 : -1) * velocityMultiplier;
        this.velocityY = 0;

        // Load projectile image and assign to the 'image' field
        ImageIcon projectileImage = new ImageIcon(getClass().getClassLoader().getResource(imageName));
        this.image = projectileImage.getImage();  // Assigning the image to the 'image' field
    }


    public void update() {
        if (!active) return;

        // Update projectile position
        x += velocityX;
        y += velocityY;

        // Deactivate if out of bounds
        if (x < 0 || x > 1600 || y < 0 || y > 900) {
            active = false;
        }
    }

    public void draw(Graphics g, ImageObserver observer) {
        if (active) {
            g.drawImage(image, (int) x, (int) y, observer);
        }
    }

    public boolean checkCollision(Rectangle targetBounds) {
        if (!active) return false;
        Rectangle projectileBounds = new Rectangle((int) x, (int) y, image.getWidth(null), image.getHeight(null));
        return projectileBounds.intersects(targetBounds);
    }

    public double getDamage() {
        return damage;
    }

    public BaseCharacterStats getShooter() {
        return shooter;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }
}
