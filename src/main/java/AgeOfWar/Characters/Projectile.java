package AgeOfWar.Characters;

import java.awt.*;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;

public class Projectile {
    private int x, y; // Position of the projectile
    private int damage; // Damage dealt upon collision
    private BaseCharacterStats shooter; // The entity that fired the projectile
    private Image image; // Visual representation of the projectile
    private boolean active; // Whether the projectile is active in the game
    private int width, height; // Width and height for scaling

    public Projectile(int width, int height, int x, int y, BaseCharacterStats shooter, String imageName) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.shooter = shooter;
        this.damage = shooter.getDamage();
        this.active = true;

        // Spawn position slightly ahead of the shooter
        this.x = shooter.getX() + (!shooter.isEnemy() ? shooter.getWidth() : 120);
        this.y = shooter.getY() + shooter.getHeight() - 150;

        // Load projectile image and assign to the 'image' field
        ImageIcon projectileImage = new ImageIcon(getClass().getClassLoader().getResource(imageName));
        this.image = projectileImage.getImage();  // Assigning the image to the 'image' field
    }

    public void update() {
        if (!active) return;

        // Move the projectile horizontally based on the shooter's direction
        x += shooter.isEnemy() ? -3 : 3;

        // Deactivate if out of bounds
        if (x < 0 || x > 1600 || y < 0 || y > 900) {
            active = false;
        }
    }

    public void draw(Graphics g, ImageObserver observer) {
        if (active) {
            // Scale the image by the specified width and height
            g.drawImage(image, x, y, width, height, observer);
        }
    }

    public boolean checkCollision(Rectangle targetBounds) {
        if (!active) return false;
        Rectangle projectileBounds = new Rectangle(x, y, width, height);
        return projectileBounds.intersects(targetBounds);
    }

    public int getDamage() {
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

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
