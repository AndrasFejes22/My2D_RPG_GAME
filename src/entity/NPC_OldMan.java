package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class NPC_OldMan extends Entity{

    public NPC_OldMan(GamePanel gp) {
        super(gp);

        //direction = "down";
        speed = 1;

        //ezt én tettem bele: Entity felülírása:
        solidArea.x = 0;
        solidArea.y = 16;
        //solidArea.width = 48;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        //idaig én tettem bele

        getImage();

        setDialogue();
    }

    public void getImage(){

        up1 = setup("/npc/oldman_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/oldman_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/oldman_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/oldman_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/npc/oldman_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/oldman_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/npc/oldman_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/oldman_right_2", gp.tileSize, gp.tileSize);
    }

    public void setDialogue(){
        dialogues[0] = "Hello lad.";
        //dialogues[1] = "So you've come to this island to \nfind the treasure?";
        dialogues[1] = "So you're here to save Middle Earth?";
        //dialogues[2] = "I used to be a great wizard but now... \nI'm a bit too old for taking an adventure.";
        dialogues[2] = "Yeah, something like that.";
        dialogues[3] = "Well, good luck on you.";
    }

    public void speak(){
        super.speak();
        onPath = true;
    }

    public void setAction(){ // kind of AI :)

        if(onPath){ // pathfinding

            // 1.: Goal: old man's home
            //int goalCol = 12; //old man's home
            //int goalRow = 9;

            //2.: Goal: player's position, so the Old man follows the player**:

            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;

            searchPath(goalCol, goalRow);

        } else { // not smart (circle) movement

            actionLockCounter++;

            if (actionLockCounter == 120) {
                Random random = new Random();
                int i = random.nextInt(100) + 1;

                if (i <= 25) {
                    direction = "up";
                }
                if (i > 25 && i <= 50) {
                    direction = "down";
                }
                if (i > 50 && i <= 75) {
                    direction = "left";
                }
                if (i > 75 && i <= 100) {
                    direction = "right";
                }

                actionLockCounter = 0;
            }
        }

    }


}
