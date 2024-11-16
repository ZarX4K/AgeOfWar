package AgeOfWar.Graphics;

import AgeOfWar.Characters.BaseCharacterStats;
import AgeOfWar.Characters.Knight;
import AgeOfWar.Graphics.BackGroundScreens;
import AgeOfWar.Logic.MainLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GameGraphics extends JFrame {
    private MainLogic mainLogic;
    private BackGroundScreens backGroundScreens;

    public GameGraphics() {
        // Initialize mainLogic and backGroundScreens first
        mainLogic = new MainLogic();
        backGroundScreens = new BackGroundScreens();

        setTitle("AGE OF WAR");
        setPreferredSize(new Dimension(1710, 990));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("gameLogo.png")).getImage());

        // Create the gamePanel and add it to the window
        GamePanel gamePanel = new GamePanel(mainLogic, backGroundScreens);
        add(gamePanel);

        // Set the game panel in mainLogic and initialize it
        mainLogic.setGamePanel(gamePanel);
        mainLogic.initialize();
        mainLogic.startGameThread();

        // Final setup for the window
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void drawCharacters(Graphics g, List<? extends BaseCharacterStats> characters) {
        for (BaseCharacterStats character : characters) {
            character.draw(g); // Use character's draw method including health bar
        }
    }

    private class GamePanel extends JPanel {


        public GamePanel(MainLogic mainLogic, BackGroundScreens backGroundScreens) {
            setBackground(Color.black);
            setFocusable(true);

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (mainLogic.getGameState() == 1) {
                // Use ImageIcon to draw the animated GIF
                backGroundScreens.getIntroImageIcon().paintIcon(this, g, 0, 0);
            } else if (mainLogic.getGameState() == 2) {
                g.drawImage(backGroundScreens.getBackgroundImage(), 0, 0, null);

                for (Knight knight : mainLogic.getKnights()) {
                    g.drawImage(knight.getWalkImage(), knight.getX(), knight.getY(), knight.getWidth(), knight.getHeight(), null);
                }
                for (Knight knight : mainLogic.getEnemyKnights()) {
                    g.drawImage(knight.getWalkImage(), knight.getX(), knight.getY(), knight.getWidth(), knight.getHeight(), null);
                }

                drawCharacters(g, mainLogic.getKnights());
                // Add other character lists here (e.g., archers, tanks)

                // Draw all enemy characters
                drawCharacters(g, mainLogic.getEnemyKnights());
                // Add other enemy character lists here (e.g., enemy archers, enemy tanks)
                // Display the gold of each player
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Arial", Font.BOLD, 18));
                g.drawString("Coins: " + mainLogic.getPlayerGold(), 20, 40); // Left top corner
                g.drawString("Coins: " + mainLogic.getEnemyPlayerGold(), 1600, 40); // Right top corner
            }

        }

    }
}
