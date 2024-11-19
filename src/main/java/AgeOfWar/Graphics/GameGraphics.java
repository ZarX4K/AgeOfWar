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

        public GamePanel(MainLogic mainLogic, BackGroundScreens backGroundScreens) {
            this.mainLogic = mainLogic;
            setBackground(Color.black);
            setFocusable(true);
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

                // Draw hotbars
             //   drawPlayerHotbar(g);
            //    drawEnemyHotbar(g);
            }
        }
    }
}