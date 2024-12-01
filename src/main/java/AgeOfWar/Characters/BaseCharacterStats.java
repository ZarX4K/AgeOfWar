package AgeOfWar.Characters;

import AgeOfWar.Graphics.Drawable;

import javax.swing.*;
import java.awt.*;

public abstract class BaseCharacterStats implements Attackable, Drawable {
    public int x;
    public int y;
    protected int width;
    protected int height;
    protected int health;
    protected int damage;
    private long lastAttackTime;
    public int priceBuy;
    private int moveSpeed;
    private boolean isMoving;
    public boolean isEnemy;
    private boolean isInCombat;
    protected Image standImage;
    protected Image walkImage;
    protected Image attackImage;
    private boolean defeated;
    private static final Color HEALTH_BAR_BACKGROUND_COLOR = Color.RED;
    private static final Color HEALTH_BAR_FOREGROUND_COLOR = Color.GREEN;
    private static final int HEALTH_BAR_HEIGHT = 35;
    private static final int HEALTH_BAR_WIDTH = 4;

    public BaseCharacterStats(int x, int y, int width, int height, String standImagePath, String walkImagePath, String attackImagePath, int health, int damage, int priceBuy, int moveSpeed, boolean isMoving, boolean isEnemy, boolean isInCombat, boolean defeated) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.standImage = new ImageIcon(getClass().getResource("/" + standImagePath)).getImage();
        this.walkImage = new ImageIcon(getClass().getResource("/" + walkImagePath)).getImage();
        this.attackImage = new ImageIcon(getClass().getResource("/" + attackImagePath)).getImage();
        this.health = health;
        this.damage = damage;
        this.priceBuy = priceBuy;
        this.moveSpeed = moveSpeed;
        this.isMoving = isMoving;
        this.isEnemy = isEnemy;
        this.isInCombat = isInCombat;
        this.defeated = defeated;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(walkImage, x, y, width, height, null);
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

    @Override
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public boolean isDefeated() {
        return defeated;
    }

    public void setDefeated(boolean defeated) {
        this.defeated = defeated;
    }

    public int getGoldReward() {
        return this.priceBuy;
    }

    public boolean isAlive() {
        return this.health > 0;
    }

    public int getDamage() {
        return this.damage;
    }


    public int getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public long getLastAttackTime() {
        return lastAttackTime;
    }

    public void setLastAttackTime(long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setX(int x) {
        this.x = x;
    }


    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return 100;
    }

    public int getPriceBuy() {
        return priceBuy;
    }
}
