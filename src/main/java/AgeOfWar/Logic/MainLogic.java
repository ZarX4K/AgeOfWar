package AgeOfWar.Logic;

import AgeOfWar.Characters.Archer;
import AgeOfWar.Characters.Knight;
import AgeOfWar.Characters.Tank;
import AgeOfWar.Graphics.BackGroundScreens;
import AgeOfWar.Graphics.GameGraphics;
import AgeOfWar.Graphics.Music;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MainLogic implements Runnable {
    private Music music;
    private GameGraphics gameGraphics;
    private List<Knight> knights;
    private List<Knight> enemyKnights;
    private Tank tank;
    private Archer archer;
    private Collisions collisions;
    private Moving moving;
    private Hitboxes hitboxes;
    private Attack attack;
    private BackGroundScreens backGroundScreens;
    private int gameState = 1;
    private boolean gameStarted = false;
    private Thread gameThread;
    private long currentTime;
    private double delta = 0;
    private int fps = 60;
    private long lastTime = System.nanoTime();
    private double drawInterval = 1000000000 / fps;
    private JPanel gamePanel;
    private KeyReader keyReader;
    private int playerGold = 500;
    private int enemyPlayerGold = 500;

    public void setGamePanel(JPanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void initialize() {
        music = new Music();
        music.loadMusic("intro1.wav");
        music.playMusic();
        knights = new ArrayList<>();
        enemyKnights = new ArrayList<>();
        hitboxes = new Hitboxes();
        moving = new Moving();
        attack = new Attack(moving);
        collisions = new Collisions(moving, hitboxes, attack, this); // Pass "this" reference to MainLogic
        keyReader = new KeyReader();
        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(keyReader);
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!gameStarted) {
                    gameStarted = true;
                    gameState = 2;
                    System.out.println("Game state changed to In--Game!");
                    music.stopMusic();
                    music.loadMusic("souboj1.wav");
                    music.playMusic();

                }
            }
        });
    }


    private void spawnKnight() {
        Knight knight = new Knight(150, 800, 150, 150, "knight.png", "knight.png", "knight.png", 100, 20, 25, 1, true, false, 10); // Replaced fixed-price with your character class parameters
        if (playerGold >= knight.priceBuy) { // Assuming knight cost is in priceBuy attribute
            knights.add(knight);
            System.out.println("Knight spawned");
            playerGold -= knight.priceBuy; // Deducting priceBuy from playerGold
            moving.moveKnight(knight, knights); // Pass the list of knights for collision handling
        } else {
            System.out.println("Not enough gold to spawn the knightd!");
        }
    }

    private void spawnEnemyKnight() {
        Knight enemyKnight = new Knight(1400, 800, 150, 150, "enemyKnight.png", "enemyKnight.png", "enemyKnight.png", 100, 20, 25, 1, true, true, 10); // Replaced fixed-price with your character class parameters
        if (enemyPlayerGold >= enemyKnight.priceBuy) { // Assuming knight cost is in priceBuy attribute
            enemyKnights.add(enemyKnight);
            System.out.println("Enemy Knight spawned");
            enemyPlayerGold -= enemyKnight.priceBuy; // Deducting priceBuy from enemyPlayerGold
            moving.moveKnight(enemyKnight, enemyKnights); // Pass the list of enemy knights
        } else {
            System.out.println("Not enough gold to spawn the enemy knight!");
        }
    }
    void awardGoldForKill(Knight knight) {
        if (knight.isEnemy()) {
            playerGold += knight.priceBuy;
            System.out.println("Player A awarded " + knight.priceBuy + " gold for killing an enemy knight!");
        } else {
            enemyPlayerGold += knight.priceBuy;
            System.out.println("Enemy Player awarded " + knight.priceBuy + " gold for killing a knight!");
        }
    }
    public void update() {
        if (keyReader.knightSpawn) {
            spawnKnight();
            keyReader.knightSpawn = false;
        }
        if (keyReader.ENEMYknightSpawn) {
            spawnEnemyKnight();
            keyReader.ENEMYknightSpawn = false;
        }

        List<Knight> deadKnights = new ArrayList<>();
        List<Knight> deadEnemyKnights = new ArrayList<>();

        for (Knight knight : knights) {
            if (knight.isAlive() && knight.isMoving()) {
                moving.moveKnight(knight, knights); // Pass all knights for queue and collision checks
            } else if (!knight.isAlive()) {
                deadKnights.add(knight);
            }
        }

        for (Knight enemyKnight : enemyKnights) {
            if (enemyKnight.isAlive() && enemyKnight.isMoving()) {
                moving.moveKnight(enemyKnight, enemyKnights); // Pass all enemy knights
            } else if (!enemyKnight.isAlive()) {
                deadEnemyKnights.add(enemyKnight);
            }
        }

        collisions.checkCollisions(knights, enemyKnights);

        // Award gold and remove dead knights
        for (Knight deadKnight : deadKnights) {
            knights.remove(deadKnight);
            awardGoldForKill(deadKnight);
        }

        for (Knight deadEnemyKnight : deadEnemyKnights) {
            enemyKnights.remove(deadEnemyKnight);
            awardGoldForKill(deadEnemyKnight);
        }

        gamePanel.repaint();
    }




    public class KeyReader implements KeyListener {
        public boolean knightSpawn, archerSpawn, tankSpawn, skyAttackSpawn;
        public boolean ENEMYknightSpawn, ENEMYarcherSpawn, ENEMYtankSpawn, ENEMYskyAttackSpawn;

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_A) knightSpawn = true;
            if (e.getKeyCode() == KeyEvent.VK_L) ENEMYknightSpawn = true;
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_A) knightSpawn = false;
            if (e.getKeyCode() == KeyEvent.VK_L) ENEMYknightSpawn = false;
        }
    }

    public void startGameThread() {
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    @Override
    public void run() {
        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                delta--;
            }
        }
    }

    public void resetGame() {
        gameState = 1;
        gameStarted = false;
        gamePanel.repaint();
    }

    public List<Knight> getKnights() {
        return knights;
    }

    public List<Knight> getEnemyKnights() {
        return enemyKnights;
    }

    public int getGameState() {
        return gameState;
    }
    public int getPlayerGold() {
        return playerGold;
    }

    public int getEnemyPlayerGold() {
        return enemyPlayerGold;
    }

}
