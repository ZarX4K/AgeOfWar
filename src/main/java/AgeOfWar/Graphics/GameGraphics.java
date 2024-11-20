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


    private class GamePanel extends JPanel {
        private MainLogic mainLogic;
        private Hotbar playerHotbar;
        private Hotbar enemyHotbar;
        public GamePanel(MainLogic mainLogic, BackGroundScreens backGroundScreens) {
            this.mainLogic = mainLogic;
            setBackground(Color.black);
            setFocusable(true);
            playerHotbar = new Hotbar(500, 20, 150, 10, "player");
            enemyHotbar = new Hotbar(1000, 20, 150, 10, "enemy");
        }
        class Hotbar {
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
                if (side.equals("player")) {
                    // Player hotbar (left to right)
                    if (progress >= 1.0) {
                        g.setColor(Color.GREEN);
                    } else {
                        g.setColor(Color.RED);
                    }
                    g.fillRect(x, y, (int) (width * progress), height);
                } else if (side.equals("enemy")) {
                    // Enemy hotbar (right to left)
                    if (progress >= 1.0) {
                        g.setColor(Color.GREEN);
                    } else {
                        g.setColor(Color.RED);
                    }
                    g.fillRect(x + width - (int) (width * progress), y, (int) (width * progress), height);
                }

                // Optional: Draw border
                g.setColor(Color.BLACK);
                g.drawRect(x, y, width, height);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (mainLogic.getGameState() == 1) {
                backGroundScreens.getIntroImageIcon().paintIcon(this, g, 0, 0);
            } else if (mainLogic.getGameState() == 2) {
                g.drawImage(backGroundScreens.getBackgroundImage(), 0, 0, null);
                playerHotbar.draw(g, mainLogic);
                enemyHotbar.draw(g, mainLogic);

                mainLogic.getPlayerCastle().draw(g);
                mainLogic.getEnemyCastle().draw(g);

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


            }
        }
    }
}
