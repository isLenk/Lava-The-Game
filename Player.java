package Talban.Final;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Player extends GameObject{

    // The current jump tick, holds C.maxJumpTick
    private int jumpTick;

    // Stores the keybinds that controls the player movement
    private HashMap<String, Character> keyBinds;

    // Determines the speed of descent, capped at C.terminalVelocityTick
    private int fallTick;

    // Determines whether the player should accept movement
    private boolean dead;

    // How many jumps the player is currently able to perform. Resets when grounded
    private int jumpsAllowed;

    // True when the player is on a platform. Used for resetting jump and fall
    private boolean grounded;

    // Determines whether the player is crouching.
    private boolean crouching;

    // How many coins/points the player earned
    private int pointsScored;

    // Additional x-velocity which was given from other objects.
    private float hitXVelocity;

    // Determines whether to slide the player horizontally
    private boolean noHorizontalMovement = false;

    // Determines whether the player has spawned into the map properly. If not, spawn them on top of a safe platform.
    private boolean spawned;

    // Grapple object that gives the player a more versatility. Requires mouse.
    Grapple myGrapple;

    // Increments x Velocity by given amount
    public void addHitXVelocity(float amount) {
        hitXVelocity += amount;
    }

    // Assigns key characters to hashmap
    public void bindKeys(char left, char right, char up, char down, char grapple, char jump) {
        keyBinds.put("left", left);
        keyBinds.put("right", right);
        keyBinds.put("up", up);
        keyBinds.put("down", down);
        keyBinds.put("grapple", grapple);
        keyBinds.put("jump", jump);
    }

    public void addPoint() {
        pointsScored ++;
    }

    public int getPoints() {
        return pointsScored;
    }

    public boolean isDead() {
        return dead;
    }

    // Stops player movement and add death debris. Toggles dead boolean
    public void died() {
        dead = true;
        xVelocity = 0;
        yVelocity = 0.2f;
        Debris.add(sketch, x, y, sketch.color(255, 90, 8), 9,   10, "cube");
    }

    // Sets the players coordinates onto a platform that is relatively safe to spawn in.
    private void spawn(ArrayList<Platform> platformList) {
        Platform spawnPlatform = null;
        boolean platformFound = false;

        // Loop until suitable conditions for a platform are met
        while (!platformFound) {
            spawnPlatform = platformList.get( new Random().nextInt(platformList.size() - 1));

            // Checks if the platform is at least 250 pixels from the bottom
            if (spawnPlatform.bottom() < sketch.height - 250) {
                platformFound = true;
            }
        }

        y = spawnPlatform.y - rectHeight - 5;
        // Randomly position the player somewhere on the platform
        x = spawnPlatform.x + ( new Random().nextFloat() * (spawnPlatform.rectWidth - rectWidth));
        spawned = true;
    }

    public void update(ArrayList<Platform> platformList, ArrayList<Player> playerList) {
        // Decrease hit velocity if the hit velocity isn't 0
        if (hitXVelocity != 0) {
            hitXVelocity = (hitXVelocity > 0) ? hitXVelocity - 0.15f : hitXVelocity + 0.15f;

            // Round the hit velocity to 0 if it's within 0.3
            if (Math.abs(hitXVelocity) < 0.3) {
                hitXVelocity = 0;
            }

        }

        // Spawn the player if they have not yet been spawned in
        if (!spawned) {
            spawn(platformList);
        }

        // Set the lastX and lastY to the current x and y
        lastY = y;
        lastX = x;

        // Ground Collision, set walking to true if they are still on the ground
        y += yVelocity;
        jumpTick -= (jumpTick < -2) ? 0 : 1;

        // JUMPING: If the jump tick is still active, decrease the player Y coordinate
        if (jumpTick > 0) {
            fallTick = 0;
            // A quick formula that uses initial velocity, gravitational acceleration and jumpTick to emulate a non-linear jump
            y -= C.initialFallVelocity + C.gravityConstant * C.gravityAcceleration * jumpTick;
        }
        // There is a 2 tick delay before the player begins their descent
        else if (jumpTick < -2) {
            fallTick = (grounded) ? 0 : fallTick + 1;
            // Peak the fall tick to whatever the limits are set to
            if (fallTick > C.terminalVelocityTick) {
                fallTick = C.terminalVelocityTick;
            }

            // A formula similar to the previous one which has a smaller constant. There is no terminal velocity.
            y += C.initialFallVelocity + 0.5 * C.gravityAcceleration * fallTick;
        }

        // Used to set grounded to false if they are not in contact with any platform
        boolean contactWithAnyPlatform = false;

        for (Platform platform : platformList) {
            // Collide with floor if they are not crouching
            if (this.intersect(platform) && !crouching) {
                collidesWallY(platform);
                // Follow platform direction
                x += platform.xVelocity;
                // Since they had contact with the platform, that means they are grounded.
                contactWithAnyPlatform = true;
            }
        }

        if (!contactWithAnyPlatform) {
            grounded = false;
        }

        // Check collision with player on y - axis
        for (Player player : playerList) {
            if (this.intersect(player)) {
                collidesPlayerY(player);
            }
        }

        x += xVelocity + hitXVelocity;

        // Check collision with player on x - axis
        for (Player player : playerList) {
            if (this.intersect(player)) {
                collidesPlayerX(player);
            }
        }

        // A little bit of slide when you move sideways
        if (noHorizontalMovement && xVelocity != 0) {
            if (xVelocity < 0) {
                xVelocity += (crouching) ? 0.3 : 1;
            }
            else {
                xVelocity -= (crouching) ? 0.3 : 1;
            }

            // Round xVelocity to 0 if they are in 0.5 range
            if (Math.abs(xVelocity) < 0.5) {
                xVelocity = 0;
            }
        }

        // Make player appear on right side of screen if they pass left
        if (left() > sketch.width) {
            x = 0 - rectWidth;
        }
        // Make player appear on left side of screen if they pass right
        else if (right() < 0) {
            x = sketch.width;
        }
    }

    // Reset self jump tick and remove the other players jump tick. Reposition self on top of the other players head
    private void collidesPlayerY(Player player) {
        // Get direction the player is travelling in
        boolean movingDown = this.y - this.lastY > 0;
        if (movingDown && Math.abs(bottom() - player.top()) < 5) {
            jumpTick = C.maxJumpTick;
            player.jumpTick = 0;
            y = player.top() - rectHeight - 10;
        }
    }

    private void collidesPlayerX(Player player) {
        // Push player in direction of self
        player.hitXVelocity = xVelocity;
    }

    // Reset jumps allowed and ground player if on top of platform
    public void collidesWallY(GameObject wall) {
        if (lastY + rectHeight < wall.top()){
            y = wall.top() - rectHeight;
            grounded = true;
            jumpsAllowed = C.maxJumpsAllowed;
        }
    }

    public void display() {
        sketch.textAlign(sketch.LEFT, sketch.CENTER);

        // Do not display if the player is dead and out of screen
        if (isDead() && y > sketch.height) {
            return;
        }

        sketch.fill(backgroundColour);
        sketch.rect(x, y, rectWidth, rectHeight);

        // Draw arrow when player is out of screen
        if (y + rectHeight < 0) {
            sketch.triangle(x - 8, 15, x, 0, x + 8, 15);
            sketch.textAlign(sketch.CENTER, sketch.TOP);
            // Adjust the height of the text depending on how far away self is from the screen 0
            int distance = Math.round((Math.abs(y) - Math.abs(y % 10) )/ 100);
            float textHeight = 10 + (20f * ((Math.abs(y)- Math.abs(y % 10)) /500f)  );
            sketch.fill(0);
            sketch.textFont(GameFont.mainFont,textHeight);
            sketch.text(distance + "m", x, 17);
            sketch.fill(255);
            sketch.textFont(GameFont.mainFont, textHeight);
            sketch.text(distance + "m", x + 2, 19);
        }
    }

    // 'Resets' the player life info
    public void revive() {
        dead = false;
        spawned = false;
        hitXVelocity = 0;
    }

    // Add x to jumps allowed
    public void giveJump(int amount) {
        jumpsAllowed += amount;
    }

    // Accelerate the player upwards by jumpTickAmount
    public void forceJump(int jumpTickAmount) {
        jumpTick += jumpTickAmount;
        grounded = false;
        Debris.add(sketch, x, bottom(), 120, 3, 7, "cube");
    }

    // Force the player to fall downwards by given amount. Removes any jump tick it has.
    public void forceFall(int fallTickAmount) {
        fallTick += fallTickAmount;
        jumpTick = -2;
        grounded = false;
        Debris.add(sketch, x, bottom(), 120, 3, 7, "cube");
    }

    // Resets jump tick if jumps allowed doesn't equal 0. also has fun debris effect
    public void jump() {
        if (jumpsAllowed == 0) {return;}
        jumpsAllowed --;
        jumpTick = C.maxJumpTick;
        grounded = false;
        Debris.add(sketch, x, bottom(), 120, 3, 7, "cube");
    }

    // Character Movement
    public void keyPressed() {
        if (dead) {return;}

        if (sketch.keyCode == keyBinds.get("right")) {
            xVelocity = C.playerXVelocity;
            noHorizontalMovement = false;
            hitXVelocity = 0;
        }

        if (sketch.keyCode == keyBinds.get("left")) {
            xVelocity = -C.playerXVelocity;
            noHorizontalMovement = false;
            hitXVelocity = 0;
        }

        if (sketch.keyCode == keyBinds.get("down")) {
            crouching = true;
            rectHeight = 40;
            y+=10;
        }

        if (sketch.keyCode == keyBinds.get("jump")) {
            jump();
        }

        // Create a grapple object targetting players mouse coordinates
        if (sketch.keyCode == keyBinds.get("grapple")) {
            myGrapple = new Grapple(sketch, sketch.mouseX, sketch.mouseY);
        }
    }

    // Resets character movement
    public void keyReleased() {
        if (dead) {return;}
        if (sketch.keyCode == keyBinds.get("right") && xVelocity == C.playerXVelocity) {
            noHorizontalMovement = true;
        }

        if (sketch.keyCode == keyBinds.get("left") && xVelocity == -C.playerXVelocity) {
            noHorizontalMovement = true;
        }

        if (sketch.keyCode == keyBinds.get("down")) {
            crouching = false;
            rectHeight = 50;
            y -= 10;
        }
    }

    public Player(PApplet sketch, float x, float y, int colour) {
        super(sketch);
        keyBinds = new HashMap<>();
        this.x = x;
        this.y = y;
        this.rectWidth = 30;
        this.rectHeight = 50;
        this.yVelocity = 0;
        this.backgroundColour = colour;
        this.borderColour = colour;
        this.jumpsAllowed = 0;
        spawned = false;
        crouching = false;
    }

}
