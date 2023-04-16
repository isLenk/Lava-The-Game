package Talban.Final;

import processing.core.PApplet;

import java.awt.event.KeyEvent;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;

public class GameRunner extends PApplet {
    // Stores all the player objects
    private static ArrayList<Player> playerList;
    // The lava that flows upwards
    private static DeadZone deadZone;
    // Stores all the platforms that the player can stand on
    private static ArrayList<Platform> platforms;
    // Stores all the coin/boosters that the player can collect
    private static ArrayList<Coin> coins;
    // Stores all the background objects
    private static ArrayList<BackgroundObject> backgroundObjects;
    // Stores all the falling barrels
    private static ArrayList<FallingBarrel> fallingBarrelList;
    // The introduction screen and results screen class
    private static GameIntro gameIntro;

    Random rng;
    // Determines the games current status
    static String gameStatus;

    // A delay timer that will count down until the next platform can be created
    private int nextPlatformDelay;

    // Used to determine how to lerp the background color
    private float timeOfDay;

    // A boolean that will determine how the background color will lerp.
    private boolean day;

    // Set window height
    public void settings() {
        size(1280, 720, P2D);
    }

    public void setup() {
        frameRate(60);
        surface.setTitle("Lava! The Game");

        gameStatus = "";
        nextPlatformDelay = C.nextPlatformTick;
        rng = new Random();

        // Create array lists
        playerList = new ArrayList<>();
        platforms = new ArrayList<>();
        coins = new ArrayList<>();
        backgroundObjects = new ArrayList<>();
        fallingBarrelList = new ArrayList<>();

        // Initialize the classes that require it
        Debris.init();
        GameFont.init(this);
        GameObjectiveHandler.init(this);

        // Create two platforms
        platforms.add(new Platform(this));
        platforms.add(new Platform(this));

        // Create dead zone and game intro objects
        deadZone = new DeadZone(this);
        gameIntro = new GameIntro(this);
    }

    private void startGame() {
        playerList.clear();
        gameStatus = "live";

        // Pick a new objective for the current round
        GameObjectiveHandler.rollNewObjective();

        // Create Player 1 and bind their controls
        Player player = new Player(this, width / 2f - 20, 150, color(255, 0, 0));
        player.bindKeys('A', 'D', 'W', 'S', 'E', ' ');
        playerList.add(player);

        // Create Player 2 and bind their controls as well
        player = new Player(this, width / 2f + 20, 150, color(0, 0, 255));
        player.bindKeys((char) KeyEvent.VK_LEFT, (char) KeyEvent.VK_RIGHT, (char) KeyEvent.VK_UP, (char) KeyEvent.VK_DOWN, (char) KeyEvent.VK_CONTROL, (char) KeyEvent.VK_SHIFT);
        playerList.add(player);
    }

    // Similar to the end game method except does not remove the players
    private void winGame() {
        gameStatus = "won";
        gameIntro.chooseWinner(playerList);
        GameObjectiveHandler.endObjective();
    }

    // End the game by setting game status and clearing player list and objectives
    private void endGame() {
        gameStatus = "intro";
        playerList.clear();
        GameObjectiveHandler.endObjective();
    }

