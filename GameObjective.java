package Talban.Final;

import processing.core.PApplet;

public class GameObjective {
    /*
        Game Objective was made with the intention of being flexible in terms of requirements.
        We wanted to be able to stack different challenges but due to it being CS finals tomorrow, we do not have
        the time to create that. Currently, Game Objective allows for time survival type challenges
     */

    private PApplet sketch;
    // The description of the objective. Appears for only five seconds
    private String description;

    // The required amount of time the player has to survive for
    private int surviveTime;

    // Determines whether the objective has been met
    private boolean completed;

    // How many seconds have elapsed since the start of this objective
    private long elapsedSeconds;

    // The time that the objective has started
    private long startTime;

    public GameObjective(PApplet sketch) {
        this.sketch = sketch;
        completed = false;
    }

    // Used to determine whether or not the game can set status to 'Won'
    public boolean completed() {
        return completed;
    }

    // Sets the required survival time
    public void setSurviveTime(int amount) {
        this.surviveTime = amount;
    }

    // Sets the description of the objective
    public void setDescription(String description) {
        this.description = description;
    }

    // Begins the objective, also can be called reset()
    public void start() {
        completed = false;
        startTime = System.currentTimeMillis();
    }

    public void update() {
        // Check if survive time is the objective
        if (surviveTime != 0) {
            // How much time has elapsed since the start of this objective
            long timeElapsed = System.currentTimeMillis() - startTime;
            elapsedSeconds = timeElapsed /1000;

            // Check if the elapsed seconds passes the survive time
            if (elapsedSeconds >= surviveTime) {
                completed = true;
            }
        }
    }

    public void display() {
        sketch.textAlign(sketch.CENTER, sketch.TOP);

        // Show game description for five seconds
        int descriptionAppearanceDuration = 5;

        // Show description of objective
        if (elapsedSeconds <= descriptionAppearanceDuration) {
            sketch.textFont(GameFont.objectiveDescriptionFont);
            sketch.fill(0);
            sketch.text(description, sketch.width/2f , 150);
            sketch.fill(255);
            sketch.text(description, sketch.width/2f- 2, 148);
        }

        // Show the countdown
        if (surviveTime != 0 && elapsedSeconds < surviveTime) {
            // The current amount of time the player has survived
            int currentTime = (int) (surviveTime - elapsedSeconds);
            sketch.textFont(GameFont.mainFont);
            sketch.fill(0);
            sketch.text( currentTime +"s", sketch.width / 2f, 50);
            sketch.fill(255);
            sketch.text( currentTime +"s", sketch.width / 2f - 2, 48);
        }
    }
}
