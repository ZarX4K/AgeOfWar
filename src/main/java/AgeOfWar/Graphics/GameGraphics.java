package AgeOfWar.Graphics;

import AgeOfWar.Characters.Archer;
import AgeOfWar.Characters.BaseCharacterStats;
import AgeOfWar.Characters.Knight;
import AgeOfWar.Characters.Tank;
import AgeOfWar.Graphics.BackGroundScreens;
import AgeOfWar.Logic.MainLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import static AgeOfWar.Logic.MainLogic.getSpawnDelay;

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
        private MainLogic mainLogic;

        public GamePanel(MainLogic mainLogic, BackGroundScreens backGroundScreens) {
            this.mainLogic = mainLogic;
            setBackground(Color.black);
            setFocusable(true);
        }

        private void drawCharacters(Graphics g, List<? extends BaseCharacterStats> characters) {
            for (BaseCharacterStats character : characters) {
                character.draw(g); // Use character's draw method including health bar
            }
        }



            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (mainLogic.getGameState() == 1) {
                    backGroundScreens.getIntroImageIcon().paintIcon(this, g, 0, 0);
                } else if (mainLogic.getGameState() == 2) {
                    g.drawImage(backGroundScreens.getBackgroundImage(), 0, 0, null);

                    // Draw player and enemy knights
                    for (Knight knight : mainLogic.getKnights()) {
                        knight.draw(g);  // Use the draw method from BaseCharacterStats
                    }
                    for (Knight knight : mainLogic.getEnemyKnights()) {
                        knight.draw(g);  // Draw enemy knights similarly
                    }

                    // You can similarly draw other character types, e.g., archers and tanks:
                    for (Archer archer : mainLogic.getArchers()) {
                        archer.draw(g);  // Draw player archers
                    }
                    for (Archer archer : mainLogic.getEnemyArchers()) {
                        archer.draw(g);  // Draw enemy archers
                    }
                    for (Tank tank : mainLogic.getTanks()) {
                        tank.draw(g);  // Draw player tanks
                    }
                    for (Tank tank : mainLogic.getEnemyTanks()) {
                        tank.draw(g);  // Draw enemy tanks
                    }

                    // Display the gold of each player
                    g.setColor(Color.YELLOW);
                    g.setFont(new Font("Courier", Font.BOLD, 23));
                    g.drawString("Coins: " + mainLogic.getPlayerGold(), 20, 220);
                    g.drawString("Coins: " + mainLogic.getEnemyGold(), 1550, 220);

                    // Calculate and display spawn readiness bars
                    long currentTime = System.currentTimeMillis();

                    // Player's spawn bar
                    long knightSpawnProgress = currentTime - mainLogic.getLastKnightSpawnTime();
                    int barWidth = 250;
                    int barHeight = 15;
                    int barX = 500;
                    int barY = 60;

                    g.setColor(Color.RED);
                    int filledWidth = Math.min((int) (barWidth * knightSpawnProgress / MainLogic.getSpawnDelay("knight")), barWidth);
                    g.fillRect(barX, barY, filledWidth, barHeight);

                    if (filledWidth == barWidth) {
                        g.setColor(Color.GREEN);
                        g.fillRect(barX, barY, barWidth, barHeight);
                    }

                    // Draw outline for player's spawn bar
                    g.setColor(Color.BLACK);
                    g.drawRect(barX, barY, barWidth, barHeight);

                    // Enemy's spawn bar
                    long enemyKnightSpawnProgress = currentTime - mainLogic.getLastEnemyKnightSpawnTime();
                    int enemyBarWidth = 250;
                    int enemyBarHeight = 15;
                    int enemyBarX = 1170 - enemyBarWidth; // Same x-position but mirrored for right-to-left loading
                    int enemyBarY = 60;

                    g.setColor(Color.RED);
                    int enemyFilledWidth = Math.min((int) (enemyBarWidth * enemyKnightSpawnProgress / MainLogic.getSpawnDelay("knight")), enemyBarWidth);
                    g.fillRect(enemyBarX + (enemyBarWidth - enemyFilledWidth), enemyBarY, enemyFilledWidth, enemyBarHeight);

                    if (enemyFilledWidth == enemyBarWidth) {
                        g.setColor(Color.GREEN);
                        g.fillRect(enemyBarX, enemyBarY, enemyBarWidth, enemyBarHeight);
                    }

                    // Draw outline for enemy's spawn bar
                    g.setColor(Color.BLACK);
                    g.drawRect(enemyBarX, enemyBarY, enemyBarWidth, enemyBarHeight);
                }
            }
        }
    }
