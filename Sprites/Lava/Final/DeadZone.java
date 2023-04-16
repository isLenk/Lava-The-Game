package Woodrow.Final;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class DeadZone extends GameObject{
    int currentFrame = 0;
    PImage[] lavaTop;
    int nextFrameDelay = 20;

    public void update(ArrayList<Player> playerList, boolean canDamage) {
        rectWidth = sketch.width;
        rectHeight = sketch.height;
        super.update(false);
        nextFrameDelay--;

        if (nextFrameDelay == 0) {
            nextFrameDelay = 20;
            currentFrame = (currentFrame < 4) ? currentFrame + 1 : 0;
        }

        for (Player player : playerList) {
            if (player.y + player.rectHeight > y + 65 && !player.isDead()) {
                player.died();
            }
        }
    }

    public void display() {
        for(int px = 0; px <= sketch.width; px += 100){
            for (int py = (int)y; py <= sketch.height; py += 100){
                if (py == (int)y) {
                    sketch.image(lavaTop[currentFrame], px, py);
                }
                else{
                    sketch.noStroke();
                    sketch.fill(255, 90, 8);
                    sketch.rect(px, py, 100, 100);
                }
            }
        }
    }

    public DeadZone(PApplet sketch) {
        super(sketch);
        x = 0;
        rectWidth = sketch.width;
        rectHeight = sketch.height;
        y = sketch.height - 20;
        yVelocity = -C.objectYVelocity;

        lavaTop = new PImage[5];
        for (int i = 0; i < 5; i++) {
            lavaTop[i] = sketch.loadImage("C:\\Users\\noah.woodrow\\IdeaProjects\\CS30FallNoahWoodrow\\src\\Woodrow\\Final\\Sprites\\Lava\\lava_frame_" + i + ".png");
        }
    }


}
