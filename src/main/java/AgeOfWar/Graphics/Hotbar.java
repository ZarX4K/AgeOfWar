package AgeOfWar.Graphics;

import AgeOfWar.Logic.MainLogic;

import java.awt.*;

public class Hotbar {//
    private int x, y, width, height;
    private String side; // "player" or "enemy"

    public Hotbar(int x, int y, int width, int height, String side) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.side = side;
    }

    public void draw(Graphics g, MainLogic mainLogic) {
        // Get cooldown progress
        double progress = mainLogic.getCooldownProgress(side);

        // Draw background (gray)
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, width, height);

        // Draw progress bar (green if ready, red otherwise)
        g.setColor(progress >= 1.0 ? Color.GREEN : Color.RED);

        if ("player".equals(side)) {
            // Player hotbar (left to right)
            g.fillRect(x, y, (int) (width * progress), height);
        } else if ("enemy".equals(side)) {
            // Enemy hotbar (right to left)
            g.fillRect(x + width - (int) (width * progress), y, (int) (width * progress), height);
        }

        // Optional: Draw border
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }
}
