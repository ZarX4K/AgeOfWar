package AgeOfWar.Logic;

import AgeOfWar.Characters.Archer;
import AgeOfWar.Characters.BaseCharacterStats;
import AgeOfWar.Characters.Knight;
import AgeOfWar.Characters.Tank;
import AgeOfWar.Graphics.BackGroundScreens;
import AgeOfWar.Graphics.GameGraphics;
import AgeOfWar.Graphics.Music;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MainLogic implements Runnable {
    private Music music;
    private GameGraphics gameGraphics;
    private List<Knight> knights;
    private List<Knight> enemyKnights;
    private List<BaseCharacterStats> characters = new ArrayList<>();
    private List<Archer> archers = new ArrayList<>();
    private List<Tank> tanks = new ArrayList<>();
    private List<Archer> enemyArchers = new ArrayList<>();
    private List<Tank> enemyTanks = new ArrayList<>();
    private Moving moving;
    // Process the movement of all characters

    private Tank tank;
    private Archer archer;
    private Collisions collisions;
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
    private int enemyGold= 500;
    private long lastKnightSpawnTime = 0;
    private long lastEnemyKnightSpawnTime = 0;
    private static final long KNIGHT_SPAWN_DELAY = 1500; // 1.5 seconds for knights
    private static final long ARCHER_SPAWN_DELAY = 2000; // 2 seconds for archers
    private static final long TANK_SPAWN_DELAY = 4000; // 4 seconds for tanks
    private long lastArcherSpawnTime = 0;
    private long lastTankSpawnTime = 0;
    private List<BaseCharacterStats> deadCharacters = new ArrayList<>();
    private long lastEnemyArcherSpawnTime = 0;
    private long lastEnemyTankSpawnTime = 0;
    public static long getSpawnDelay(String characterType) {
        switch (characterType) {
            case "knight":
                return KNIGHT_SPAWN_DELAY;
            case "archer":
                return ARCHER_SPAWN_DELAY;
            case "tank":
                return TANK_SPAWN_DELAY;
            default:
                throw new IllegalArgumentException("Unknown character type: " + characterType);
        }
    }

    public void setGamePanel(JPanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void initialize() {
        hitboxes = new Hitboxes();  // Create the Hitboxes object first
        moving = new Moving(hitboxes);  // Pass it to Moving
        music = new Music();
        music.loadMusic("intro1.wav");
        music.playMusic();
        knights = new ArrayList<>();
        enemyKnights = new ArrayList<>();
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
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastKnightSpawnTime >= KNIGHT_SPAWN_DELAY) {
            Knight knight = new Knight(150, 800, 150, 150, "knight.png", "knight.png", "knight.png", 100, 20, 25, 1, true, false, false, 10);
            if (playerGold >= knight.priceBuy) {
                knights.add(knight);
                System.out.println("Knight spawned");
                deductPlayerGold(knight.priceBuy);  // Deduct the cost of the knight from the player's gold
                moving.moveCharacter(knight, new ArrayList<BaseCharacterStats>(knights));  // Move the knight after spawning
                lastKnightSpawnTime = currentTime; // Update last spawn time for the player knight
            } else {
                System.out.println("Not enough gold to spawn the knight!");
            }
        }
    }

    private void spawnArcher() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastArcherSpawnTime >= ARCHER_SPAWN_DELAY) {
            Archer archer = new Archer(150, 800, 150, 150, "archer.png", "archer.png", "archer.png", 100, 15, 50, 1, true, false, false, 5,10);
            if (playerGold >= archer.priceBuy) {
                archers.add(archer);
                System.out.println("Archer spawned");
                deductPlayerGold(archer.priceBuy);  // Deduct the cost of the archer from the player's gold
                moving.moveCharacter(archer, new ArrayList<BaseCharacterStats>(archers));  // Move the archer after spawning
                lastArcherSpawnTime = currentTime; // Update spawn time
            } else {
                System.out.println("Not enough gold to spawn the archer!");
            }
        }
    }

    private void spawnTank() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTankSpawnTime >= TANK_SPAWN_DELAY) {
            Tank tank = new Tank(150, 800, 150, 150, "Tank.png", "Tank.png", "Tank.png", 200, 30, 100, 1, true, false, 15, 10, 20);
            if (playerGold >= tank.priceBuy) {
                tanks.add(tank);
                System.out.println("Tank spawned");
                deductPlayerGold(tank.priceBuy);  // Deduct the cost of the tank from the player's gold
                moving.moveCharacter(tank, new ArrayList<BaseCharacterStats>(tanks));  // Move the tank after spawning
                lastTankSpawnTime = currentTime; // Update spawn time
            } else {
                System.out.println("Not enough gold to spawn the tank!");
            }
        }
    }

    private void spawnEnemyKnight() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastEnemyKnightSpawnTime >= KNIGHT_SPAWN_DELAY) {
            Knight enemyKnight = new Knight(1400, 800, 150, 150, "enemyKnight.png", "enemyKnight.png", "enemyKnight.png", 100, 20, 25, 1, true, true, false,10);
            if (enemyGold >= enemyKnight.priceBuy) {
                enemyKnights.add(enemyKnight);
                System.out.println("Enemy Knight spawned");
                deductEnemyGold(enemyKnight.priceBuy);  // Deduct the cost of the enemy knight from the enemy's gold
                moving.moveCharacter(enemyKnight, new ArrayList<BaseCharacterStats>(enemyKnights));  // Move the enemy knight after spawning
                lastEnemyKnightSpawnTime = currentTime;
            } else {
                System.out.println("Not enough gold to spawn the enemy knight!");
            }
        }
    }

    private void spawnEnemyArcher() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastEnemyArcherSpawnTime >= ARCHER_SPAWN_DELAY) {
            Archer enemyArcher = new Archer(1400, 800, 150, 150, "enemyArcher.png", "enemyArcher.png", "enemyArcher.png", 100, 15, 50, 1, true, true, false, 5,10);
            if (enemyGold >= enemyArcher.priceBuy) {
                enemyArchers.add(enemyArcher);
                System.out.println("Enemy Archer spawned");
                deductEnemyGold(enemyArcher.priceBuy);  // Deduct the cost of the enemy archer from the enemy's gold
                moving.moveCharacter(enemyArcher, new ArrayList<BaseCharacterStats>(enemyArchers));  // Move the enemy archer after spawning
                lastEnemyArcherSpawnTime = currentTime;
            } else {
                System.out.println("Not enough gold to spawn the enemy archer!");
            }
        }
    }

    private void spawnEnemyTank() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastEnemyTankSpawnTime >= TANK_SPAWN_DELAY) {
            Tank enemyTank = new Tank(1400, 800, 150, 150, "enemyTank.png", "enemyTank.png", "enemyTank.png", 200, 30, 100, 1, true, true, 15, 10, 20);
            if (enemyGold >= enemyTank.priceBuy) {
                enemyTanks.add(enemyTank);
                System.out.println("Enemy Tank spawned");
                deductEnemyGold(enemyTank.priceBuy);  // Deduct the cost of the enemy tank from the enemy's gold
                moving.moveCharacter(enemyTank, new ArrayList<BaseCharacterStats>(enemyTanks));  // Move the enemy tank after spawning
                lastEnemyTankSpawnTime = currentTime;
            } else {
                System.out.println("Not enough gold to spawn the enemy tank!");
            }
        }
    }




    public void awardGoldForKill(BaseCharacterStats character) {
        if (character.isEnemy()) {
            playerGold += character.getPriceBuy();
            System.out.println("Player A awarded " + character.getPriceBuy() + " gold for killing an enemy " + character.getClass().getSimpleName() + "!");
        } else {
            enemyGold += character.getPriceBuy();
            System.out.println("Enemy Player awarded " + character.getPriceBuy() + " gold for killing a " + character.getClass().getSimpleName() + "!");
        }
    }




    public void update() {
        long currentTime = System.currentTimeMillis();

        if (keyReader.knightSpawn && (currentTime - lastKnightSpawnTime >= KNIGHT_SPAWN_DELAY)) {
            spawnKnight();
            keyReader.knightSpawn = false;
        }
        if (keyReader.archerSpawn && (currentTime - lastArcherSpawnTime >= ARCHER_SPAWN_DELAY)) {
            spawnArcher();
            keyReader.archerSpawn = false;
        }
        if (keyReader.tankSpawn && (currentTime - lastTankSpawnTime >= TANK_SPAWN_DELAY)) {
            spawnTank();
            keyReader.tankSpawn = false;
        }

        // Handle enemy spawns
        if (keyReader.ENEMYknightSpawn && (currentTime - lastEnemyKnightSpawnTime >= KNIGHT_SPAWN_DELAY)) {
            spawnEnemyKnight();
            keyReader.ENEMYknightSpawn = false;
        }
        if (keyReader.ENEMYarcherSpawn && (currentTime - lastEnemyArcherSpawnTime >= ARCHER_SPAWN_DELAY)) {
            spawnEnemyArcher();
            keyReader.ENEMYarcherSpawn = false;
        }
        if (keyReader.ENEMYtankSpawn && (currentTime - lastEnemyTankSpawnTime >= TANK_SPAWN_DELAY)) {
            spawnEnemyTank();
            keyReader.ENEMYtankSpawn = false;
        }

        List<BaseCharacterStats> allCharacters = new ArrayList<>();
        allCharacters.addAll(knights);
        allCharacters.addAll(archers);
        allCharacters.addAll(tanks);
        allCharacters.addAll(enemyKnights);
        allCharacters.addAll(enemyArchers);
        allCharacters.addAll(enemyTanks);

        for (BaseCharacterStats character : allCharacters) {
            if (character.isAlive() && character.isMoving()) {
                moving.moveCharacter(character, allCharacters);  // Pass all characters for movement and collision checks
            } else if (!character.isAlive()) {
                deadCharacters.add(character);
            }
        }

        // Update and check movement for Knights, Archers, and Tanks
        for (BaseCharacterStats character : characters) {
            if (character.isAlive() && character.isMoving()) {
                moving.moveCharacter(character, characters);  // Pass all characters for movement and collision checks
            } else if (!character.isAlive()) {
                deadCharacters.add(character);
            }
        }

        // Handle collisions
        collisions.checkCollisions(allCharacters);

        // Award gold and remove dead characters
        for (BaseCharacterStats deadCharacter : deadCharacters) {
            if (deadCharacter instanceof Knight) {
                knights.remove(deadCharacter);
            } else if (deadCharacter instanceof Archer) {
                archers.remove(deadCharacter);
            } else if (deadCharacter instanceof Tank) {
                tanks.remove(deadCharacter);
            }

            awardGoldForKill(deadCharacter);
        }

        gamePanel.repaint();
    }

    public int getEnemyGold() {
        return enemyGold;
    }


    public class KeyReader implements KeyListener {
        public boolean knightSpawn, archerSpawn, tankSpawn, skyAttackSpawn;
        public boolean ENEMYknightSpawn, ENEMYarcherSpawn, ENEMYtankSpawn, ENEMYskyAttackSpawn;

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_A) knightSpawn = true;
            if (e.getKeyCode() == KeyEvent.VK_S) archerSpawn = true;  // New key for spawning archer
            if (e.getKeyCode() == KeyEvent.VK_D) tankSpawn = true;     // New key for spawning tank
            if (e.getKeyCode() == KeyEvent.VK_L) ENEMYknightSpawn = true;
            if (e.getKeyCode() == KeyEvent.VK_K) ENEMYarcherSpawn = true; // Key for enemy archer spawn
            if (e.getKeyCode() == KeyEvent.VK_J) ENEMYtankSpawn = true; // New key for enemy tank
        }

        @Override
        public void keyReleased(KeyEvent e) {

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
    public List<Archer> getArchers() {
        return archers;
    }

    public List<Archer> getEnemyArchers() {
        return enemyArchers;
    }

    public List<Tank> getTanks() {
        return tanks;
    }

    public List<Tank> getEnemyTanks() {
        return enemyTanks;
    }


    public int getGameState() {
        return gameState;
    }
    public int getPlayerGold() {
        return playerGold;
    }


    public long getLastKnightSpawnTime() {
        return lastKnightSpawnTime;
    }

    public long getLastEnemyKnightSpawnTime() {
        return lastEnemyKnightSpawnTime;
    }




    // Assuming you have these getter and setter methods:


    // Example methods for handling gold
    public void deductPlayerGold(int amount) {
        playerGold -= amount;
    }

    public void deductEnemyGold(int amount) {
        enemyGold -= amount;
    }


}

