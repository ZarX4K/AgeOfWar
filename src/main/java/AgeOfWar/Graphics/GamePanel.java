package AgeOfWar.Graphics;

import AgeOfWar.Characters.Projectile;
import AgeOfWar.Logic.MainLogic;
import AgeOfWar.Logic.GameState;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private final MainLogic mainLogic;
    private final Hotbar playerHotbar;
    private final Hotbar enemyHotbar;

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

        // Check current game state
        GameState currentState = mainLogic.getGameState();

        switch (currentState) {
            case INTRO:
                drawIntroScreen(g);
                break;
            case IN_GAME:
                drawGameScreen(g);
                break;
            case GAME_OVER:
                drawScreenB(g);
                break;
            case ENEMY_WIN:
                drawScreenA(g);
                break;
            default:
                throw new IllegalStateException("Unexpected game state: " + currentState);
        }
    }

    private void drawIntroScreen(Graphics g) {
        g.drawImage(mainLogic.getBackGroundScreens().getIntroImageIcon().getImage(), 0, 0, null);
    }

    private void drawScreenA(Graphics g) {
        g.drawImage(mainLogic.getBackGroundScreens().getEndScreenA(), 0, 0, null);
    }

    private void drawScreenB(Graphics g) {
        g.drawImage(mainLogic.getBackGroundScreens().getEndScreenB(), 0, 0, null);
    }

    private void drawGameScreen(Graphics g) {
        g.drawImage(mainLogic.getBackGroundScreens().getBackgroundImage(), 0, 0, null);

        // Draw hotbars
        playerHotbar.draw(g, mainLogic);
        enemyHotbar.draw(g, mainLogic);

        // Draw castles
        mainLogic.getPlayerCastle().draw(g);
        mainLogic.getEnemyCastle().draw(g);

        // Draw characters
        drawCharacters(g);

        // Draw projectiles
        drawProjectiles(g);

        // Display gold
        displayGold(g);
    }

    private void drawCharacters(Graphics g) {
        mainLogic.getKnights().forEach(knight -> knight.draw(g));
        mainLogic.getEnemyKnights().forEach(knight -> knight.draw(g));
        mainLogic.getArchers().forEach(archer -> archer.draw(g));
        mainLogic.getEnemyArchers().forEach(archer -> archer.draw(g));
        mainLogic.getTanks().forEach(tank -> tank.draw(g));
        mainLogic.getEnemyTanks().forEach(tank -> tank.draw(g));
    }

    private void drawProjectiles(Graphics g) {
        // Retrieve projectiles from MainLogic and draw them
        for (Projectile projectile : mainLogic.getProjectiles()) {
            projectile.draw(g, this);
        }
    }

    private void displayGold(Graphics g) {
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Courier", Font.BOLD, 23));
        g.drawString("Coins: " + mainLogic.getPlayerGold(), 20, 220);
        g.drawString("Coins: " + mainLogic.getEnemyGold(), 1550, 220);
    }
}
