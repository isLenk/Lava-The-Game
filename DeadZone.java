package Talban.Final;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class DeadZone extends GameObject {
    // Current animation frame
    private int currentFrame;

    // All the images of the lava
    private PImage[] lavaTop;

    // A tick counter that prevents the player
    private int nextFrameDelay;

    // A boolean that enables when the lava is lowering itself back to the bottom of the screen
    private boolean resetting;

    // Returns the resetting boolean value
    public boolean resetting() {
        return resetting;
    }

    // Returns the position in which the object can be considered "in contact with lava"
    public float yContactPoint() {
        return y + 50;
    }

    // Raise (or lower) lava depending on reset boolean and game status. Adjust animation sequence and kill players in contact.
    public void update(ArrayList<Player> playerList, String gameStatus) {
        // Adjust the dimensions in case the screen gets resized
        rectWidth = sketch.width;
        rectHeight = sketch.height;

        // Raise lava if the game is currently live
        if (gameStatus.equals("live")) {
            super.update(false);
        }
        // Lower the lava down to the rest position if the game is not live
        else {
            y = sketch.height - 150;
        }

        // Decrement the delay counter and reset it while incrementing the current frame if the delay reaches 0
        nextFrameDelay--;

        if (nextFrameDelay == 0) {
            nextFrameDelay = C.deadZoneAnimationTick;
            // Increment the current frame but return to 0 if the current frame is past the amount of animations
            currentFrame = (currentFrame < 4) ? currentFrame + 1 : 0;
        }

        // When the y position is higher or equal to the allowed safe zone, begin resetting down
        if (y <= C.deadZoneSafeArea) {
            resetting = true;
            yVelocity = 5;
        }

        // If the dead zone is resetting, continue lowering until the y position is greater than the screen height
        if (resetting && y > sketch.height) {
            resetting = false;
            yVelocity = -C.objectYVelocity / 2;
        }

        // Kill the player if they are in contact with the dead zone
        for (Player player : playerList) {
            if (player.bottom() > yContactPoint() && !player.isDead()) {
                player.died();
            }
        }
    }

    // Draw the top of the lava using images, then fill the bottom parts with orange-red
    public void display() {
        // A for loop that stretches across the screen width by 100x100 grid
        for(int px = 0; px <= sketch.width; px += 100){
            sketch.image(lavaTop[currentFrame], px, y);
        }

        // Anything below the top layer is just red
        sketch.noStroke();
        sketch.fill(255, 90, 8);
        sketch.rect(0, y + 100, sketch.width, sketch.height - y);
    }

    public DeadZone(PApplet sketch) {
        super(sketch);
        // Place 20 pixels from the bottom of the screen and set it to be the length of the screen
        x = 0;
        rectWidth = sketch.width;
        rectHeight = sketch.height;
        y = sketch.height - 20;
        yVelocity = -C.objectYVelocity/2;
        nextFrameDelay = C.deadZoneAnimationTick;

        // Load lava frames in
        lavaTop = new PImage[5];
        for (int i = 0; i < 5; i++) {
            lavaTop[i] = JImage.getImage(sketch, "/Talban/Final/Sprites/Lava/lava_frame_" + i + ".png");
        }
    }
}
