package AgeOfWar.Logic;

import AgeOfWar.Characters.*;
import AgeOfWar.Graphics.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MainLogic implements Runnable {

    // Constants
    private static final long KNIGHT_SPAWN_DELAY = 1500;
    private static final long ARCHER_SPAWN_DELAY = 2000;
    private static final long TANK_SPAWN_DELAY = 4000;
    private static final int FPS = 60;
    private static final double DRAW_INTERVAL = 1000000000.0 / FPS;

    // Game state variables
    private int gameState = 1;
    private boolean gameStarted = false;
    private int playerGold = 500;
    private int enemyGold = 500;

    // Time tracking
    private long lastKnightSpawnTime = 0;
    private long lastArcherSpawnTime = 0;
    private long lastTankSpawnTime = 0;
    private long lastEnemyKnightSpawnTime = 0;
    private long lastEnemyArcherSpawnTime = 0;
    private long lastEnemyTankSpawnTime = 0;
    private long currentTime;
    private double delta = 0;
    private long lastTime = System.nanoTime();
    public static long getSpawnDelay(String characterType) {
        switch (characterType.toLowerCase()) {
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


    // Game components
    private Music music;
    private GameGraphics gameGraphics;
    private Moving moving;
    private Collisions collisions;
    private Hitboxes hitboxes;
    private Attack attack;
    private BackGroundScreens backGroundScreens;
    private JPanel gamePanel;
    private Thread gameThread;

    // Key input
    private KeyReader keyReader;

    // Characters
    private final List<Knight> knights = new ArrayList<>();
    private final List<Archer> archers = new ArrayList<>();
    private final List<Tank> tanks = new ArrayList<>();
    private final List<Knight> enemyKnights = new ArrayList<>();
    private final List<Archer> enemyArchers = new ArrayList<>();
    private final List<Tank> enemyTanks = new ArrayList<>();
    private final List<BaseCharacterStats> deadCharacters = new ArrayList<>();

    // Initialization
    public void initialize() {
        music = new Music();
        moving = new Moving();
        hitboxes = new Hitboxes();
        attack = new Attack();
        collisions = new Collisions(moving, hitboxes, attack, this);

        keyReader = new KeyReader();

        music.loadMusic("intro1.wav");
        music.playMusic();

        setupGamePanel();
    }

    private void setupGamePanel() {
        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(keyReader);
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!gameStarted) {
                    startGame();
                }
            }
        });
    }

    private void startGame() {
        gameStarted = true;
        gameState = 2;
        System.out.println("Game state changed to In-Game!");
        music.stopMusic();
        music.loadMusic("souboj1.wav");
        music.playMusic();
    }

    public void setGamePanel(JPanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    // Spawning methods
    private void spawnKnight() {
        spawnCharacter(knights, new Knight(150, 800, 150, 150, "knight.png", "knight.png", "knight.png", 100, 20, 25, 1, true, false, false, false, 10), playerGold, lastKnightSpawnTime, KNIGHT_SPAWN_DELAY, false);
    }

    private void spawnArcher() {
        spawnCharacter(archers, new Archer(150, 800, 150, 150, "archer.png", "archer.png", "archer.png", 100, 15, 50, 1, true, false, false, false, 5, 10), playerGold, lastArcherSpawnTime, ARCHER_SPAWN_DELAY, false);
    }

    private void spawnTank() {
        spawnCharacter(tanks, new Tank(150, 800, 150, 150, "Tank.png", "Tank.png", "Tank.png", 200, 30, 100, 1, true, false, false, false, 10, 20, 20), playerGold, lastTankSpawnTime, TANK_SPAWN_DELAY, false);
    }

    private void spawnEnemyKnight() {
        spawnCharacter(enemyKnights, new Knight(1400, 800, 150, 150, "enemyKnight.png", "enemyKnight.png", "enemyKnight.png", 100, 20, 25, 1, true, true, false, false, 10), enemyGold, lastEnemyKnightSpawnTime, KNIGHT_SPAWN_DELAY, true);
    }

    private void spawnEnemyArcher() {
        spawnCharacter(enemyArchers, new Archer(1400, 800, 150, 150, "enemyArcher.png", "enemyArcher.png", "enemyArcher.png", 100, 15, 50, 1, true, true, false, false, 5, 10), enemyGold, lastEnemyArcherSpawnTime, ARCHER_SPAWN_DELAY, true);
    }

    private void spawnEnemyTank() {
        spawnCharacter(enemyTanks, new Tank(1400, 800, 150, 150, "enemyTank.png", "enemyTank.png", "enemyTank.png", 200, 30, 100, 1, true, true, false, false, 15, 10, 20), enemyGold, lastEnemyTankSpawnTime, TANK_SPAWN_DELAY, true);
    }

    private <T extends BaseCharacterStats> void spawnCharacter(List<T> characterList, T character, int gold, long lastSpawnTime, long spawnDelay, boolean isEnemy) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSpawnTime >= spawnDelay && gold >= character.priceBuy) {
            characterList.add(character);
            System.out.println(character.getClass().getSimpleName() + " spawned");
            if (isEnemy) deductEnemyGold(character.priceBuy);
            else deductPlayerGold(character.priceBuy);

            moving.moveCharacter(character, new ArrayList<>(characterList));
        }
    }

    // Update logic
    public void update() {
        handleSpawning();
        handleCollisions();

        removeDefeatedCharacters(knights);
        removeDefeatedCharacters(archers);
        removeDefeatedCharacters(tanks);
        removeDefeatedCharacters(enemyKnights);
        removeDefeatedCharacters(enemyArchers);
        removeDefeatedCharacters(enemyTanks);
        List<BaseCharacterStats> allAllies = new ArrayList<>();
        allAllies.addAll(knights);
        allAllies.addAll(archers);
        allAllies.addAll(tanks);

        List<BaseCharacterStats> allEnemies = new ArrayList<>();
        allEnemies.addAll(enemyKnights);
        allEnemies.addAll(enemyArchers);
        allEnemies.addAll(enemyTanks);

        for (BaseCharacterStats ally : allAllies) {
            moving.moveCharacter(ally, allAllies); // Ensuring infront method is called
        }

        for (BaseCharacterStats enemy : allEnemies) {
            moving.moveCharacter(enemy, allEnemies); // Ensuring infront method is called
        }
        gamePanel.repaint();
    }

    private void handleSpawning() {
        if (keyReader.knightSpawn) {
            spawnKnight();
            keyReader.knightSpawn = false; // Reset the flag after spawning
        }
        if (keyReader.archerSpawn) {
            spawnArcher();
            keyReader.archerSpawn = false; // Reset the flag after spawning
        }
        if (keyReader.tankSpawn) {
            spawnTank();
            keyReader.tankSpawn = false; // Reset the flag after spawning
        }
        if (keyReader.ENEMYknightSpawn) {
            spawnEnemyKnight();
            keyReader.ENEMYknightSpawn = false; // Reset the flag after spawning
        }
        if (keyReader.ENEMYarcherSpawn) {
            spawnEnemyArcher();
            keyReader.ENEMYarcherSpawn = false; // Reset the flag after spawning
        }
        if (keyReader.ENEMYtankSpawn) {
            spawnEnemyTank();
            keyReader.ENEMYtankSpawn = false; // Reset the flag after spawning
        }
    }


    private void handleCollisions() {
        List<BaseCharacterStats> playerCharacters = new ArrayList<>();
        playerCharacters.addAll(knights);
        playerCharacters.addAll(archers);
        playerCharacters.addAll(tanks);

        List<BaseCharacterStats> enemyCharacters = new ArrayList<>();
        enemyCharacters.addAll(enemyKnights);
        enemyCharacters.addAll(enemyArchers);
        enemyCharacters.addAll(enemyTanks);

        collisions.checkCollisions(playerCharacters, enemyCharacters);
    }

    private void removeDefeatedCharacters(List<? extends BaseCharacterStats> charactersList) {
        charactersList.removeIf(character -> {
            if (character.getHealth() <= 0 && !character.isDefeated()) {
                character.setDefeated(true);
                return true;
            }
            return false;
        });
    }


    // Utility methods

    public void awardGoldForKill(BaseCharacterStats character) {
        if (character.isEnemy()) {
            playerGold += character.getGoldReward();
            System.out.println("Player awarded " + character.getGoldReward() + " gold for killing an enemy.");
        } else {
            enemyGold += character.getGoldReward();
            System.out.println("Enemy awarded " + character.getGoldReward() + " gold for killing a player unit.");
        }
    }

    public void deductPlayerGold(int amount) {
        playerGold -= amount;
    }

    public void deductEnemyGold(int amount) {
        enemyGold -= amount;
    }

    // Game loop
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
            delta += (currentTime - lastTime) / DRAW_INTERVAL;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                delta--;
            }
        }
    }

    // Key handling
    public class KeyReader implements KeyListener {
        public boolean knightSpawn, archerSpawn, tankSpawn;
        public boolean ENEMYknightSpawn, ENEMYarcherSpawn, ENEMYtankSpawn;

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_A) knightSpawn = true;
            if (e.getKeyCode() == KeyEvent.VK_S) archerSpawn = true;
            if (e.getKeyCode() == KeyEvent.VK_D) tankSpawn = true;
            if (e.getKeyCode() == KeyEvent.VK_L) ENEMYknightSpawn = true;
            if (e.getKeyCode() == KeyEvent.VK_K) ENEMYarcherSpawn = true;
            if (e.getKeyCode() == KeyEvent.VK_J) ENEMYtankSpawn = true;
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_A) knightSpawn = false;
            if (e.getKeyCode() == KeyEvent.VK_S) archerSpawn = false;
            if (e.getKeyCode() == KeyEvent.VK_D) tankSpawn = false;
            if (e.getKeyCode() == KeyEvent.VK_L) ENEMYknightSpawn = false;
            if (e.getKeyCode() == KeyEvent.VK_K) ENEMYarcherSpawn = false;
            if (e.getKeyCode() == KeyEvent.VK_J) ENEMYtankSpawn = false;
        }
    }



    public List<Tank> getEnemyTanks() {
        return enemyTanks;
    }

    public List<Archer> getEnemyArchers() {
        return enemyArchers;
    }

    public List<Knight> getEnemyKnights() {
        return enemyKnights;
    }

    public List<Tank> getTanks() {
        return tanks;
    }

    public List<Archer> getArchers() {
        return archers;
    }

    public List<Knight> getKnights() {
        return knights;
    }


    public long getLastEnemyKnightSpawnTime() {
        return lastEnemyKnightSpawnTime;
    }



    public long getLastKnightSpawnTime() {
        return lastKnightSpawnTime;
    }



    public int getEnemyGold() {
        return enemyGold;
    }


    public int getPlayerGold() {
        return playerGold;
    }





    public int getGameState() {
        return gameState;
    }


}
