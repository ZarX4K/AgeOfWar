package AgeOfWar.Characters;

import javax.swing.*;
import java.awt.*;

public class Castle {
    private int x, y, width, height, health;
    private boolean defeated;
    private static final Color HEALTH_BAR_BACKGROUND_COLOR = Color.RED;
    private static final Color HEALTH_BAR_FOREGROUND_COLOR = Color.GREEN;
    private static final int HEALTH_BAR_HEIGHT = 145;
    private static final int HEALTH_BAR_WIDTH = 10;
    protected Image standImage;


    public Castle(int x, int y, int width, int height, int health, String standImagePath) {
        this.standImage = new ImageIcon(getClass().getResource("/" + standImagePath)).getImage();

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.health = health;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            // Trigger castle destroyed logic (e.g., game over)
        }
    }

    public int getHealth() {
        return health;
    }

    public void draw(Graphics g) {
        g.drawImage(standImage ,x, y, width, height, null);
        drawHealthBar(g);
    }
    private void drawHealthBar(Graphics g) {
        double healthPercentage = (double) health / getMaxHealth();
        int barCurrentHeight = (int) (healthPercentage * HEALTH_BAR_HEIGHT);
        barCurrentHeight = Math.max(0, barCurrentHeight);
        int barX = x + (width / 2) - (HEALTH_BAR_WIDTH / 2);
        int barY = y - HEALTH_BAR_HEIGHT - 5;
        g.setColor(HEALTH_BAR_BACKGROUND_COLOR);
        g.fillRect(barX, barY, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        g.setColor(HEALTH_BAR_FOREGROUND_COLOR);
        g.fillRect(barX, barY + (HEALTH_BAR_HEIGHT - barCurrentHeight), HEALTH_BAR_WIDTH, barCurrentHeight);
    }

    // Getter and Setter methods for x, y, width, height
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getMaxHealth() {
        return 500;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
