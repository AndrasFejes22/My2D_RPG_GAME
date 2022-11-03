package main;

import java.awt.*;

public class EventHandler {

    GamePanel gp;
    Rectangle eventRect;
    int eventRectDefaultX;
    int eventRectDefaultY;

    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new Rectangle();
        eventRect.x = 23;
        eventRect.y = 23;
        eventRect.width = 2;
        eventRect.height = 2;
        eventRectDefaultX = eventRect.x;
        eventRectDefaultY = eventRect.y;
    }

    public void checkEvent(){

        if(hit(27,17, "right") == true){
            System.out.println("Pit damage!");
            damagePit(gp.dialogueState);
        }

        if(hit(23,12, "up") == true){
            System.out.println("Healing!");
            healingPool(gp.dialogueState);
        }

        if(hit(26,20, "right") == true){
            System.out.println("Teleport!");
            teleport(gp.dialogueState);
        }



    }

    private void teleport(int gameState) {
        gp.gameState = gameState;
        gp.ui.currentDialogue = "Teleport!";
        gp.player.worldX = gp.tileSize * 37;
        gp.player.worldY = gp.tileSize * 10;
    }

    private void damagePit(int gameState) {
        gp.gameState = gameState;
        gp.ui.currentDialogue = "You fall into a pit!";
        gp.player.life -= 1;
    }

    private void healingPool(int gameState) {
        if(gp.keyH.enterPressed == true){
            gp.gameState = gameState;
            gp.ui.currentDialogue = "You have drank water. \nYour life has been recovered!";
            gp.player.life = gp.player.maxLife;
        }

    }

    public boolean hit (int eventCol, int eventRow, String reqDirection){

        boolean hit = false;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRect.x = eventCol * gp.tileSize + eventRect.x;
        eventRect.y = eventRow * gp.tileSize + eventRect.y;

        if(gp.player.solidArea.intersects(eventRect)){
            if(gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")){
                hit = true;
            }
        }

        //reset:
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect.x = eventRectDefaultX;
        eventRect.y = eventRectDefaultY;


        return hit;
    }


}
