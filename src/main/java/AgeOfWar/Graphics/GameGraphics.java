package AgeOfWar.Graphics;

import AgeOfWar.Logic.MainLogic;

import javax.swing.*;
import java.awt.*;

public class GameGraphics extends JFrame {
    private MainLogic mainLogic;

    public GameGraphics() {
        mainLogic = new MainLogic();

        setTitle("AGE OF WAR");
        setPreferredSize(new Dimension(1710, 990));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("gameLogo.png")).getImage());

        // Create the gamePanel and add it to the window
        GamePanel gamePanel = new GamePanel(mainLogic);
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
}
