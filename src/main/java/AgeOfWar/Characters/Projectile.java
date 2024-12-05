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
    private String team; // "player" or "enemy"

    public Projectile(int width, int height, int x, int y, BaseCharacterStats shooter, String imageName,String team) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.shooter = shooter;
        this.damage = shooter.getDamage();
        this.active = true;
        this.team = team;

        // Spawn position slightly ahead of the shooter
        if (!shooter.isEnemy()) {
            // Player shoots, so spawn projectile in front of the player
            this.x = shooter.getX() + shooter.getWidth(); // Player shoots to the right
        } else {
            // Enemy shoots, so spawn projectile in front of the enemy (to the left)
            this.x = shooter.getX() - this.width; // Enemy shoots to the left
        }

        this.y = shooter.getY() + shooter.getHeight() -150 ; // Center vertically based on shooter


        // Load projectile image and assign to the 'image' field
        ImageIcon projectileImage = new ImageIcon(getClass().getClassLoader().getResource(imageName));
        this.image = projectileImage.getImage();  // Assigning the image to the 'image' field
    }

    public void update() {
        if (!active) return;

        // Move the projectile horizontally based on the shooter's direction
        if (shooter.isEnemy()) {
            x -= 3; // Move left for enemy
        } else {
            x += 3; // Move right for non-enemy
        }
    }


    public void draw(Graphics g, ImageObserver observer) {
        if (active) {
            // Scale the image by the specified width and height
            g.drawImage(image, x, y, width, height, observer);
        }
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
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
