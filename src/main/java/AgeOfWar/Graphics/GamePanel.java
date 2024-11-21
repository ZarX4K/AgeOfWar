package AgeOfWar.Graphics;

import AgeOfWar.Logic.MainLogic;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private MainLogic mainLogic;
    private Hotbar playerHotbar;
    private Hotbar enemyHotbar;

    public GamePanel(MainLogic mainLogic) {
        this.mainLogic = mainLogic;
        setBackground(Color.BLACK);
        setFocusable(true);
        playerHotbar = new Hotbar(500, 20, 150, 10, "player");
        enemyHotbar = new Hotbar(1000, 20, 150, 10, "enemy");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (mainLogic.getGameState() == 1) {
            drawIntroScreen(g);
        } else if (mainLogic.getGameState() == 2) {
            drawGameScreen(g);
        }
    }

    private void drawIntroScreen(Graphics g) {
        // Draw intro screen background
        g.drawImage(mainLogic.getBackGroundScreens().getIntroImageIcon().getImage(), 0, 0, null);
    }

    private void drawGameScreen(Graphics g) {
        // Draw game background
        g.drawImage(mainLogic.getBackGroundScreens().getBackgroundImage(), 0, 0, null);

        // Draw hotbars
        playerHotbar.draw(g, mainLogic);
        enemyHotbar.draw(g, mainLogic);

        // Draw castles
        mainLogic.getPlayerCastle().draw(g);
        mainLogic.getEnemyCastle().draw(g);

        // Draw characters (Knights, Archers, Tanks)
        drawCharacters(g);

        // Display gold of each player
        displayGold(g);
    }

    private void drawCharacters(Graphics g) {
        // Draw player and enemy knights
        mainLogic.getKnights().forEach(knight -> knight.draw(g));
        mainLogic.getEnemyKnights().forEach(knight -> knight.draw(g));

        // Draw player and enemy archers
        mainLogic.getArchers().forEach(archer -> archer.draw(g));
        mainLogic.getEnemyArchers().forEach(archer -> archer.draw(g));

        // Draw player and enemy tanks
        mainLogic.getTanks().forEach(tank -> tank.draw(g));
        mainLogic.getEnemyTanks().forEach(tank -> tank.draw(g));
    }

    private void displayGold(Graphics g) {
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Courier", Font.BOLD, 23));
        g.drawString("Coins: " + mainLogic.getPlayerGold(), 20, 220);
        g.drawString("Coins: " + mainLogic.getEnemyGold(), 1550, 220);
    }
}