    private void update() {
        // Increase time of day
        timeOfDay += (day) ? C.timeSpeed : -C.timeSpeed;

        // If the time of day exceeds the end of day variable or the time of day is less than 0, flip it.
        if (timeOfDay > C.endOfDay) {
            day = false;
        } else if (timeOfDay < 0) {
            day = true;
        }

        // Update the current objective and dead zone
        GameObjectiveHandler.update();
        deadZone.update(playerList, gameStatus);

        // Decrement platform delay and add a new platform and reset delay if the delay reaches 0.
        nextPlatformDelay--;
        if (nextPlatformDelay == 0) {
            nextPlatformDelay = C.nextPlatformTick;
            platforms.add(new Platform(this));
        }

        // BACKGROUND OBJECTS UPDATE
        for (int i = backgroundObjects.size() - 1; i >= 0; i--) {
            BackgroundObject backgroundObject = backgroundObjects.get(i);
            backgroundObject.update();

            if (deadZone.yContactPoint() < backgroundObject.y) {
                Debris.add(this, backgroundObject.x, backgroundObject.y, color(255, 180), 10, backgroundObject.rectHeight / 2, "cube");
                backgroundObjects.remove(i);
            }
        }

        // Used to determine if the game has ended
        boolean allPlayersDied = true;

        for (Player player : playerList) {
            player.update(platforms, playerList);

            // If the grapple exists and the player is still alive, update its position and add a debris effect
            if (player.myGrapple != null && !player.isDead()) {
                Debris.add(this, player.x + player.myGrapple.getCurrX(), player.y + player.myGrapple.getCurrY(), color(54, 48, 44, 120), 3);

                player.myGrapple.update(platforms, player);
                // Remove the grapple if it is dead
                if (player.myGrapple.isDead()) {
                    player.myGrapple = null;
                }
            }

            // Revive the player if the game is over
            if (player.isDead()) {
                if (deadZone.resetting() || gameStatus.equals("won")) {
                    player.revive();
                }
            }

            if (!player.isDead()) {
                allPlayersDied = false;
            }
        }

        for (int i = platforms.size() - 1; i >= 0; i--) {
            Platform platform = platforms.get(i);
            platform.update();
            // Update the platform mob if it exists
            if (platform.getMob() != null) {
                platform.getMob().mobUpdate(playerList);
            }

            // Remove platform from the list and create a debris effect as it contacts the dead zone
            if (platform.y > height || platform.y > deadZone.yContactPoint()) {
                Debris.add(this, platform.x + platform.rectWidth / 2, platform.y, 170, (int) (platform.rectWidth / 20), platform.rectHeight, "cube");
                platforms.remove(i);
            }
        }

        for (int i = coins.size() - 1; i >= 0; i--) {
            Coin coin = coins.get(i);
            coin.pUpdate(playerList);

            // Remove coin from the list and create a debris effect as it contacts the dead zone
            if (deadZone.yContactPoint() < coin.y || coin.hit()) {
                Debris.add(this, coin.x, coin.y, color(255, 90, 8), 10);
                coins.remove(i);
            }
        }

        // Fetch debris list and then update them. Remove it from the list if it is dead
        ArrayList<Debris> debrisList = Debris.getDebrisList();
        for (int i = debrisList.size() - 1; i >= 0; i--) {
            Debris debris = debrisList.get(i);
            debris.update();

            if (debris.lifespan <= 0) {
                Debris.remove(i);
            }
        }

        for (int i = fallingBarrelList.size() - 1; i >= 0; i--) {
            FallingBarrel fallingBarrel = fallingBarrelList.get(i);
            fallingBarrel.pUpdate(playerList);

            // Consider contact with the deadzone as hitting
            if (fallingBarrel.y > deadZone.yContactPoint()) {
                fallingBarrel.setHitTrue();
                ;
            }

            // When the barrel hits an object, check if it has already exploded. If not, create a debris effect and call exploded thrust the players
            if (fallingBarrel.isHit()) {
                if (!fallingBarrel.hasExploded()) {
                    fallingBarrel.exploded(playerList);
                    Debris.add(this, fallingBarrel.x, fallingBarrel.y, color(235, 155, 52), 10, 15, "cube");
                }

                if (fallingBarrel.canDelete()) {
                    fallingBarrelList.remove(i);
                }
            }
        }

        // Try and add background objects, coins, or barrels
        tryAddRandomObjects();

        // End game if either all players died while the game status isn't on won
        if (allPlayersDied && !gameStatus.equals("won")) {
            endGame();
        }

        // Win the game if we meet the objective goal
        if (GameObjectiveHandler.objectiveComplete()) {
            winGame();
        }

        // Update the game intro screen if the game is not live
        if (!gameStatus.equals("live")) {
            gameIntro.update();
        }

    }

    public void draw() {
        update();
        // Progress background color from day to night and back
        background(lerpColor(color(99, 180, 207), color(16, 32, 69), timeOfDay / C.endOfDay));

        // Draw background 'background' objects
        for (BackgroundObject backgroundObject : backgroundObjects) {
            if (!backgroundObject.isFrontLayer()) {
                backgroundObject.display();
            }
        }

        // Draw player and Grapple Rope
        for (Player player : playerList) {
            // Draw grapple if it both exists and the player hasn't died
            if (player.myGrapple != null && !player.isDead()) {
                player.myGrapple.display(player.x, player.y);
            }

            player.display();

            // Draw points number on player
            noStroke();
            fill(255);
            textFont(GameFont.mainFont, 20);
            textAlign(CENTER, CENTER);
            text(player.getPoints(), player.x + player.rectWidth / 2, player.y + player.rectWidth / 2);
        }

        // Draw Platforms
        for (Platform platform : platforms) {
            platform.display();
            if (platform.getMob() != null) {
                platform.getMob().display();
            }
        }

        // Draw coins
        for (Coin coin : coins) {
            coin.display();
        }

        // Draw debris
        for (Debris debris : Debris.getDebrisList()) {
            debris.display();
        }

        // Draw falling barrels
        for (FallingBarrel fallingBarrel : fallingBarrelList) {
            fallingBarrel.display();
        }

        // Draw foreground 'background' objects
        for (BackgroundObject backgroundObject : backgroundObjects) {
            if (backgroundObject.isFrontLayer()) {
                backgroundObject.display();
            }
        }

        deadZone.display();
        GameObjectiveHandler.display();

        if (!gameStatus.equals("live")) {
            gameIntro.display();
        }
    }

    private void tryAddRandomObjects() {
        // Generate Coins
        if (rng.nextInt(C.coinDropChance[1]) < C.coinDropChance[0]) {
            coins.add(new Coin(this));
        }

        // Generate Falling Barrels
        if (rng.nextInt(C.fallingBarrelChance[1]) < C.fallingBarrelChance[0]) {
            fallingBarrelList.add(new FallingBarrel(this));
        }

        // Generate Background Objects
        if (rng.nextInt(C.backgroundObjectChance[1]) < C.backgroundObjectChance[0]) {
            backgroundObjects.add(new BackgroundObject(this));
        }
    }

    public void keyPressed() {
        // Restart game if the game has finished
        if (keyCode == 'R' && gameStatus.equals("won")) {
            gameIntro.reset();
            endGame();
        }

        // Intro game controls
        if (gameStatus.equals("intro")) {
            if (keyCode == ' ') {
                gameIntro.reset();
                startGame();
            }

            gameIntro.keyPressed();

        }

        // Call the player keyPressed method
        for (Player player : playerList) {
            player.keyPressed();
        }
    }

    public void keyReleased() {
        // Call the player keyPressed method
        for (Player player : playerList) {
            player.keyReleased();
        }
    }

    public static void main(String[] args) {
        String[] processingArgs = {""};
        GameRunner mySketch = new GameRunner();
        PApplet.runSketch(processingArgs, mySketch);
    }

}

