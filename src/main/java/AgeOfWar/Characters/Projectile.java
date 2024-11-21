package AgeOfWar.Characters;

import AgeOfWar.Characters.BaseCharacterStats;
import AgeOfWar.Characters.Archer;
import AgeOfWar.Characters.Tank;

import javax.swing.*;
import java.awt.*;

public class Projectile {
    private int x, y;
    private int speedX, speedY;
    private BaseCharacterStats shooter;
    private String imagePath;

    public Projectile(int x, int y, int targetX, int targetY, BaseCharacterStats shooter, String imagePath) {
        this.x = x;
        this.y = y;
        this.shooter = shooter;
        this.imagePath = imagePath;

        // Calculate the trajectory towards the target
        double angle = Math.atan2(targetY - y, targetX - x);
        double speed = 6; // You can adjust the speed of the projectile

        this.speedX = (int) (speed * Math.cos(angle));
        this.speedY = (int) (speed * Math.sin(angle));
    }

    // Method to update projectile position
    public void move() {
        x += speedX;
        y += speedY;
    }

    // Check if the projectile collides with an enemy
    public void checkCollision(BaseCharacterStats target) {
        if (isColliding(target)) {
            // The projectile hits the target, apply damage based on the shooter's stats
            target.takeDamage(shooter.getDamage());

            // If target's health is 0 or less, mark it as defeated
            if (!target.isAlive()) {
                target.setDefeated(true);
            }
        }
    }

    // Check if the projectile collides with the given character
    private boolean isColliding(BaseCharacterStats target) {
        return this.x >= target.getX() && this.x <= target.getX() + target.getWidth()
                && this.y >= target.getY() && this.y <= target.getY() + target.getHeight();
    }

    // Getter for the projectile's x and y coordinates (used for rendering)
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getImagePath() {
        return imagePath;
    }

    // Render the projectile on the screen
    public void draw(Graphics g) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/" + imagePath));
        g.drawImage(icon.getImage(), x, y, null);
    }
}
