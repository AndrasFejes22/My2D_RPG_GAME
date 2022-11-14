package main;

import java.awt.*;

public class EventHandler {

    GamePanel gp;
    //Rectangle eventRect;
    EventRect eventRect [][][];
    int previousEventX;
    int previousEventY;
    boolean canTouchEvent = true;

    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new EventRect[gp.maxMap][gp.maxWorldRow][gp.maxWorldCol];

        int col = 0;
        int row = 0;
        int map = 0;

        while (map< gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow){
            eventRect[map][col][row] = new EventRect();
            eventRect[map][col][row].x = 23;
            eventRect[map][col][row].y = 23;
            eventRect[map][col][row].width = 2;
            eventRect[map][col][row].height = 2;
            eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
            eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;

            col++;
            if(col == gp.maxWorldCol){
                col = 0;
                row++;
                if(row == gp.maxWorldRow){
                    row = 0;
                    map++;
                }
            }
        }


    }

    public void checkEvent(){

        // Check if te player is more than 1 tile away from the last event:

        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if(distance > gp.tileSize){
            canTouchEvent = true;
        }

        if (canTouchEvent){
            if(hit(0, 27, 17, "right")){
                System.out.println("Pit damage!");
                damagePit(gp.dialogueState);
            }

            if(hit(0, 23, 12, "up")){
                System.out.println("Healing!");
                healingPool(gp.dialogueState);
            }

            if(hit(0, 26, 20, "right")){
                System.out.println("Teleport!");
                teleport(gp.dialogueState);
            }
            // teleports to two maps
            if(hit(0, 10, 39, "any")){
                teleportToOtherMap(1, 12, 13);
            }
            if(hit(1, 12, 13, "any")){
                teleportToOtherMap(0, 10, 39);
            }

        }




    }

    private void teleportToOtherMap(int map, int col, int row) {

    }

    // EVENTS

    private void teleport(int gameState) {
        gp.gameState = gameState;
        gp.ui.currentDialogue = "Teleport!";
        gp.player.worldX = gp.tileSize * 37;
        gp.player.worldY = gp.tileSize * 10;
    }

    private void damagePit(int gameState) {
        gp.gameState = gameState;
        gp.playSoundEffect(6);
        gp.ui.currentDialogue = "You fall into a pit!";
        gp.player.life -= 1;
        //eventRect[col][row].eventDone = true;
        canTouchEvent = false;
    }

    private void healingPool(int gameState) {
        if(gp.keyH.enterPressed){
            gp.gameState = gameState;
            gp.player.attackCanceled = true;
            gp.playSoundEffect(2);
            gp.ui.currentDialogue = "You have drank water. \nYour life and mana have been recovered!";
            gp.player.life = gp.player.maxLife;
            gp.player.mana = gp.player.maxMana;
            // when you healed recall monsters:
            gp.aSetter.setMonster();;
        }

    }

    // check boolean

    public boolean hit (int map, int col, int row, String reqDirection){

        boolean hit = false;
        if(map == gp.currentMap) {
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
            eventRect[map][col][row].x = col * gp.tileSize + eventRect[map][col][row].x;
            eventRect[map][col][row].y = row * gp.tileSize + eventRect[map][col][row].y;

            if (gp.player.solidArea.intersects(eventRect[map][col][row]) && eventRect[map][col][row].eventDone == false) {
                if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                    hit = true;

                    previousEventX = gp.player.worldX;
                    previousEventY = gp.player.worldY;
                }
            }

            //reset:
            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
            eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
            eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;
        }

        return hit;
    }


}
