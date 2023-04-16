package Talban.Final;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

import java.util.ArrayList;

public class GameIntro {
    private PApplet sketch;

    // Variables to hold all the images for the intro screen
    private PImage gameTitle;
    private PImage gameBeginPrompt;
    private PImage controlButtonPrompt;

    // Variables that make the title go up and down
    private float yMaxOffset;
    private float currentYOffset;

    // Determines whether to display the controls or intro screen
    private boolean toggleControls;

    // String list storing the winners name and color
    private static String[] roundWinner;

    public GameIntro(PApplet sketch){
        this.sketch = sketch;

        // Load intro screen images
        gameTitle = JImage.getImage(sketch,"/Talban/Final/Sprites/Game_Intro/lavaTitle.png");
        gameBeginPrompt = JImage.getImage(sketch, "/Talban/Final/Sprites/Game_Intro/gameBeginPrompt.png");
        controlButtonPrompt = JImage.getImage(sketch,"/Talban/Final/Sprites/Game_Intro/controlPrompt.png");

        yMaxOffset = 10;
        currentYOffset = 0;
        toggleControls = false;
        roundWinner = null;
    }

    public void keyPressed() {
        // Toggle controls variable
        if (sketch.keyCode == 'C' && roundWinner == null) {
            toggleControls = !toggleControls;
        }
    }

    // Resets all the required variables that default the screen
    public void reset() {
        roundWinner = null;
        toggleControls = false;
    }

    // Adjust the y offset variable
    public void update() {
        // Increase or decrease the y offset depending on the yMaxOffset variable
        currentYOffset = (yMaxOffset > 0) ? currentYOffset + 1 : currentYOffset -1;

        // Flip the yMaxOffset from negative to postive depending on if they reached it
        if (currentYOffset == yMaxOffset) {
            yMaxOffset = -yMaxOffset;
        }
    }

    public void chooseWinner(ArrayList<Player> playerList) {
        // Pick the top player
        Player winningPlayer = playerList.get(0);

        // Loop through each player and see if they have more points than the current winning player
        for (Player player : playerList) {
            if (player != winningPlayer && player.getPoints() > winningPlayer.getPoints()) {
                winningPlayer = player;
            }
        }

        // Store the winning players data
        roundWinner = new String[] {
                String.format("Player %s", playerList.indexOf(winningPlayer) + 1),
                String.valueOf(winningPlayer.backgroundColour)
        };
    }

    public void display(){
        float center = sketch.width/2f;
        // Display the winner screen
        if (roundWinner != null) {
            sketch.textFont(GameFont.mainFont);
            sketch.textAlign(sketch.CENTER, sketch.CENTER);
            sketch.fill(0);
            sketch.text(roundWinner[0] + " Wins!", center + 2, 52);
            sketch.fill(255);
            sketch.text(roundWinner[0]  + " Wins!", center, 50);
            sketch.fill(Integer.parseInt(roundWinner[1]));
            sketch.text(roundWinner[0]  + " Wins!", center - 2, 48);

            sketch.textFont(GameFont.mainFont, 20);
            sketch.fill(0);
            sketch.text("Press R to Continue...", center, sketch.height - 150);
            sketch.fill(255);
            sketch.text("Press R to Continue...", center-2, sketch.height - 148);
        }
        else {

            // Display the intro screen
            if (!toggleControls) {
                sketch.image(gameTitle, center - 640, sketch.height * (1 / 6f) + currentYOffset);
                sketch.image(gameBeginPrompt, center - 640, sketch.height * (1 / 6f) + 250 + currentYOffset);
                sketch.image(controlButtonPrompt, center- 640, sketch.height - 100 + currentYOffset / 2);

            // Display the controls screen
            } else {
                sketch.textAlign(sketch.CENTER, sketch.TOP);
                sketch.textFont(GameFont.mainFont);
                drawBorderedText("CONTROLS", center, 50 + currentYOffset);
                sketch.textFont(GameFont.objectiveDescriptionFont);

                drawBorderedText("Player 1", center, 250);
                drawBorderedText("wasd for movement - spacebar for jump - e for grapple", center, 290);

                drawBorderedText("Player 2", center, 350);
                drawBorderedText("arrow's for movement - shift for jump - ctrl for grapple", center, 390);

            }
        }
    }

    // Writes a text with a shadow on it
    private void drawBorderedText(String text, float x, float y) {
        sketch.fill(0);
        sketch.text(text, x-2f, y-2f);

        sketch.fill(255);
        sketch.text(text, x, y);
    }
}
