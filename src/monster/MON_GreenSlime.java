package monster;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Rock;

import java.util.Random;

public class MON_GreenSlime extends Entity{
    //GamePanel gp;

    public MON_GreenSlime(GamePanel gp) {
        super(gp);

        //sets:
        name = "Green Slime";
        speed = 1;
        maxLife = 4;
        life = maxLife;
        type = type_monster;
        attack = 2;
        defense = 0;
        exp = 2; // How much XP you can get when you kill a monster
        projectile = new OBJ_Rock(gp);

        //solidArea:

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();

    }

    public void getImage(){
        up1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        up2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        down1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        left2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        right1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
    }

    public void setAction(){ // kind of AI :) //movement in circle

        if(onPath){ // pathfinding

            // 1.: Goal: old man's home
            //int goalCol = 12; //old man's home
            //int goalRow = 9;

            //2.: Goal: player's position, so the Old man follows the player**:

            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;

            searchPath(goalCol, goalRow);

            int i = new Random().nextInt(100) + 1;
            if(i > 99 && !projectile.alive && shotAvailableCounter == 30){
                projectile.set(worldX, worldY, direction , true, this);
                gp.projectileList.add(projectile);
                shotAvailableCounter = 0;
            }

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

        /*

        */

    }

    public void damageReaction(){
        actionLockCounter = 0;
        //direction = gp.player.direction; // when attacked moving away from the player
        onPath = true;// when attacked, start chasing the player
    }

    public void checkDrop(){
        int i = new Random().nextInt(100) + 1;

        // set the monster drop:
        if(i < 50){
            dropItem(new OBJ_Coin_Bronze(gp)); // itt visszaadjuk az Entity classnak
        }
        if(i >= 50 && i < 75){
            dropItem(new OBJ_Heart(gp));
        }
        if(i >= 75 && i < 100){
            dropItem(new OBJ_ManaCrystal(gp));
        }
    }

}
