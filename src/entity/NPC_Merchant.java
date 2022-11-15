package entity;

import main.GamePanel;
import object.*;

import java.util.Random;

public class NPC_Merchant extends Entity{

    public NPC_Merchant(GamePanel gp) {
        super(gp);

        //direction = "down";
        speed = 1;

        //ezt én tettem bele: Entity felülírása:
        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        //idaig én tettem bele

        getImage();
        setDialogue();
        setItems();
    }

    public void getImage(){

        up1 = setup("/npc/merchant_down_1", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/merchant_down_2", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/merchant_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/merchant_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/npc/merchant_down_1", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/merchant_down_2", gp.tileSize, gp.tileSize);
        right1 = setup("/npc/merchant_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/merchant_down_2", gp.tileSize, gp.tileSize);
    }

    public void setDialogue(){
        dialogues[0] = "So, you found me. \nI have some good stuff for a brave adventurer. \nDo you want to trade?";
        /*
        //dialogues[1] = "So you've come to this island to \nfind the treasure?";
        dialogues[1] = "So you're here to save Middle Earth?";
        //dialogues[2] = "I used to be a great wizard but now... \nI'm a bit too old for taking an adventure.";
        dialogues[2] = "Yeah, something like that.";
        dialogues[3] = "Well, good luck on you.";
        */
    }

    public void setItems(){
        // add items, de túllépheti a maxSize-ot!
        //inventory.clear(); // delete extra items
        //inventory.add(currentWeapon);
        //inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Axe(gp));
        inventory.add(new OBJ_Sword_Normal(gp));
        inventory.add(new OBJ_Shield_Blue(gp));
        inventory.add(new OBJ_Shield_Wood(gp));
        inventory.add(new OBJ_Potion_Red(gp));

    }

    /*
    public void speak(){
        super.speak();
    }

     */




}
