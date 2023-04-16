package Talban.Final;

import processing.core.PApplet;

import java.util.ArrayList;

public class GameObject {

    protected static PApplet sketch;

    // The coordinates of the game object
    protected float x, y;

    // The last known coordinates of the game object before it was updated
    protected float lastX, lastY;

    // The dimensions of the game object's collision box
    protected float rectWidth, rectHeight;

    // The velocities of the game object
    protected float xVelocity, yVelocity;

    // The game object colours
    protected int backgroundColour, borderColour;


    // Set default variable values
    public GameObject(PApplet sketch) {
        this.sketch = sketch;
        x = 0;
        y = 0;
        lastX = 0;
        lastY = 0;
        rectWidth = 50;
        rectHeight = 50;
        xVelocity = 0;
        yVelocity = 0;
        backgroundColour = 255;
        borderColour = 0;
    }

    // Overload with configured parameters
    public GameObject(PApplet sketch, float x, float y, float rectWidth, float rectHeight, float xVelocity, float yVelocity, int backgroundColor, int borderColour) {
        this(sketch);
        this.x = x;
        this.y = y;
        this.rectWidth = rectWidth;
        this.rectHeight = rectHeight;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.backgroundColour = backgroundColor;
        // If the borderColour was set to -1, then the borderColour is the background colour
        this.borderColour = (borderColour != -1) ? borderColour : backgroundColour;
    }

    public float getDistanceFromTarget(GameObject target) {
        float xDist = Math.abs(target.x - x);
        float yDist = Math.abs(target.y - y);
        return (float)Math.sqrt(xDist * xDist + yDist * yDist);
    }

    public float getDistanceFromCoordinate(float x,  float y) {
        sketch.fill(0);
        float xDist = Math.abs(x - (this.x + rectWidth/2) );
        //sketch.text("X Distance: " + xDist, 50, 50);
        float yDist = Math.abs(y - (this.y + rectHeight/2));
        //sketch.text("Y Distance: " + yDist, 50, 70);
        float distance = (float)Math.sqrt(xDist * xDist + yDist * yDist);
        //sketch.text("Distance: " + distance, 50, 90);
        return distance;
    }
    // Object applies gravity acceleration
    public void update(ArrayList<GameObject> walls) {
        // Set the lastX and lastY to the current x and y
        lastY = y;
        lastX = x;

        // Increase y coordinate by both its velocity and the gravity acceleration
        y += yVelocity + C.gravityAcceleration;

        // Check collision on walls
        for (GameObject wall : walls) {
            if (this.intersect(wall)) {
                collidesWallY(wall);
            }
        }

        // Increase x coordinate by its velocity
        x += xVelocity;

        // Check collision on walls
        for (GameObject wall : walls) {
            if (this.intersect(wall)) {
                collidesWallX(wall);
            }
        }

    }

    // Collision Free. Gravity parameter: If set to true, apply gravity acceleration
    public void update(boolean gravity) {
        lastY = y;
        lastX = x;

        y += yVelocity;
        if (gravity) // Apply gravity if the parameter allows
            y += C.gravityAcceleration;
        x += xVelocity;
    }

    // Check if object collides with game object on X
    public void collidesWallX(GameObject wall) {
        // Get direction the player is travelling in
        boolean movingRight = this.x - this.lastX > 0;

        if (movingRight) {
            x = wall.left() - rectWidth;
        }
        else {
            x = wall.right();
        }
    }

    // Check if object collides with game object on Y
    public void collidesWallY(GameObject wall) {
        // Get direction the player is travelling in
        boolean movingDown = this.y - this.lastY > 0;

        if (movingDown) {
            y = wall.top() - rectHeight;
        }
        else {
            y = wall.bottom();
        }
    }

    public float top() {
        return y;
    }

    // Get bottom side of the game object
    public float bottom() {
        return y + rectHeight;
    }

    // Get the left side of the game object
    public float left() {
        return x;
    }

    // Get the right side of the game object
    public float right() {
        return x + rectWidth;
    }

    // Check if the game object intersects with the target object
    public boolean intersect(GameObject hit) {
        if (!(this.equals(hit)) &&
                bottom() > hit.top() &&
                top() < hit.bottom() &&
                right() > hit.left() &&
                left() < hit.right()) {
            return true;
        }
        return false;
    }

    // Default draw game object
    public void display() {
        sketch.fill(backgroundColour);
        sketch.stroke(borderColour);
        sketch.rect(x, y, rectWidth, rectHeight);
    }

}
