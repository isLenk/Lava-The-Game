package Woodrow.Final;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

import java.util.ArrayList;

public class GameRunner extends PApplet {

    private static ArrayList<Player> playerList;
    private static DeadZone deadZone;
    private static ArrayList<Platform> platforms;
    private int nextPlatformDelay;

    // Set window height
    public void settings(){
        size(1280, 720, P2D);
    }

    public void setup() {
        nextPlatformDelay = C.nextPlatformTick;
        playerList = new ArrayList<>();
        platforms = new ArrayList<>();
        playerList.add(new Player(this, width/2f, 150, color(255, 0, 0)));
        deadZone = new DeadZone(this);
        Platform spawnPlatform = new Platform(this);
        spawnPlatform.x = width * 0.5f - width * 0.25f;
        spawnPlatform.rectWidth = width * 0.5f;
        spawnPlatform.y = 300;

        platforms.add(spawnPlatform);
        platforms.add(new Platform(this));

    }

    public void draw() {
        background(188, 228, 245);
        deadZone.update(playerList, true);
        nextPlatformDelay--;
        if (nextPlatformDelay == 0) {
            nextPlatformDelay = C.nextPlatformTick;
            platforms.add(new Platform(this));
        }

        for (Player player : playerList) {
            player.update(platforms, "");
            player.display();
        }

        for (GameObject platform : platforms) {
            platform.update(false);
            platform.display();
        }

        deadZone.display();

    }

    public void keyPressed() {
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
