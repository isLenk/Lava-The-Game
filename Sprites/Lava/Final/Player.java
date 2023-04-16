package Woodrow.Final;

import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;

public class Player extends GameObject{
    int jumpTick;
    // Counter that accelerates down fall
    protected int fallTick;
    private boolean dead;
    private int jumpsAllowed;
    private boolean cursorNear;

    public void died() {
        dead = true;
        this.yVelocity = 0.2f;
    }

    public boolean isDead() {
        return dead;
    }

    public void update(ArrayList<Platform> walls, String placeholder) {
        //System.out.println(getDistanceFromCoordinate(sketch.mouseX, sketch.mouseY));
        if (getDistanceFromCoordinate(sketch.mouseX, sketch.mouseY) < C.cursorPlayerActivationDistance) {
            cursorNear = true;
        } else {
            cursorNear = false;
        }
        // Set the lastX and lastY to the current x and y
        lastY = y;
        lastX = x;
        // Ground Collision, set walking to true if they are still on the ground
        y += yVelocity;
        jumpTick -= (jumpTick < -2) ? 0 : 1;

        if (!dead) {
        // JUMPING: If the jump tick is still active, decrease the player Y coordinate
        if (jumpTick > 0) {
            fallTick = 0;
            // A quick formula that uses initial velocity, gravitational acceleration and jumpTick to emulate a non-linear jump
            y -= C.initialFallVelocity + C.gravityConstant * C.gravityAcceleration * jumpTick;
        }
        // There is a 2 tick delay before the player begins their descent
        else if (jumpTick < -2) {
            fallTick += (fallTick > C.terminalVelocityTick) ? 0 : 1;
            // A formula similar to the previous one which has a smaller constant. There is no terminal velocity.
            y += C.initialFallVelocity + 0.5 * C.gravityAcceleration * fallTick;
        }
        }

        for (GameObject wall : walls) {
            if (this.intersect(wall)) {
                collidesWallY(wall);
                jumpsAllowed = C.maxJumpsAllowed;
            }
        }

        x += xVelocity;


        // Ground collision, set walking to true if they are still on the ground
        for (GameObject wall : walls) {
            if (this.intersect(wall)) {
                collidesWallX(wall);
            }
        }

    }

    public void collidesWallY(GameObject wall) {
        // Get direction the player is travelling in
        boolean movingDown = this.y - this.lastY > 0;

        if (movingDown) {
            y = wall.top() - rectHeight;
        }
        else {
            y = wall.bottom() + wall.yVelocity;
        }
    }

    public void display()
    {

        if (cursorNear) {
            sketch.fill(140, 0.5f);
            sketch.stroke(0);
            sketch.circle(x + rectWidth/2, y + rectHeight/2, C.cursorPlayerActivationDistance * 2);
        }

        sketch.fill(backgroundColour);
        sketch.rect(x, y, rectWidth, rectHeight);

        if (y + rectHeight < 0) {
            sketch.rect(x, 0, 20, 30);
        }

    }

    public Player(PApplet sketch, float x, float y, int colour) {
        super(sketch);
        this.x = x;
        this.y = y;
        this.rectWidth = 50;
        this.rectHeight = 80;
        this.yVelocity = 0;
        this.backgroundColour = colour;
        this.borderColour = colour;
        this.jumpsAllowed = 0;
    }

    public void jump() {
        if (jumpsAllowed == 0) {return;}
        jumpsAllowed --;
        jumpTick = C.maxJumpTick;

    }

    public void keyPressed() {
        if (dead) {return;}
        if (sketch.keyCode == 'D') {
            xVelocity = C.playerXVelocity;
        }

        if (sketch.keyCode == 'A') {
            xVelocity = -C.playerXVelocity;
        }


        if (sketch.keyCode == ' ') {
            jump();
        }
    }

    public void keyReleased() {
        if (dead) {return;}
        if (sketch.keyCode == 'D' && xVelocity == C.playerXVelocity) {
            xVelocity = 0;
        }

        if (sketch.keyCode == 'A' && xVelocity == -C.playerXVelocity) {
            xVelocity = 0;
        }

    }

    public Player (PApplet sketch) {
        super(sketch);
        dead = false;
    }

}
