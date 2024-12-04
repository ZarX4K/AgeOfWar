package AgeOfWar.Logic;

import AgeOfWar.Characters.*;
import AgeOfWar.Graphics.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainLogic implements Runnable {

    // Constants
    private static final long KNIGHT_SPAWN_DELAY = 1500;
    private static final long ARCHER_SPAWN_DELAY = 2000;
    private static final long  TANK_SPAWN_DELAY = 4000;
    private static final long ENEMY_KNIGHT_SPAWN_DELAY = 1500; // Example value
    private static final long ENEMY_ARCHER_SPAWN_DELAY = 2000; // Example value
    private static final long ENEMY_TANK_SPAWN_DELAY = 4000; //dd
    private static final int FPS = 60;
    private static final double DRAW_INTERVAL = 1000000000.0 / FPS;

    // Game state variables
    private GameState gameState = GameState.INTRO; // Use the enum here
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
    private long knightCooldownEnd = 0;
    private long archerCooldownEnd = 0;
    private long tankCooldownEnd = 0;
    private long enemyKnightCooldownEnd = 0;
    private long enemyArcherCooldownEnd = 0;
    private long enemyTankCooldownEnd = 0;
    private long currentTime;
    private double delta = 0;
    private long lastTime = System.nanoTime();



    // Game components/
    private Music music;
    private GameGraphics gameGraphics;
    private Moving moving;
    private Collisions collisions;
    private Hitboxes hitboxes;
    private Attack attack;
    private BackGroundScreens backGroundScreens;
    private JPanel gamePanel;
    private Thread gameThread; //

    // Key input
    private KeyReader keyReader;

    // Characters
    private final List<Knight> knights = new ArrayList<>();
    private final List<Archer> archers = new ArrayList<>();
    private final List<Tank> tanks = new ArrayList<>();
    private final List<Knight> enemyKnights = new ArrayList<>();
    private final List<Archer> enemyArchers = new ArrayList<>();
    private final List<Tank> enemyTanks = new ArrayList<>();
    private Castle playerCastle;
    private Castle enemyCastle;
    private final List<Projectile> projectiles = new ArrayList<>(); // Store active projectiles

    // Initialization
    public void initialize() {
        music = new Music();
        moving = new Moving();
        hitboxes = new Hitboxes();
        attack = new Attack();
        collisions = new Collisions(moving, hitboxes, attack, this);
        keyReader = new KeyReader(this);  // Pass the instance of MainLogic here
        backGroundScreens = new BackGroundScreens();

        music.loadMusic("intro1.wav");
        music.playMusic();

        setupGamePanel();

        playerCastle = new Castle(-100, 600, 400, 400, 500,"FortressP.png");  // Player's castle at the left bottom
        enemyCastle = new Castle(1400, 600, 400, 400, 500,"FortressE.png");  // Enemy's castle at the right bottomm


    }

    private void setupGamePanel() {
        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(keyReader);  // Attach the KeyListener to the game panel
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
        gameState = GameState.IN_GAME; // Use the enum here
        music.stopMusic();
        music.loadMusic("souboj1.wav");
        music.playMusic();
    }

    public void setGamePanel(JPanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    // Method to spawn a projectile
    public void spawnProjectile(BaseCharacterStats shooter) {
        String projectileImage = "Arrow.png"; // Default image for player's projectile
        if (shooter.isEnemy()) {
            projectileImage = "ArrowE.png"; // Use a different image for enemy's projectile
        }

        // Create the projectile based on the shooter's position and direction
        Projectile arrow = new Projectile(80, 80, shooter.getX(), shooter.getY(), shooter, projectileImage);
        projectiles.add(arrow);  // Add the projectile to a list of projectiles in the game
    }

    // Update logic for projectiles
    private void updateProjectiles() {
        Iterator<Projectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            projectile.update();
            if (!projectile.isActive()) {
                iterator.remove(); // Remove inactive projectiles
            }
        }
    }




    // Spawning methods
    private void spawnKnight() {
        Knight knight = new Knight(150, 800, 150, 150, "knight.png", "knight.png", "knight.png", 100, 20, 25, 3, true, false, false, false, 25);
        if (isSpawnAvailable(playerGold, lastKnightSpawnTime, KNIGHT_SPAWN_DELAY, knight.getPriceBuy())) {
            deductPlayerGold(knight.getPriceBuy());
            knights.add(knight);
            lastKnightSpawnTime = System.currentTimeMillis();
            knightCooldownEnd = lastKnightSpawnTime + KNIGHT_SPAWN_DELAY;
            moving.moveCharacter(knight, new ArrayList<>(knights));
      }
    }

    private void spawnArcher() {
        Archer archer = new Archer(150, 800, 150, 150, "archer.png", "archer.png", "archer.png", 100, 15, 50, 3, true, false, false, false, 20, 150);
        if (isSpawnAvailable(playerGold, lastArcherSpawnTime, ARCHER_SPAWN_DELAY, archer.getPriceBuy())) {
            deductPlayerGold(archer.getPriceBuy());
            archers.add(archer);
            lastArcherSpawnTime = System.currentTimeMillis();
            archerCooldownEnd = lastArcherSpawnTime + ARCHER_SPAWN_DELAY;
            moving.moveCharacter(archer, new ArrayList<>(archers));
        }
    }

    private void spawnTank() {
        Tank tank = new Tank(150, 800, 150, 150, "Tank.png", "Tank.png", "Tank.png", 170, 25, 120, 3, true, false, false, false, 30, 150, 15);
        if (isSpawnAvailable(playerGold, lastTankSpawnTime, TANK_SPAWN_DELAY, tank.getPriceBuy())) {
            deductPlayerGold(tank.getPriceBuy());
            tanks.add(tank);
            lastTankSpawnTime = System.currentTimeMillis();
            tankCooldownEnd = lastTankSpawnTime + TANK_SPAWN_DELAY;
            moving.moveCharacter(tank, new ArrayList<>(tanks));
        }
    }


    private void spawnEnemyKnight() {
        Knight enemyKnight = new Knight(1400, 800, 150, 150, "enemyKnight.png", "enemyKnight.png", "enemyKnight.png", 100, 20, 25, 3, true, true, false, false, 25);
        if (isEnemySpawnAvailable(enemyGold, lastEnemyKnightSpawnTime, KNIGHT_SPAWN_DELAY, enemyKnight.getPriceBuy())) {
            enemyKnights.add(enemyKnight);
            deductEnemyGold(enemyKnight.getPriceBuy());
            lastEnemyKnightSpawnTime = System.currentTimeMillis();
            enemyKnightCooldownEnd = lastEnemyKnightSpawnTime + KNIGHT_SPAWN_DELAY;
            moving.moveCharacter(enemyKnight, new ArrayList<>(enemyKnights));
        }
    }

    private void spawnEnemyArcher() {
        Archer enemyArcher = new Archer(1400, 800, 150, 150, "enemyArcher.png", "enemyArcher.png", "enemyArcher.png", 100, 15, 50, 3, true, true, false, false, 20, 150);
        if (isEnemySpawnAvailable(enemyGold, lastEnemyArcherSpawnTime, ARCHER_SPAWN_DELAY, enemyArcher.getPriceBuy())) {
            enemyArchers.add(enemyArcher);
            deductEnemyGold(enemyArcher.getPriceBuy());
            lastEnemyArcherSpawnTime = System.currentTimeMillis();
            enemyArcherCooldownEnd = lastEnemyArcherSpawnTime + ARCHER_SPAWN_DELAY;
            moving.moveCharacter(enemyArcher, new ArrayList<>(enemyArchers));
        }
    }

    private void spawnEnemyTank() {
        Tank enemyTank = new Tank(1400, 800, 150, 150, "enemyTank.png", "enemyTank.png", "enemyTank.png", 170, 25, 120, 3, true, true, false, false, 30, 150, 15);
        if (isEnemySpawnAvailable(enemyGold, lastEnemyTankSpawnTime, TANK_SPAWN_DELAY, enemyTank.getPriceBuy())) {
            enemyTanks.add(enemyTank);
            deductEnemyGold(enemyTank.getPriceBuy());
            lastEnemyTankSpawnTime = System.currentTimeMillis();
            enemyTankCooldownEnd = lastEnemyTankSpawnTime + TANK_SPAWN_DELAY;
            moving.moveCharacter(enemyTank, new ArrayList<>(enemyTanks));
        }
    }


    private boolean isSpawnAvailable(int gold, long lastSpawnTime, long spawnDelay, int price) {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastSpawnTime >= spawnDelay && gold >= price &&
                currentTime >= knightCooldownEnd && currentTime >= archerCooldownEnd && currentTime >= tankCooldownEnd);
    }
    private boolean isEnemySpawnAvailable(int gold, long lastSpawnTime, long spawnDelay, int price) {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastSpawnTime >= spawnDelay && gold >= price &&
                currentTime >= enemyKnightCooldownEnd && currentTime >= enemyArcherCooldownEnd && currentTime >= enemyTankCooldownEnd);
    }

    public long getPlayerRemainingCooldown() {
        long currentTime = System.currentTimeMillis();
        long knightCooldown = Math.max(0, knightCooldownEnd - currentTime);
        long archerCooldown = Math.max(0, archerCooldownEnd - currentTime);
        long tankCooldown = Math.max(0, tankCooldownEnd - currentTime);

        // Return the longest remaining cooldown
        return Math.max(knightCooldown, Math.max(archerCooldown, tankCooldown));
    }

    public long getEnemyRemainingCooldown() {
        long currentTime = System.currentTimeMillis();
        long enemyKnightCooldown = Math.max(0, enemyKnightCooldownEnd - currentTime);
        long enemyArcherCooldown = Math.max(0, enemyArcherCooldownEnd - currentTime);
        long enemyTankCooldown = Math.max(0, enemyTankCooldownEnd - currentTime);

        // Return the longest remaining cooldown
        return Math.max(enemyKnightCooldown, Math.max(enemyArcherCooldown, enemyTankCooldown));
    }

    public double getCooldownProgress(String side) {
        long remainingCooldown;
        long spawnDelay;

        if (side.equals("player")) {
            remainingCooldown = getPlayerRemainingCooldown();
            spawnDelay = Math.max(KNIGHT_SPAWN_DELAY, Math.max(ARCHER_SPAWN_DELAY, TANK_SPAWN_DELAY));
        } else {
            remainingCooldown = getEnemyRemainingCooldown();
            spawnDelay = Math.max(ENEMY_KNIGHT_SPAWN_DELAY, Math.max(ENEMY_ARCHER_SPAWN_DELAY, ENEMY_TANK_SPAWN_DELAY));
        }

        // Calculate progress as a value between 0 and 1
        return 1.0 - (double) remainingCooldown / spawnDelay;
    }

    // Update logic
    public void update() {
        if (playerCastle.getHealth() <= 0 && gameState != GameState.GAME_OVER) {
            gameState = GameState.GAME_OVER; // Game over state
            music.stopMusic();
            music.loadMusic("Winn.wav");
            music.playMusic();
            return;
        }
        if (enemyCastle.getHealth() <= 0 && gameState != GameState.ENEMY_WIN) {
            gameState = GameState.ENEMY_WIN; // Enemy wins state
            music.stopMusic();
            music.loadMusic("Winn.wav");
            music.playMusic();
            return;
        }
        handleSpawning();
        handleCollisions();


        updateProjectiles(); // Update projectiles here



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
            keyReader.knightSpawn = false;
        }
        if (keyReader.archerSpawn) {
            spawnArcher();
            keyReader.archerSpawn = false;
        }
        if (keyReader.tankSpawn) {
            spawnTank();
            keyReader.tankSpawn = false;
        }
        if (keyReader.ENEMYknightSpawn) {
            spawnEnemyKnight();
            keyReader.ENEMYknightSpawn = false;
        }
        if (keyReader.ENEMYarcherSpawn) {
            spawnEnemyArcher();
            keyReader.ENEMYarcherSpawn = false;
        }
        if (keyReader.ENEMYtankSpawn) {
            spawnEnemyTank();
            keyReader.ENEMYtankSpawn = false;
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

        // Check projectile collisions
        for (Projectile projectile : projectiles) {
            // Check if the projectile hits any player character
            for (BaseCharacterStats character : playerCharacters) {
                if (hitboxes.collidesArrowCharacter(projectile, character)) {
                    projectile.setActive(false);
                    character.takeDamage(projectile.getDamage());  // Deal damage to player character
                }
            }

            // Check if the projectile hits any enemy character
            for (BaseCharacterStats character : enemyCharacters) {
                if (hitboxes.collidesArrowCharacter(projectile, character)) {
                    projectile.setActive(false);
                    character.takeDamage(projectile.getDamage());  // Deal damage to enemy character
                }
            }

            // Check projectile collision with player's castle (player projectiles should hit the enemy castle)
            if (hitboxes.collidesArrowCastle(projectile, playerCastle)) {
                projectile.setActive(false);
                playerCastle.takeDamage(projectile.getDamage());  // Deal damage to player's castle
            }

            // Check projectile collision with enemy's castle (enemy projectiles should hit the player's castle)
            if (hitboxes.collidesArrowCastle(projectile, enemyCastle)) {
                projectile.setActive(false);
                enemyCastle.takeDamage(projectile.getDamage());  // Deal damage to enemy's castle
            }
        }

        // Check other character collisions
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
    public void resetGame() {
        // Reset game variables and characters
        playerGold = 500;
        enemyGold = 500;
        gameState = GameState.INTRO;
        gameStarted= false;// Reset to intro screen
        playerCastle.setHealth(500);  // Reset player's castle health
        enemyCastle.setHealth(500);   // Reset enemy's castle health

        knights.clear();
        archers.clear();
        tanks.clear();
        enemyKnights.clear();
        enemyArchers.clear();
        enemyTanks.clear();

        // Reset music
        music.stopMusic();
        music.loadMusic("intro1.wav");
        music.playMusic();

    }



    // Key handling
    public class KeyReader implements KeyListener {
        private MainLogic mainLogic;  // Store the reference to MainLogic

        public KeyReader(MainLogic mainLogic) {
            this.mainLogic = mainLogic;  // Initialize the reference
        }

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
            if (e.getKeyCode() == KeyEvent.VK_ENTER && (mainLogic.getGameState() == GameState.GAME_OVER || mainLogic.getGameState() == GameState.ENEMY_WIN)) {
                mainLogic.resetGame();
            }
            if (e.getKeyCode() == KeyEvent.VK_G) {
                Archer dummyShooter = new Archer(800, 800, 150, 150, "Archer.png", "Archer.png", "Archer.png", 100, 15, 50, 1, true, true, false, false, 20, 10);
                archers.add(dummyShooter);
                mainLogic.spawnProjectile(dummyShooter); // This spawns the projectile (arrow)
            }
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
    // Getter for projectiles
    public List<Projectile> getProjectiles() {
        return projectiles;
    }


    public Castle getPlayerCastle() {
        return playerCastle;
    }

    public Castle getEnemyCastle() {
        return enemyCastle;
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

    public int getEnemyGold() {
        return enemyGold;
    }


    public int getPlayerGold() {
        return playerGold;
    }

    public BackGroundScreens getBackGroundScreens() {
        return backGroundScreens;
    }

    public GameState getGameState() {
        return gameState;
    }

}