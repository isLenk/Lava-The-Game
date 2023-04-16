package Talban.Final;

import processing.core.PApplet;

import java.util.ArrayList;

public class Grapple{
    PApplet sketch;

    // The target coordinates (Assigned to mouse)
    private float endX, endY;

    // How fast the grapple will thrust the player to said direction
    private float thrust;

    // The current dimensions of the grapple relative to the player. H being hypotenuse
    private float currX, currY, currH;

    // How long the grapple can live for before shrinking
    private long lifeSpan;

    // Grapple head is used to check collisions with the platforms
    private GrappleHead head;

    // Assigned to whatever object the grapple hits
    private GameObject target;

    // The offset from the top left of the grapple when the grapple hits it
    private float targetXOffset, targetYOffset;

    // Used for pulling the player closer to where the grapple hit
    private float percentageFromGoal = 0;

    // Used to determine whether or not to remove the grapple
    private boolean dead;

    public boolean isDead() {
        return dead;
    }

    public float getCurrX() {
        return currX;
    }

    public float getCurrY() {
        return currY;
    }

    // Assign the target coordinates of the grapple and set a lifespan
    public Grapple(PApplet sketch, float endX, float endY) {
        this.sketch = sketch;
        this.endX = endX;
        this.endY = endY;
        this.thrust = 25;
        currX = 0;
        currY = 0;
        currH = 0;
        lifeSpan = (long)(System.currentTimeMillis()/1000 + 1.25);
        head = new GrappleHead(sketch);
    }

    public void update(ArrayList<Platform> platforms, Player player) {
        // If there is a target available, pull player towards it
        if (target != null ) {
            percentageFromGoal += 0.1;
            // Interpolation Math: t ( y2 - y1 ) + y1;
            player.x = percentageFromGoal * (target.x  + targetXOffset - player.x) + player.x;
            player.y = percentageFromGoal * (target.y - player.rectHeight  - targetYOffset - player.y) + player.y;

            // Consider grapple as dead if it passes the goal percentage
            if (percentageFromGoal >= 1) {
                dead = true;
            }
            return;
        }

        // Checks if the current time passed the lifespan. This is buggy and at first we thought we should fix it
        // But the results of dying because of a shortened lifespan was hilarious
        if (System.currentTimeMillis()/1000 >= lifeSpan) {
            lifeSpan = 0;
        }

        float playerX = player.x;
        float playerY = player.y;

        // Calculate the triangle dimensions from the player to the end point coordinates
        float xDistFromEnd = endX - playerX;
        float yDistFromEnd = endY - playerY;
        float hDistFromEnd = (float)Math.sqrt((xDistFromEnd * xDistFromEnd) + (yDistFromEnd * yDistFromEnd));

        // Modify the curr' variables by adding thrust to the currH and then recalculating the other legs
        currH = currH + thrust;
        currX = (currH / hDistFromEnd) * xDistFromEnd;
        currY = (currH / hDistFromEnd) * yDistFromEnd;

        // Adjust the head object with the new coordinates
        head.update(playerX + currX, playerY + currY);

        // Check if any platform intersected with the head object, set the target data to platform
        for (Platform platform : platforms) {
            if (platform.intersect(head)) {
                target = platform;
                targetXOffset = head.x - target.x;
                targetYOffset = head.y - target.y;
            }
        }
    }

    public void display(float playerX, float playerY) {
        sketch.stroke(54, 21, 0);
        // Draw line using curr' variable data if there is not yet a target
        if (target == null) {
            sketch.line(playerX, playerY, playerX + currX, playerY + currY);
        }
        // Draw line using target related data if there is a hit
        else {
            sketch.line(playerX, playerY, target.x + targetXOffset, target.y + targetYOffset);
        }
    }

}
